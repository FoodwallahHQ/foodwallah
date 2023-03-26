package ai.kalico.api.dto;

import ai.kalico.api.service.openai.completion.CompletionChoice;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Biz Melesse created on 3/26/23
 */
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class GptResponse {
  private int sortOrder;
  private List<CompletionChoice> completionChoices;
  private final ClusterItem clusterItem;
}
