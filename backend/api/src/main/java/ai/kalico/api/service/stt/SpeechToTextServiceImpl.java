package ai.kalico.api.service.stt;

import ai.kalico.api.props.DockerImageProps;
import ai.kalico.api.props.WhisperProps;
import ai.kalico.api.service.utils.KALUtils;
import ai.kalico.api.service.utils.ShellService;
import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Biz Melesse created on 11/27/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SpeechToTextServiceImpl implements SpeechToTextService {
  private final ShellService shell;
  private final DockerImageProps dockerImageProps;
  private final WhisperProps whisperProps;

  @SneakyThrows
  @Override
  public Void transcribe(SttRequest request) {
    File file = new File(request.getPath());
    if (file.exists()) {
      log.info("Whisper audio transcription in progress for {}", request.getPath());
      String workingDir = KALUtils.getCanonicalPath(new File(request.getPath()).getParent());
      String[] command = {
          "docker",
          "run",
          "-v",
          workingDir + ":" + workingDir,
          "-w",
          workingDir,
          dockerImageProps.getWhisper(),
          "whisper",
          KALUtils.getCanonicalPath(request.getPath()),
          "--model",
          whisperProps.getModel(),
          "--language",
          request.getLanguage(),
          "--output_dir",
          workingDir,
          "--fp16",
          "False"
      };
      log.info("Command: {}", String.join(" ", command));
      shell.exec(command);
      log.info("Whisper audio transcription complete for {}", request.getPath());
    } else {
      log.info("SpeechToTextServiceImpl.transcribe Audio does not exist at path {}", request.getPath());
    }
    return null;
  }
}
