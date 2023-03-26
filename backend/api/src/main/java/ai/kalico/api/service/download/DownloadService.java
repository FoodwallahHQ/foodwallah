package ai.kalico.api.service.download;

/**
 * @author Biz Melesse created on 12/24/22
 */
public interface DownloadService {
  void instagramReelDownload(String contentId, String permalink, String url, String path);
  void downloadImage(String url, String path);

  /**
   * Generate HTTP Live Streaming (HLS) files and upload them to S3.
   * HTML5 video player uses the HLS index generated in this process
   * to stream the media content to the client.
   *
   * @param mp4Path
   * @param hlsPath
   */
  void generateHlsManifest(String mp4Path, String hlsPath);


}
