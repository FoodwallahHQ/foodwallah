package ai.kalico.api.service.instagram4j.models.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class User extends Profile {
    @JsonProperty("is_business")
    private boolean is_business;
    private int media_count;
    private int follower_count;
    private int following_count;
    private String biography;
    private String external_url;
    private List<ProfilePic> hd_profile_pic_versions;
    private ProfilePic hd_profile_pic_url_info;
    private int account_type;

    @Getter @Setter
    public static class ProfilePic implements Serializable {
        public String url;
        public int width;
        public int height;
    }
}
