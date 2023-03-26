package ai.kalico.api.data.postgres.entity;

import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Bizuwork Melesse
 * created on March 24, 2023
 */
@Getter @Setter
@Entity
@Table(schema = "public", name = "recipe")
public class RecipeEntity {

    @Id
    @SequenceGenerator(name="pk_sequence",sequenceName="recipe_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.AUTO,generator="pk_sequence_recipe")
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "content_id")
    private String contentId;

    @Basic
    @Column(name = "slug")
    private String slug;

    @Basic
    @Column(name = "canonical_url")
    private String canonicalUrl;

    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "summary")
    private String summary;

    @Basic
    @Column(name = "transcript")
    private String transcript;

    @Basic
    @Column(name = "thumbnail")
    private String thumbnail;

    @Basic
    @Column(name = "num_ingredients")
    private Integer numIngredients;

    @Basic
    @Column(name = "num_steps")
    private Integer numSteps;

    @Basic
    @Column(name = "cooking_time_minutes")
    private Integer cookingTimeMinutes;

    @Column(name = "ingredients")
    String ingredients;

    @Column(name = "instructions")
    String instructions;

    @Basic
    @Column(name = "processed")
    private Boolean processed = false;

    @Basic
    @Column(name = "failed")
    private Boolean failed = false;

    @Basic
    @Column(name = "reason_failed")
    private String reasonFailed;

    @Basic
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Basic
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
