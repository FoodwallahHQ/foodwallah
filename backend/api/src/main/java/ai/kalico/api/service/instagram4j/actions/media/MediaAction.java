package ai.kalico.api.service.instagram4j.actions.media;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.actions.feed.FeedIterable;
import ai.kalico.api.service.instagram4j.requests.media.MediaActionRequest;
import ai.kalico.api.service.instagram4j.requests.media.MediaCommentRequest;
import ai.kalico.api.service.instagram4j.requests.media.MediaConfigureSidecarRequest;
import ai.kalico.api.service.instagram4j.requests.media.MediaConfigureSidecarRequest.MediaConfigureSidecarPayload;
import ai.kalico.api.service.instagram4j.requests.media.MediaConfigureTimelineRequest;
import ai.kalico.api.service.instagram4j.requests.media.MediaConfigureTimelineRequest.MediaConfigurePayload;
import ai.kalico.api.service.instagram4j.requests.media.MediaConfigureToIgtvRequest;
import ai.kalico.api.service.instagram4j.requests.media.MediaEditRequest;
import ai.kalico.api.service.instagram4j.requests.media.MediaGetCommentsRequest;
import ai.kalico.api.service.instagram4j.requests.media.MediaInfoRequest;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import ai.kalico.api.service.instagram4j.responses.media.MediaCommentResponse;
import ai.kalico.api.service.instagram4j.responses.media.MediaGetCommentsResponse;
import ai.kalico.api.service.instagram4j.responses.media.MediaInfoResponse;
import ai.kalico.api.service.instagram4j.responses.media.MediaResponse;
import ai.kalico.api.service.instagram4j.responses.media.MediaResponse.MediaConfigureSidecarResponse;
import ai.kalico.api.service.instagram4j.responses.media.MediaResponse.MediaConfigureTimelineResponse;
import ai.kalico.api.service.instagram4j.responses.media.MediaResponse.MediaConfigureToIgtvResponse;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MediaAction {
    @NonNull
    private IGClient client;
    @NonNull
    private String media_id;
    
    public CompletableFuture<MediaCommentResponse> comment(String comment) {
        return new MediaCommentRequest(media_id, comment).execute(client);
    }
    
    public CompletableFuture<MediaResponse> editCaption(String caption) {
        return new MediaEditRequest(media_id, caption).execute(client);
    }
    
    public CompletableFuture<MediaInfoResponse> info() {
        return new MediaInfoRequest(media_id).execute(client);
    }
    
    public FeedIterable<MediaGetCommentsRequest, MediaGetCommentsResponse> comments() {
        return new FeedIterable<>(client, () -> new MediaGetCommentsRequest(media_id));
    }
    
    public CompletableFuture<IGResponse> action(MediaActionRequest.MediaAction action) {
        return new MediaActionRequest(media_id, action).execute(client);
    }
    
    public static MediaAction of(IGClient client, String media_id) {
        return new MediaAction(client, media_id);
    }
    
    public static CompletableFuture<MediaConfigureTimelineResponse> configureMediaToTimeline(IGClient client, String upload_id, MediaConfigurePayload payload) {
        return new MediaConfigureTimelineRequest(payload.upload_id(upload_id)).execute(client);
    }
    
    public static CompletableFuture<MediaConfigureSidecarResponse> configureAlbumToTimeline(IGClient client, MediaConfigureSidecarPayload payload) {
        return new MediaConfigureSidecarRequest(payload).execute(client);
    }
    
    public static CompletableFuture<MediaConfigureToIgtvResponse> configureToIgtv(IGClient client, String upload_id, String title, String caption, boolean postToFeed) {
        return new MediaConfigureToIgtvRequest(upload_id, title, caption, postToFeed).execute(client);
    }
}
