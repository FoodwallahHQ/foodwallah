package ai.kalico.api.service.gif;

/**
 * @author Biz Melesse created on 1/12/23
 */
public interface GifService {

  /**
   * Generate a GIF of a video already stored on S3
   *
   * @param mediaId
   * @param start start time
   * @param end end time
   * @return
   */
  String generateGif(String mediaId, float start, float end);

  void deleteGif(String url);
}
