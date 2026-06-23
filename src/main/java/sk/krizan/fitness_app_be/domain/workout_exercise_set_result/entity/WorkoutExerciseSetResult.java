package sk.krizan.fitness_app_be.domain.workout_exercise_set_result.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
import sk.krizan.fitness_app_be.common.audit.AuditableEntity;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.entity.WorkoutExerciseSession;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSet;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSetType;

import java.math.BigDecimal;
import java.time.Duration;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class WorkoutExerciseSetResult extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_exercise_session_id", nullable = false)
    private WorkoutExerciseSession workoutExerciseSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_exercise_set_id")
    private WorkoutExerciseSet workoutExerciseSet;

    @Min(0)
    @NotNull
    @Column(name = "order_number")
    private Integer order;

    @Enumerated(EnumType.STRING)
    private WorkoutExerciseSetType workoutExerciseSetType;

    @Min(1)
    private Integer repetitions;

    @DecimalMin("0.125")
    @Column(precision = 10, scale = 3)
    private BigDecimal weight;

    private Duration timeSeconds;

    private BigDecimal distanceMeters;

    private Duration restDurationSeconds;

    @NotNull
    @Builder.Default
    private Boolean completed = false;

    @Length(max = 1024)
    private String note;

}
