package ai.kalico.api.service.instagram4j.responses;

import ai.kalico.api.service.instagram4j.models.IGBaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IGResponse extends IGBaseModel {
    private String status;
    @JsonIgnore
    private int statusCode;
    private String message;
    private boolean spam;
    private boolean lock;
    private String feedback_title;
    private String feedback_message;
    private String error_type;
    private String checkpoint_url;
}
