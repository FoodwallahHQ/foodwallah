package ai.kalico.api.service.instagram4j.models.media.reel.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Getter @Setter
@SuperBuilder
@JsonInclude(Include.NON_NULL)
public class StoryQuizItems extends ReelMetadataItem {
    @NonNull
    private String question;
    private int correct_answer;
    @NonNull
    private List<Option> options;
    @Builder.Default
    private String text_color = "#ffffff";
    @Builder.Default
    private String start_background_color = "#262626";
    @Builder.Default
    private String end_background_color = "#262626";

    @Override
    public String key() {
        return "story_quizs";
    }

    @Getter @Setter
    @Builder
    public static class Option {
        private String text;
        @Builder.Default
        private int count = 0;
    }

}
