package ai.kalico.api.service.instagram4j.models.user;

import ai.kalico.api.service.instagram4j.models.IGBaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Data
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Profile extends IGBaseModel implements Serializable {
    private static final long serialVersionUID = -892648357982l;
    @EqualsAndHashCode.Include
    private Long pk;
    private String username;
    private String full_name;
    @JsonProperty("is_private")
    private boolean is_private;
    private String profile_pic_url;
    private String profile_pic_id;
    @JsonProperty("is_verified")
    private boolean is_verified;
    private boolean has_anonymous_profile_picture;
}
