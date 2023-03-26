package ai.kalico.api.service.instagram4j.responses.fbsearch;

import ai.kalico.api.service.instagram4j.responses.IGPageRankTokenResponse;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FbsearchPlacesResponse extends IGResponse implements IGPageRankTokenResponse {

    private int num_results;
    private String rank_token;
    private String page_token;
    private String status;
    private boolean has_more;
    private List<SearchLocationLocation> items;

    @Getter @Setter
    public static class SearchLocationLocation {
        private String title;
        private String subtitle;
        private FbsearchLocation location;
    }

    @Getter @Setter
    public static class FbsearchLocation {
        private long pk;
        private long facebook_places_id;
        private String short_name;
        private String external_source;
        private String name;
        private String address;
        private String city;
        private double lng;
        private double lat;
    }
}
