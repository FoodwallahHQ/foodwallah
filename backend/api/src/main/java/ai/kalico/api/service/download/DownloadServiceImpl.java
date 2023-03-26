package ai.kalico.api.service.download;

import com.fasterxml.jackson.databind.ObjectMapper;
import ai.kalico.api.props.AWSProps;
import ai.kalico.api.props.DockerImageProps;
import ai.kalico.api.service.aws.S3Service;
import ai.kalico.api.service.utils.KALUtils;
import ai.kalico.api.service.utils.ScraperUtils;
import ai.kalico.api.service.utils.ShellService;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

/**
 * @author Biz Melesse created on 12/24/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DownloadServiceImpl implements DownloadService {
  public static final String VIDEO = "video";
  private final DockerImageProps dockerImageProps;
  public final ObjectMapper objectMapper;
  public final S3Service s3Service;
  public final AWSProps awsProps;
  public final ScraperUtils scraperUtils;
  private final ShellService shell;

  @Override
  public void instagramReelDownload(String contentId, String permalink, String url, String path) {
    log.info("Downloading content for content ID {}: permalink={} mediaUrl={}",
        contentId, permalink, url);
    download(url, path);
  }

  @Override
  public void downloadImage(String url, String path) {
    download(url, path);
  }

  private void download(String url, String path) {
    try {
      final CloseableHttpClient httpClient = HttpClients.createDefault();
      HttpGet httpGet = new HttpGet(new URI(url));
      HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

      try (InputStream in = httpEntity.getContent()) {
        // Save the file to disk for processing
        if (!new File(path).exists()) {
          new File(path).mkdirs();
        }
        File output = new File(path);
        Files.copy(in, output.toPath(), StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @SneakyThrows
  @Override
  public void generateHlsManifest(String mp4Path, String hlsPath) {
    log.info("Generating HLS segments for {}", mp4Path);
    String workingDir = KALUtils.getCanonicalPath(new File(mp4Path).getParent());
    new File(KALUtils.getCanonicalPath(new File(hlsPath).getParent())).mkdir();
    String[] command = {
        "docker",
        "run",
        "-v",
        workingDir + ":" + workingDir,
        "-w",
        workingDir,
        dockerImageProps.getFfmpeg(),
        "-i",
        KALUtils.getCanonicalPath(mp4Path),
        "-codec:",
        "copy",
        "-start_number",
        "0",
        "-hls_time",
        "5",
        "-hls_list_size",
        "0",
        "-f",
        "hls",
        KALUtils.getCanonicalPath(hlsPath)
    };
    log.info("Command: {}", String.join(" ", command));
    shell.exec(command);
    log.info("Done with generating HLS segments for {}", mp4Path);
  }
}
