package ai.kalico.api.service.stt;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Biz Melesse created on 11/27/22
 */
@Getter
@Setter
public class SttRequest {
  private String path;
  private String language;
  private String mediaId;
}
