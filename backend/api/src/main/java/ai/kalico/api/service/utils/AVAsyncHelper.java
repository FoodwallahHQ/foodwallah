package ai.kalico.api.service.utils;

import ai.kalico.api.data.postgres.entity.RecipeEntity;
import ai.kalico.api.data.postgres.entity.SampledImageEntity;
import ai.kalico.api.data.postgres.entity.MediaContentEntity;
import ai.kalico.api.data.postgres.repo.RecipeRepo;
import ai.kalico.api.data.postgres.repo.SampledImageRepo;
import ai.kalico.api.data.postgres.repo.MediaContentRepo;
import ai.kalico.api.dto.Pair;
import ai.kalico.api.props.AWSProps;
import ai.kalico.api.props.DockerImageProps;
import ai.kalico.api.props.ProjectProps;
import ai.kalico.api.service.aws.S3Service;
import ai.kalico.api.service.download.DownloadService;
import ai.kalico.api.service.language.LanguageService;
import ai.kalico.api.service.ocr.OcrRequest;
import ai.kalico.api.service.ocr.OcrService;
import ai.kalico.api.service.stt.SpeechToTextService;
import ai.kalico.api.service.stt.SttRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Biz Melesse created on 12/31/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AVAsyncHelper {
  private final ShellService shell;
  private final SpeechToTextService stt;
  private final OcrService ocr;
  private final AWSProps awsProps;
  private final S3Service s3Service;
  private final MediaContentRepo mediaContentRepo;
  private final RecipeRepo recipeRepo;
  private final SampledImageRepo sampledImageRepo;
  private final DockerImageProps dockerImageProps;
  private final DownloadService downloadService;
  private final LanguageService languageService;
  private final ProjectProps projectProps;


  @Async
  public void uploadImages(OcrRequest ocrRequest, Long projectId) {
    List<String> fileNames;
    try {
      fileNames = getFileNames(".jpg", ocrRequest.getOcrPath());
      List<Pair<String, String>> fileNameKeyList = new ArrayList<>();
      List<String> keys = new ArrayList<>();
      for (String fileName : fileNames) {
        String[] tokens = fileName.split("/");
        if (tokens.length > 0) {
          String shortName = tokens[tokens.length - 1];
          String key = awsProps.getImageFolder() + "/" + ocrRequest.getMediaId() + "/" + shortName;
          Pair<String, String> p = new Pair<>(fileName, key);
          fileNameKeyList.add(p);
          keys.add(key);
        }
      }
      if (fileNameKeyList.size() > 0) {
        s3Service.uploadImagesAsync(awsProps.getBucket(), fileNameKeyList, S3Service.IMAGE_TYPE);
      }
      createSampledImageRecord(keys, projectId);
    } catch (IOException e) {
      log.error("AVAsyncHelper.uploadImages {}", e.getLocalizedMessage());
    }
  }

  @Async
  public void saveOcrTextToDB(String path, Long projectId) {
    try {
      List<String> fileNames = getFileNames(".txt", path);
      fileNames = fileNames
          .stream()
          .sorted().collect(Collectors.toList());
      StringBuilder builder = new StringBuilder("\n");
      String prevText = "";
      for (String fileName : fileNames) {
        File file = new File(fileName);
        String ocrText = new String(Files.readAllBytes(file.toPath()));
        if (isSimilar(prevText, ocrText) || ocrText.trim().length() < 2) {
          prevText = ocrText;
          continue;
        }
        prevText = ocrText;
        if (projectProps.isOcrHtmlLineBreaks()) {
          String[] tokens = fileName.split("/");
          if (tokens.length > 0) {
            String shortName = tokens[tokens.length - 1].replace(".txt", "");
            String formattedText =
                shortName + "<br>----------<br>" + ocrText.replace("\n", "<br>") + "<br>";
            builder.append(formattedText);
          }
        } else {
          builder.append(ocrText.replace("\n", ""));
        }
      }
      MediaContentEntity entity = mediaContentRepo.findByProjectId(projectId);
      if (entity != null) {
        entity.setOnScreenText(builder.toString());
        mediaContentRepo.save(entity);
      }

    } catch (IOException e) {
      log.error("AVAsyncHelper.saveOcrTextToDB {}", e.getLocalizedMessage());
    }

  }

  private boolean isSimilar(String prevText, String currText) {
    double threshold = 0.60;
    LevenshteinDistance d = new LevenshteinDistance();
    int distance = d.apply(prevText, currText);
    double ratio = 1.0 - ((double) distance) / (Math.max(prevText.length(), currText.length()));
    return ratio >= threshold;
  }

  @Transactional
  public void createSampledImageRecord(List<String> keys, Long projectId) {
    MediaContentEntity mediaContentEntity = mediaContentRepo.findByProjectId(projectId);
    List<SampledImageEntity> sampledImageEntities = new ArrayList<>();
    if (mediaContentEntity != null) {
      for (String key : keys) {
        SampledImageEntity sampledImage = new SampledImageEntity();
        sampledImage.setProjectId(mediaContentEntity.getProjectId());
        sampledImage.setImageKey(key);
        sampledImageEntities.add(sampledImage);
      }
      if (sampledImageEntities.size() > 0) {
        // Delete any existing images
        List<SampledImageEntity> toDelete = sampledImageRepo.findByProjectIdOrderByImageKeyAsc(
            mediaContentEntity.getProjectId());
        if (toDelete.size() > 0) {
          sampledImageRepo.deleteAll(toDelete);
        }
        sampledImageRepo.saveAll(sampledImageEntities);
      }
    }
  }

  @SneakyThrows
  @Async
  public void processAudio(String mediaId, Long projectId, boolean recipeContent) {
    String videoPath = getVideoPath(mediaId);
    String audioPath = getAudioPath(mediaId);
    log.info("Starting audio processing for mediaId={} projectId={}", mediaId, projectId);
    log.trace("Extracting audio track from video file {}", videoPath);
    if (Files.exists(Path.of(videoPath)) && !Files.exists(Path.of(audioPath))) {
      //-i input file
      //-vn no video
      //-ac audio channel (1 = mono, 2 = stereo)
      //-ar audio sample rate
      //-ab audio bitrate
      //-f output format
      //-acodec bitrate
      log.info("FFMPEG audio extraction in progress for mediaId {}", mediaId);
      String workingDir = KALUtils.getCanonicalPath(new File(videoPath).getParent());
      String[] command = {
          "docker",
          "run",
          "-v",
          workingDir + ":" + workingDir,
          "-w",
          workingDir,
          dockerImageProps.getFfmpeg(),
          "-i",
          KALUtils.getCanonicalPath(videoPath),
          "-vn",
          "-ac",
          "1",
          "-ar",
          "16000",
          "-acodec",
          "pcm_s16le",
          "-f",
          "wav",
          KALUtils.getCanonicalPath(audioPath)
      };
      log.info("Command: {}", String.join(" ", command));
      shell.exec(command);
      log.info("FFMPEG audio extraction done for mediaId {}", mediaId);
    }
    else {
      log.trace("Failed to locate video file at {}", videoPath);
    }
    runStt(mediaId, audioPath, projectId, recipeContent);
  }

  private void runStt(String mediaId, String audioPath, Long projectId, boolean recipeContent) {
    log.info("Starting STT service for mediaId={} projectId={}", mediaId, projectId);
    if (!Files.exists(Path.of(getTranscriptPath(mediaId)))) {
      SttRequest sttRequest = new SttRequest();
      sttRequest.setPath(audioPath);
      sttRequest.setMediaId(mediaId);
      sttRequest.setLanguage("English");
      stt.transcribe(sttRequest);
    } else {
      log.info("Skipping STT for mediaId={} projectId={}. Audio transcript already exists.", mediaId, projectId);
    }
    saveTranscriptToDb(mediaId, projectId, recipeContent);
    languageService.generateContent(projectId, mediaId, recipeContent);
  }

  @Async
  public void processImages(String mediaId, Long projectId) {
    OcrRequest ocrRequest = new OcrRequest();
    ocrRequest.setMediaId(mediaId);
    ocrRequest.setVideoPath(getVideoPath(mediaId));
    ocrRequest.setOcrPath(getOcrPath(mediaId));
    ocrRequest.setOcrTesseractPath(getOCrModifiedImagePath(ocrRequest.getOcrPath()));
    ocrRequest.setLanguage("eng");
    ocr.runOcr(ocrRequest);
    saveOcrTextToDB(ocrRequest.getOcrTesseractPath(), projectId);
    uploadImages(ocrRequest, projectId);
  }

  private void saveTranscriptToDb(String mediaId, Long projectId, boolean recipeContent) {
    String transcriptPath = getTranscriptPath(mediaId);
    if (Files.exists(Path.of(transcriptPath))) {
      File file = new File(transcriptPath);
      try {
        String transcript = new String(Files.readAllBytes(file.toPath()));
        if (recipeContent) {
          Optional<RecipeEntity> recipeEntityOpt = recipeRepo.findByContentId(mediaId);
          if (recipeEntityOpt.isPresent()) {
            RecipeEntity recipeEntity = recipeEntityOpt.get();
            recipeEntity.setTranscript(transcript);
            recipeRepo.save(recipeEntity);
          }
        } else {
          MediaContentEntity entity = mediaContentRepo.findByProjectId(projectId);
          if (entity != null) {
            entity.setRawTranscript(transcript);
            mediaContentRepo.save(entity);
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Async
  public void uploadRemoteImage(String url, String contentId) {
    String path = getJpgPath(contentId);
    downloadService.downloadImage(url, path);
    String key = awsProps.getImageFolder() + "/" + contentId;
    s3Service.uploadImageAsync(awsProps.getBucket(), key, path, S3Service.IMAGE_TYPE);
  }

  private String getOCrModifiedImagePath(String ocrPath) {
    return ocrPath + "/tesseract";
  }

  private String getOcrPath(String mediaId) {
    return getParentPath(mediaId) + "/" + "ocr";
  }

  public String getTranscriptPath(String mediaId) {
    return getParentPath(mediaId) + "/" + mediaId + ".wav.txt";
  }

  public String getJpgPath(String mediaId) {
    return getParentPath(mediaId) + "/" + mediaId + ".jpg";
  }

  public String getAudioPath(String mediaId) {
    return getParentPath(mediaId) + "/" + mediaId + ".wav";
  }

  public String getVideoPath(String mediaId) {
    return getParentPath(mediaId) + "/" + mediaId + ".mp4";
  }

  public String getHlsPath(String mediaId) {
    return getParentPath(mediaId) + "/hls/" + mediaId + ".m3u8";
  }

  public String getGifPath(String mediaId, String gifName) {
    return String.format("%s/%s",
        getParentPath(mediaId),
        gifName);
  }

  public String getGifName(String mediaId, String start, String duration) {
    return String.format("%ss%sd%s.gif",
        mediaId,
        start.replace(".", ""),
        duration.replace(".", ""));
  }

  public String getParentPath(String mediaId) {
    final String baseFilePath = "/tmp/videos";
    return baseFilePath + "/" + mediaId;
  }

  private List<String> getFileNames(String ext, String path) throws IOException {
    List<String> results = new ArrayList<>();
    File[] files = new File(path).listFiles();
    if (files != null) {
      for (File file : files) {
        if (file.isFile() && file.getName().contains(ext)) {
          results.add(KALUtils.getCanonicalPath(file.getPath()));
        }
      }
    }
    return results;
  }

  @Async
  public void processHls(String mediaId, String path) {
    String hlsPath = getHlsPath(mediaId);
    downloadService.generateHlsManifest(path, hlsPath);
    s3Service.uploadHlsData(awsProps.getBucket(), mediaId, hlsPath, awsProps.getStreamFolder());
  }
}
