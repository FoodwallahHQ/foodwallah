package ai.kalico.api.service.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Biz Melesse created on 11/29/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShellService {

  public void exec(String[] command) {
    ProcessBuilder processBuilder = new ProcessBuilder(command);
    processBuilder.redirectInput(Redirect.INHERIT);
    processBuilder.redirectOutput(Redirect.INHERIT);
    processBuilder.redirectError(Redirect.INHERIT);

    Process p = null;
    try {
      p = processBuilder.start();
    } catch (final IOException e) {
      e.printStackTrace();
    }
    //Wait to get exit value
    try {
      if (p != null) {
        final int exitValue = p.waitFor();
        if (exitValue == 0) {
          log.info("Successfully executed the command: " + String.join(" ", command));
          BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
          String s = null;
          while (true) {
            try {
              if ((s = stdInput.readLine()) == null)
                break;
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
            log.info(s);
          }
        }
        else {
          StringBuilder builder = new StringBuilder();
          builder.append("Failed to execute the following command: ")
              .append(String.join(" ", command))
              .append(" due to the following error(s):");
          try (final BufferedReader b = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
            String line;
            if ((line = b.readLine()) != null)
              builder.append(line).append("\n");
          } catch (final IOException e) {
            e.printStackTrace();
          }
          log.error(builder.toString());
          throw new RuntimeException(builder.toString());
        }
      }
    } catch (InterruptedException | RuntimeException e) {
      e.printStackTrace();
    }
  }

}
