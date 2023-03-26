package ai.kalico.api.service.instagram4j.responses.commerce;

import ai.kalico.api.service.instagram4j.models.commerce.Product;
import ai.kalico.api.service.instagram4j.models.user.Profile;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommerceProductsDetailsResponse extends IGResponse {
    private Profile merchant;
    private Product product_item;
}
