package sk.krizan.fitness_app_be.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.Length;
import sk.krizan.fitness_app_be.configuration.attribute_converter.DurationConverter;
import sk.krizan.fitness_app_be.model.enums.WorkoutExerciseSetType;

import java.time.Duration;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class WorkoutExerciseSet implements OrderableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "workout_exercise_id", nullable = false)
    private WorkoutExercise workoutExercise;

    @Min(0)
    @NotNull
    @Column(name = "order_number")
    private Integer order;

    @Enumerated(EnumType.STRING)
    private WorkoutExerciseSetType workoutExerciseSetType;

    @Min(1)
    private Integer goalRepetitions;

    @Min(1)
    private Integer actualRepetitions;

    @DecimalMin("0.125")
    private Double goalWeight;

    @DecimalMin("0.125")
    private Double actualWeight;

    @Convert(converter = DurationConverter.class)
    private Duration goalTime;

    @Convert(converter = DurationConverter.class)
    private Duration actualTime;

    @Convert(converter = DurationConverter.class)
    private Duration restDuration;

    @NotNull
    @Builder.Default
    private Boolean completed = false;

    @Length(max = 1024)
    private String note;
}
