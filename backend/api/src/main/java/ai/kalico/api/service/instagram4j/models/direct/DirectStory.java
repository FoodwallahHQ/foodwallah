package ai.kalico.api.service.instagram4j.models.direct;

import ai.kalico.api.service.instagram4j.models.direct.item.ThreadRavenMediaItem;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DirectStory {
    private List<ThreadRavenMediaItem> items;
    private long last_activity_at;
    private boolean has_newer;
    private String next_cursor;
}
