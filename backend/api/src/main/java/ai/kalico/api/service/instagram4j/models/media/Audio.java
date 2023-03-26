package ai.kalico.api.service.instagram4j.models.media;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Audio {
    private String audio_src;
    private long duration;
    private double[] waveform_data;
    private int waveform_sampling_frequency_hz;
}
