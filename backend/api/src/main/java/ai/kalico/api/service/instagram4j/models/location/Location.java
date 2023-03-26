package ai.kalico.api.service.instagram4j.models.location;

import ai.kalico.api.service.instagram4j.models.IGBaseModel;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonInclude(Include.NON_NULL)
public class Location extends IGBaseModel {
    private Long pk;
    private String external_id;
    private String name;
    private String external_source;
    private Double lat;
    private Double lng;
    private String address;
    private Integer minimum_age;

    @Getter @Setter
    public static class Venue extends Location {
        @JsonAlias("external_id_source")
        private String external_source;
    }
}
