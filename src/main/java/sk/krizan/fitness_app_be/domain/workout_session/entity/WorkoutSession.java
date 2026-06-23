package sk.krizan.fitness_app_be.domain.workout_session.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.common.audit.AuditableEntity;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WorkoutStatus;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.entity.WorkoutExerciseSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class WorkoutSession extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "week_workout_id")
    private WeekWorkout weekWorkout;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private WorkoutStatus status = WorkoutStatus.NOT_STARTED;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    @Builder.Default
    @OneToMany(mappedBy = "workoutSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order_number ASC")
    private List<WorkoutExerciseSession> workoutExerciseSessions = new ArrayList<>();

    public void addToWorkoutExerciseSessions(WorkoutExerciseSession workoutExerciseSession) {
        workoutExerciseSessions.add(workoutExerciseSession);
        workoutExerciseSession.setWorkoutSession(this);
    }

}
