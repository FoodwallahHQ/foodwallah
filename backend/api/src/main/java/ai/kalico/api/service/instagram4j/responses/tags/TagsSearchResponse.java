package ai.kalico.api.service.instagram4j.responses.tags;

import ai.kalico.api.service.instagram4j.responses.IGPageRankTokenResponse;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TagsSearchResponse extends IGResponse implements IGPageRankTokenResponse {
    private String rank_token;
    private String page_token;
    private String status;
    private boolean has_more;
    private List<SearchTagTag> results;

    @Getter @Setter
    public static class SearchTagTag {
        private long id;
        private String name;
        private String formatted_media_count;
        private String search_result_subtitle;
        private String profile_pic_url;
        private int media_count;
        private boolean use_default_avatar;
    }
}
