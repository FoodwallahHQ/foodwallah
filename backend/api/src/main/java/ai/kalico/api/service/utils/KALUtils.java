package ai.kalico.api.service.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Biz Melesse created on 1/2/23
 */
@Slf4j
public class KALUtils {

  @SneakyThrows
  public static String getCanonicalPath(String path) {
    String cPath = new File(path).getCanonicalPath();
    if (cPath.startsWith("/private")) {
      return cPath.replace("/private", "");
    }
    return cPath;
  }

  public static String generateUid() {
    StringBuilder builder = new StringBuilder();
    // An ID length of N gives 62^N unique IDs
    int contentIdLength = 8;
    for (int i = 0; i < contentIdLength; i++) {
      builder.append(getRandomCharacter());
    }
    return builder.toString();
  }

  public static Character getRandomCharacter() {
    Random random = new Random();
    String uidAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnoqprstuvwxyz0123456789";
    int index = random.nextInt(uidAlphabet.length());
    return uidAlphabet.charAt(index);
  }

  public static Platform getPlatform(String url) {
    URL parsedUrl = null;
    try {
      parsedUrl = new URL(url);
    } catch (MalformedURLException e) {
      log.error(e.getLocalizedMessage());
    }
    if (parsedUrl != null) {
      if (parsedUrl.getHost().toLowerCase().contains("youtube")) {
        return Platform.YOUTUBE;
      }
      else if (parsedUrl.getHost().toLowerCase().contains("instagram")) {
        return Platform.INSTAGRAM;
      }
    }
    return Platform.INVALID;
  }


  /**
   * Turn any mobile urls into regular urls and perform additional pre-processing as necessary
   *
   * @param url
   * @return
   */
  public static String normalizeUrl(String url) {
    if (url.toLowerCase().contains("youtube")) {
      // Turn youtube mobile url into regular url
      return url.replace("m.youtube", "www.youtube");
    }
    return url;
  }
}
