package ai.kalico.api.service.instagram4j.requests.creatives;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.creatives.CreativesAssetsResponse;
import lombok.Getter;
import lombok.Setter;

public class CreativesAssetsRequest extends IGPostRequest<CreativesAssetsResponse> {

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new CreativesAssetsPayload();
    }

    @Override
    public String path() {
        return "creatives/assets/";
    }

    @Override
    public Class<CreativesAssetsResponse> getResponseType() {
        return CreativesAssetsResponse.class;
    }

    @Getter @Setter
    public static class CreativesAssetsPayload extends IGPayload {
        private final String type = "static_stickers";
    }
}
