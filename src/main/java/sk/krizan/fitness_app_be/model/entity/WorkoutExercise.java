package sk.krizan.fitness_app_be.model.entity;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.model.enums.WorkoutExerciseType;

import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private WorkoutExerciseType workoutExerciseType;

    @OneToMany(mappedBy = WorkoutExerciseSet.Fields.workoutExercise, orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkoutExerciseSet> workoutExerciseSetList = new ArrayList<>();

    public void addToWorkoutExerciseSetList(List<WorkoutExerciseSet> workoutExerciseSetList) {
        this.getWorkoutExerciseSetList().addAll(workoutExerciseSetList);
        workoutExerciseSetList.forEach(workoutExerciseSet -> workoutExerciseSet.setWorkoutExercise(this));
    }

    public void removeFromWorkoutExerciseSetList(WorkoutExerciseSet workoutExerciseSet) {
        if (workoutExerciseSet == null) {
            return;
        }
        this.workoutExerciseSetList.remove(workoutExerciseSet);
    }
}
