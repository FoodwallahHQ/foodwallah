package ai.kalico.api.service.instagram4j.requests.feed;

import ai.kalico.api.service.instagram4j.requests.IGGetRequest;
import ai.kalico.api.service.instagram4j.responses.feed.FeedReelsTrayResponse;

public class FeedReelsTrayRequest extends IGGetRequest<FeedReelsTrayResponse> {

    @Override
    public String path() {
        return "feed/reels_tray/";
    }

    @Override
    public Class<FeedReelsTrayResponse> getResponseType() {
        return FeedReelsTrayResponse.class;
    }

}
