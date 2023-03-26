package ai.kalico.api.service.instagram4j.models.news;

import ai.kalico.api.service.instagram4j.models.IGBaseModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NewsStory extends IGBaseModel {
    private int type;
    private int story_type;
    private String pk;
}
