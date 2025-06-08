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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.model.enums.WeightUnit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @NotNull
    @ManyToOne
    private Profile author;

    @ManyToOne
    private Profile trainee;

    @ManyToMany
    @JoinTable(
            name = "workout_tag",
            joinColumns = @JoinColumn(name = "workout_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private final Set<Tag> tagSet = new HashSet<>();

    @OneToMany(mappedBy = WorkoutExercise.Fields.workout, orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<WorkoutExercise> workoutExerciseList = new ArrayList<>();

    @Column(length = 1000)
    private String description;

    @NotNull
    private Boolean isTemplate = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    private WeightUnit weightUnit;

    public void addToTagSet(Set<Tag> tagSet) {
        this.getTagSet().addAll(tagSet);
    }

    public void addToWorkoutExerciseList(List<WorkoutExercise> workoutExerciseList) {
        this.getWorkoutExerciseList().addAll(workoutExerciseList);
        workoutExerciseList.forEach(workoutExercise -> workoutExercise.setWorkout(this));
    }

    public void removeFromWorkoutExerciseList(WorkoutExercise workoutExercise) {
        if (workoutExercise == null) {
            return;
        }
        this.workoutExerciseList.remove(workoutExercise);
    }
}
