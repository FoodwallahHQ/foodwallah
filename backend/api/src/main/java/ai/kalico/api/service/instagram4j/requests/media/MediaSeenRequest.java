package ai.kalico.api.service.instagram4j.requests.media;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.models.media.reel.ReelMedia;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MediaSeenRequest extends IGPostRequest<IGResponse> {
    @NonNull
    private List<ReelMedia> reel_media_ids;

    @Override
    public String apiPath() {
        return "api/v2/";
    }

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new MediaSeenPayload(reel_media_ids);
    }

    @Override
    public String path() {
        return "media/seen/";
    }

    @Override
    public String getQueryString(IGClient client) {
        return mapQueryString("reel", "1");
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

    @Getter @Setter
    public class MediaSeenPayload extends IGPayload {
        private Map<String, String[]> reels;

        public MediaSeenPayload(List<ReelMedia> reelMedias) {
            this.reels = new HashMap<>();
            reelMedias.forEach(this::map_to_reels);
        }

        public void map_to_reels(ReelMedia media) {
            String key = String.format("%s_%s", media.getId(), media.getUser().getPk());
            String[] value = {
                    String.format("%s_%s", media.getTaken_at(), System.currentTimeMillis() / 1000)};

            reels.put(key, value);
        }
    }

}
