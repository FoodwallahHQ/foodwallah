package ai.kalico.api.service.instagram4j.models.discover;

import ai.kalico.api.service.instagram4j.models.IGBaseModel;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Cluster extends IGBaseModel {
    private String id;
    private String title;
    private String context;
    private String description;
    private List<String> labels;
    private String name;
    private String type;
}
