package ai.kalico.api.service.instagram4j.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonInclude(Include.NON_NULL)
public class IGPayload extends IGBaseModel {
    private String _csrftoken;
    private String id;
    private String _uid;
    private String _uuid;
    private String guid;
    private String device_id;
    private String phone_id;
}
