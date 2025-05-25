package sk.krizan.fitness_app_be.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.configuration.attribute_converter.DurationConverter;

import java.time.Duration;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class WorkoutExercise implements OrderableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(0)
    @NotNull
    @Column(name = "order_number")
    private Integer order;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Workout workout;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Exercise exercise;

    private Integer sets;

    private Integer repetitions;

    //  stored as a String
    @Convert(converter = DurationConverter.class)
    private Duration restDuration;
}
