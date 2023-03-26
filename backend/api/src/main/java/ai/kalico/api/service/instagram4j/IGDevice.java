package ai.kalico.api.service.instagram4j;

import java.io.Serializable;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@EqualsAndHashCode(callSuper=false)
public class IGDevice implements Serializable {
    private static final long serialVersionUID = -823447845648l;
    private final String userAgent;
    private final String capabilities;
    private final Map<String, Object> deviceMap;
}
