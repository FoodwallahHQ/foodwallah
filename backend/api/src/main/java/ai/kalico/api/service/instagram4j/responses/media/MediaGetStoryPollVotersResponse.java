package ai.kalico.api.service.instagram4j.responses.media;

import ai.kalico.api.service.instagram4j.models.media.reel.VoterInfo;
import ai.kalico.api.service.instagram4j.responses.IGPaginatedResponse;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MediaGetStoryPollVotersResponse extends IGResponse implements IGPaginatedResponse {
    private VoterInfo voter_info;

    @Override
    public String getNext_max_id() {
        return voter_info.getMax_id();
    }

    @Override
    public boolean isMore_available() {
        return voter_info.isMore_available();
    }

}
