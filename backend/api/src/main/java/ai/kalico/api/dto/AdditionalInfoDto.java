package ai.kalico.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Biz Melesse created on 4/22/23
 */
@Getter
@Setter
public class AdditionalInfoDto {
  @JsonProperty("cooking_time")
  int cookingTime;
  @JsonProperty("prep_time")
  int prepTime;
  int cuisine;
  int category;
}
