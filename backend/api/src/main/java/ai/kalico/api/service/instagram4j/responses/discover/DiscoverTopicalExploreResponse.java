package ai.kalico.api.service.instagram4j.responses.discover;

import ai.kalico.api.service.instagram4j.models.discover.Cluster;
import ai.kalico.api.service.instagram4j.models.discover.SectionalItem;
import ai.kalico.api.service.instagram4j.responses.IGPaginatedResponse;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DiscoverTopicalExploreResponse extends IGResponse implements IGPaginatedResponse {
    private List<SectionalItem> sectional_items;
    private String rank_token;
    private List<Cluster> clusters;
    private String next_max_id;
    private boolean more_available;
}
