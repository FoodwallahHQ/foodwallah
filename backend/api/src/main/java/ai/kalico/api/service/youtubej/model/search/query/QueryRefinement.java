package ai.kalico.api.service.youtubej.model.search.query;

import ai.kalico.api.service.youtubej.model.Utils;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class QueryRefinement extends Searchable {

    private final List<String> thumbnails;

    public QueryRefinement(JSONObject json) {
        super(json);
        thumbnails = Utils.parseThumbnails(json.getJSONObject("thumbnail"));
    }

    public List<String> thumbnails() {
        return thumbnails;
    }

    @Override
    protected String extractQuery(JSONObject json) {
        return Utils.parseRuns(json.getJSONObject("query"));
    }

    @Override
    protected String extractSearchPath(JSONObject json) {
        return json.getJSONObject("searchEndpoint")
                .getJSONObject("commandMetadata")
                .getJSONObject("webCommandMetadata")
                .getString("url");
    }

}
