package ai.kalico.api.service.instagram4j.responses.media;

import ai.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RuploadPhotoResponse extends IGResponse {
    private String upload_id;
}
