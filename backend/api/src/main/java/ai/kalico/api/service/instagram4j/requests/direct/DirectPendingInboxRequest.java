package ai.kalico.api.service.instagram4j.requests.direct;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.requests.IGGetRequest;
import ai.kalico.api.service.instagram4j.responses.direct.DirectInboxResponse;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor
public class DirectPendingInboxRequest extends IGGetRequest<DirectInboxResponse> {
    @NonNull
    private String cursor;

    @Override
    public String path() {
        return "direct_v2/pending_inbox/";
    }

    @Override
    public String getQueryString(IGClient client) {
        return mapQueryString("cursor", cursor);
    }

    @Override
    public Class<DirectInboxResponse> getResponseType() {
        return DirectInboxResponse.class;
    }

}
