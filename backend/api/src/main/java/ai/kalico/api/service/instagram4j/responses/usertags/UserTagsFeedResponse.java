package ai.kalico.api.service.instagram4j.responses.usertags;

import ai.kalico.api.service.instagram4j.models.media.timeline.TimelineMedia;
import ai.kalico.api.service.instagram4j.responses.IGPaginatedResponse;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserTagsFeedResponse extends IGResponse implements IGPaginatedResponse{

	private List<TimelineMedia> items;
	
	private int num_results;
	private String next_max_id;
	private boolean more_available;
	
	
}
