package sk.krizan.fitness_app_be.domain.workout_exercise_session.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
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
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.domain.workout_exercise_set_result.entity.WorkoutExerciseSetResult;
import sk.krizan.fitness_app_be.domain.workout_session.entity.WorkoutSession;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class WorkoutExerciseSession extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_session_id", nullable = false)
    private WorkoutSession workoutSession;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_exercise_id", nullable = false)
    private WorkoutExercise workoutExercise;

    @Min(0)
    @NotNull
    @Column(name = "order_number")
    private Integer order;

    @Length(max = 1024)
    private String note;

    @Builder.Default
    @OneToMany(mappedBy = "workoutExerciseSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order_number ASC")
    private List<WorkoutExerciseSetResult> workoutExerciseSetResults = new ArrayList<>();

    public void addToWorkoutExerciseSetResults(WorkoutExerciseSetResult result) {
        workoutExerciseSetResults.add(result);
        result.setWorkoutExerciseSession(this);
    }

}
