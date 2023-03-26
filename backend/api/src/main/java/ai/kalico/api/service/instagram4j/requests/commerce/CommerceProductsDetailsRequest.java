package ai.kalico.api.service.instagram4j.requests.commerce;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.requests.IGGetRequest;
import ai.kalico.api.service.instagram4j.responses.commerce.CommerceProductsDetailsResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommerceProductsDetailsRequest extends IGGetRequest<CommerceProductsDetailsResponse> {
    @NonNull
    private String product_id, merchant_id;

    @Override
    public String path() {
        return "commerce/products/" + product_id + "/details/";
    }

    @Override
    public String getQueryString(IGClient client) {
        return mapQueryString("merchant_id", merchant_id);
    }

    @Override
    public Class<CommerceProductsDetailsResponse> getResponseType() {
        return CommerceProductsDetailsResponse.class;
    }

}
