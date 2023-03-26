package ai.kalico.api.service.instagram4j.responses.commerce;

import ai.kalico.api.service.instagram4j.models.discover.SectionalMediaGridItem;
import ai.kalico.api.service.instagram4j.responses.IGPaginatedResponse;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommerceDestinationResponse extends IGResponse implements IGPaginatedResponse {
    private List<SectionalMediaGridItem> sectional_items;
    private String rank_token;
    private String next_max_id;
    private boolean more_available;
}
