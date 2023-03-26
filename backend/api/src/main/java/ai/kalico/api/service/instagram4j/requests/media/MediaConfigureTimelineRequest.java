package ai.kalico.api.service.instagram4j.requests.media;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.models.location.Location;
import ai.kalico.api.service.instagram4j.models.media.UserTags.UserTagPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.media.MediaResponse.MediaConfigureTimelineResponse;
import ai.kalico.api.service.instagram4j.utils.IGUtils;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Collections;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
public class MediaConfigureTimelineRequest extends IGPostRequest<MediaConfigureTimelineResponse> {
    @NonNull
    private MediaConfigurePayload payload;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return payload;
    }

    @Override
    public String path() {
        return "media/configure/";
    }

    @Override
    public Class<MediaConfigureTimelineResponse> getResponseType() {
        return MediaConfigureTimelineResponse.class;
    }

    @Getter @Setter
    @Accessors(fluent = true)
    @JsonAutoDetect(fieldVisibility = Visibility.ANY)
    @JsonInclude(Include.NON_NULL)
    public static class MediaConfigurePayload extends IGPayload {
        private String upload_id;
        private String caption = "";
        private String disable_comments;
        private String location;
        private String usertags;

        public MediaConfigurePayload location(Location loc) {
            Location payloadLoc = new Location();

            payloadLoc.setExternal_id(loc.getExternal_id());
            payloadLoc.setName(loc.getName());
            payloadLoc.setAddress(loc.getAddress());
            payloadLoc.setLat(loc.getLat());
            payloadLoc.setLng(loc.getLng());
            payloadLoc.setExternal_source(loc.getExternal_source());
            payloadLoc.put(payloadLoc.getExternal_source() + "_id", payloadLoc.getExternal_id());
            this.location = IGUtils.objectToJson(payloadLoc);
            this.put("geotag_enabled", "1");
            this.put("posting_latitude", payloadLoc.getLat().toString());
            this.put("posting_longitude", payloadLoc.getLng().toString());
            this.put("media_latitude", payloadLoc.getLat().toString());
            this.put("media_longitude", payloadLoc.getLng().toString());

            return this;
        }

        public MediaConfigurePayload usertags(UserTagPayload... tags) {
            this.usertags = IGUtils.objectToJson(Collections.singletonMap("in", tags));

            return this;
        }

        public String usertags() {
            return this.usertags;
        }

    }

}
