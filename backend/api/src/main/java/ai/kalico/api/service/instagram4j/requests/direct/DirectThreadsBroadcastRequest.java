package ai.kalico.api.service.instagram4j.requests.direct;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import ai.kalico.api.service.instagram4j.utils.IGUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DirectThreadsBroadcastRequest extends IGPostRequest<IGResponse> {
    @NonNull
    private final BroadcastPayload payload;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return payload;
    }

    @Override
    public String path() {
        return "direct_v2/threads/broadcast/" + payload.getItemType() + "/";
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

    @Getter @Setter
    @JsonInclude(Include.NON_NULL)
    public static abstract class BroadcastPayload extends IGPayload {
        private String action = "send_item";

        @JsonIgnore
        public abstract String getItemType();

        public abstract String getThread_ids();

        public abstract String getRecipient_users();
    }

    @Getter @Setter
    public static class BroadcastTextPayload extends BroadcastPayload {
        private final String thread_ids;
        private final String recipient_users;
        private final String text;

        public BroadcastTextPayload(String text, long... pks) {
            this.thread_ids = null;
            this.recipient_users = IGUtils.objectToJson(Arrays.asList(pks));
            this.text = text;
        }

        public BroadcastTextPayload(String text, String... thread_ids) {
            this.thread_ids = IGUtils.objectToJson(thread_ids);
            this.recipient_users = null;
            this.text = text;
        }

        public String getItemType() {
            return "text";
        }
    }

    @Getter @Setter
    public static class BroadcastMediaSharePayload extends BroadcastPayload {
        private final String thread_ids;
        private final String recipient_users;
        private final String text;
        private final String media_id;

        public BroadcastMediaSharePayload(String media_id, String text, long... pks) {
            this.thread_ids = null;
            this.recipient_users = IGUtils.objectToJson(Arrays.asList(pks));
            this.media_id = media_id;
            this.text = text;
        }

        public BroadcastMediaSharePayload(String media_id, String text, String... thread_ids) {
            this.thread_ids = IGUtils.objectToJson(thread_ids);
            this.recipient_users = null;
            this.media_id = media_id;
            this.text = text;
        }

        @Override
        public String getItemType() {
            return "media_share";
        }
    }

    @Getter @Setter
    public static class BroadcastReelSharePayload extends BroadcastPayload {
        private final String thread_ids;
        private final String recipient_users;
        private final String text;
        private final String media_id;

        public BroadcastReelSharePayload(String media_id, String text, long... pks) {
            this.thread_ids = null;
            this.recipient_users = IGUtils.objectToJson(Arrays.asList(pks));
            this.media_id = media_id;
            this.text = text;
        }

        public BroadcastReelSharePayload(String media_id, String text, String... thread_ids) {
            this.thread_ids = IGUtils.objectToJson(thread_ids);
            this.recipient_users = null;
            this.media_id = media_id;
            this.text = text;
        }

        @Override
        public String getItemType() {
            return "reel_share";
        }
    }

    @Getter @Setter
    public static class BroadcastStorySharePayload extends BroadcastPayload {
        private final String thread_ids;
        private final String recipient_users;
        private final String story_media_id;
        private final String text;

        public BroadcastStorySharePayload(String media_id, String text, long... pks) {
            this.thread_ids = null;
            this.recipient_users = IGUtils.objectToJson(Arrays.asList(pks));
            this.story_media_id = media_id;
            this.text = text;
        }

        public BroadcastStorySharePayload(String media_id, String text, String... thread_ids) {
            this.thread_ids = IGUtils.objectToJson(thread_ids);
            this.recipient_users = null;
            this.story_media_id = media_id;
            this.text = text;
        }

        @Override
        public String getItemType() {
            return "story_share";
        }
    }

    @Getter @Setter
    @AllArgsConstructor
    public static class BroadcastShareVoicePayload extends BroadcastPayload {
        private final String thread_ids;
        private final String recipient_users;
        private final String upload_id;
        private String waveform = IGUtils
                .objectToJson(new double[] {0.146, 0.5, 0.854, 1, 0.854, 0.5, 0.146, 0, 0.146,
                        0.5, 0.854, 1, 0.854, 0.5, 0.146, 0, 0.146, 0.5, 0.854, 1});
        private int waveform_sampling_frequency_hz = 10;

        public BroadcastShareVoicePayload(String upload_id, long... pks) {
            this.thread_ids = null;
            this.recipient_users = IGUtils.objectToJson(Arrays.asList(pks));
            this.upload_id = upload_id;
        }

        public BroadcastShareVoicePayload(String upload_id, String... upload_ids) {
            this.thread_ids = IGUtils.objectToJson(upload_ids);
            this.recipient_users = null;
            this.upload_id = upload_id;
        }

        @Override
        public String getItemType() {
            return "share_voice";
        }
    }

    @Getter @Setter
    public static class BroadcastProfilePayload extends BroadcastPayload {
        private final String thread_ids;
        private final String recipient_users;
        private final String profile_user_id;

        public BroadcastProfilePayload(String user_id, long... pks) {
            this.thread_ids = null;
            this.recipient_users = IGUtils.objectToJson(Arrays.asList(pks));
            this.profile_user_id = user_id;
        }

        public BroadcastProfilePayload(String user_id, String... thread_ids) {
            this.thread_ids = IGUtils.objectToJson(thread_ids);
            this.recipient_users = null;
            this.profile_user_id = user_id;
        }

        public String getItemType() {
            return "profile";
        }
    }

    @Getter @Setter
    public static class BroadcastConfigurePhotoPayload extends BroadcastPayload {
        private final String thread_ids;
        private final String recipient_users;
        private final String upload_id;

        public BroadcastConfigurePhotoPayload(String upload_id, long... pks) {
            this.thread_ids = null;
            this.recipient_users = IGUtils.objectToJson(Arrays.asList(pks));
            this.upload_id = upload_id;
        }

        public BroadcastConfigurePhotoPayload(String upload_id, String... thread_ids) {
            this.thread_ids = IGUtils.objectToJson(thread_ids);
            this.recipient_users = null;
            this.upload_id = upload_id;
        }

        public String getItemType() {
            return "configure_photo";
        }
    }

    @Getter @Setter
    public static class BroadcastConfigureVideoPayload extends BroadcastPayload {
        private final String thread_ids;
        private final String recipient_users;
        private final String upload_id;

        public BroadcastConfigureVideoPayload(String upload_id, long... pks) {
            this.thread_ids = null;
            this.recipient_users = IGUtils.objectToJson(Arrays.asList(pks));
            this.upload_id = upload_id;
        }

        public BroadcastConfigureVideoPayload(String upload_id, String... thread_ids) {
            this.thread_ids = IGUtils.objectToJson(thread_ids);
            this.recipient_users = null;
            this.upload_id = upload_id;
        }

        public String getItemType() {
            return "configure_video";
        }
    }

    @Getter @Setter
    public static class BroadcastLinkPayload extends BroadcastPayload {
        private final String thread_ids;
        private final String recipient_users;
        private final String link_text;
        private final String link_urls;

        public BroadcastLinkPayload(String text, String[] link_urls, long... pks) {
            this.thread_ids = null;
            this.recipient_users = IGUtils.objectToJson(Arrays.asList(pks));
            this.link_text = text;
            this.link_urls = IGUtils.objectToJson(link_urls);
        }

        public BroadcastLinkPayload(String text, String[] link_urls, String... thread_ids) {
            this.thread_ids = IGUtils.objectToJson(thread_ids);
            this.recipient_users = null;
            this.link_text = text;
            this.link_urls = IGUtils.objectToJson(link_urls);
        }

        public String getItemType() {
            return "link";
        }
    }

}
