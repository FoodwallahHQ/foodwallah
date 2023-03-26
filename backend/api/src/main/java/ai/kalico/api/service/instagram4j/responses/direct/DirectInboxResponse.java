package ai.kalico.api.service.instagram4j.responses.direct;

import ai.kalico.api.service.instagram4j.models.direct.Inbox;
import ai.kalico.api.service.instagram4j.models.user.User;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectInboxResponse extends IGResponse {
    private User viewer;
    private Inbox inbox;
    private int seq_id;
    private int pending_requests_total;
    private User most_recent_inviter;
}
