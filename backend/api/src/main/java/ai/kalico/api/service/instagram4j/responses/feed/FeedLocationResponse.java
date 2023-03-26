package ai.kalico.api.service.instagram4j.responses.feed;

import ai.kalico.api.service.instagram4j.models.feed.Reel;
import ai.kalico.api.service.instagram4j.models.location.Location;
import ai.kalico.api.service.instagram4j.models.media.timeline.TimelineMedia;
import ai.kalico.api.service.instagram4j.responses.IGPaginatedResponse;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FeedLocationResponse extends IGResponse implements IGPaginatedResponse {
    private List<TimelineMedia> ranked_items;
    private List<TimelineMedia> items;
    private Reel story;
    private Location location;
    private int num_results;
    private int media_count;
    private String next_max_id;
    private boolean more_available;
}
