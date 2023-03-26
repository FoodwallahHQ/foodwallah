package ai.kalico.api.service.stt;

/**
 * @author Biz Melesse created on 11/27/22
 */
public interface SpeechToTextService {

  Void transcribe(SttRequest request);

}
