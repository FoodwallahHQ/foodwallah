package ai.kalico.api.service.ocr;

import ai.kalico.api.props.ProjectProps;
import ai.kalico.api.props.DockerImageProps;
import ai.kalico.api.service.utils.KALUtils;
import ai.kalico.api.service.utils.ShellService;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

/**
 * @author Biz Melesse created on 11/27/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OcrServiceImpl implements OcrService {
  private final ShellService shell;
  private final String imageFormat = "jpg";
  private final ProjectProps projectProps;
  private final DockerImageProps dockerImageProps;

  @SneakyThrows
  @Override
  public Void runOcr(OcrRequest request) {
    extractImages(request);
    addWhiteBorder(request);
    String testFile = request.getOcrTesseractPath() + "/0001." + imageFormat + ".txt";
    File file = new File(testFile);
    if (file.exists()) {
      log.info("Tesseract OCR ouptut already exists for video id={}. Aborting task.", request.getMediaId());
      return null;
    }
    log.info("Tesseract OCR in progress for video id={}", request.getMediaId());
    String workingDir = KALUtils.getCanonicalPath(new File(request.getOcrPath()).getParent());
    String[] command = {
        "docker",
        "run",
        "--entrypoint",
        "/bin/bash",
        "-v",
        workingDir + ":" + workingDir,
        "-w",
        workingDir,
        dockerImageProps.getTesseract(),
        "-c",
        String.format("for FILE in %s/*.%s; do tesseract $FILE $FILE --oem 1 -l eng; done",
            KALUtils.getCanonicalPath(request.getOcrTesseractPath()),
            imageFormat
        )
    };
    log.info("Command: {}", String.join(" ", command));
    shell.exec(command);
    log.info("Tesseract OCR finished for video id={}", request.getMediaId());
    return null;
  }

  @SneakyThrows
  private void addWhiteBorder(OcrRequest request) {
    // Add a white border around all the images so that Tesseract can process them without an issue
    File file = new File(request.getOcrTesseractPath());
    if (file.exists()) {
      log.info("OCR images with border path exists. Aborting task.");
      return;
    }
    copyOcrImages(request);
    log.info("ImageMagick is adding borders to OCR images");
    String workingDir = KALUtils.getCanonicalPath(new File(request.getOcrPath()).getParent());
    String[] command = {
        "docker",
        "run",
        "--entrypoint",
      "/bin/bash",
        "-v",
        workingDir + ":" + workingDir,
        "-w",
        workingDir,
        dockerImageProps.getImageMagick(),
        "-c",
        String.format("for FILE in %s/*.%s; do convert  $FILE  "
            + "-bordercolor White -border 10x10 $FILE; done",
            KALUtils.getCanonicalPath(request.getOcrTesseractPath()),
            imageFormat
        )
    };
    log.info("Command: {}", String.join(" ", command));
    shell.exec(command);
    log.info("ImageMagic border addition is done for OCR images with video id={}", request.getMediaId());
  }

  private void copyOcrImages(OcrRequest request) {
    String source = request.getOcrPath();
    File srcDir = new File(source);

    File destDir = new File(request.getOcrTesseractPath());

    try {
      FileUtils.copyDirectory(srcDir, destDir);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @SneakyThrows
  private void extractImages(OcrRequest request) {
    // Output 1 frame per second of video
      File file = new File(request.getOcrPath());
      if (file.exists()) {
        log.info("OCR path exists. Aborting image extraction.");
        return;
      }
      file.mkdirs();
      log.info("FFMPEG image sample generation in progress");
      String workingDir = KALUtils.getCanonicalPath(file.getParent());

    String[] command = {
          "docker",
          "run",
          "-v",
          workingDir + ":" + workingDir,
          "-w",
          workingDir,
          dockerImageProps.getFfmpeg(),
          "-i",
        KALUtils.getCanonicalPath(request.getVideoPath()),
          "-filter:v",
          "fps=" + projectProps.getFps(),
          KALUtils.getCanonicalPath(request.getOcrPath()) + "/" + "%04d." + imageFormat
      };
      log.info("Command: {}", String.join(" ", command));
      shell.exec(command);
      log.info("FFMPEG image sample generation in done for video id={}", request.getMediaId());
  }
}
