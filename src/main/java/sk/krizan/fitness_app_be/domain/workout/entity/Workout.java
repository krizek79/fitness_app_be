package sk.krizan.fitness_app_be.domain.workout.entity;

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
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.Length;
import sk.krizan.fitness_app_be.common.audit.AuditableEntity;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.reference.entity.DistanceUnit;
import sk.krizan.fitness_app_be.domain.reference.entity.WeightUnit;
import sk.krizan.fitness_app_be.domain.tag.entity.Tag;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;

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
public class Workout extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToOne(mappedBy = WeekWorkout.Fields.workout)
    private WeekWorkout weekWorkout;

    @NotNull
    @ManyToOne
    private Profile author;

    @ManyToOne
    private Profile trainee;

    @Column(length = 1000)
    private String description;

    @NotNull
    @Builder.Default
    private Boolean isTemplate = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    private WeightUnit weightUnit;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DistanceUnit distanceUnit;

    @Length(max = 1024)
    private String note;

    @Builder.Default
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "workout_tag",
            joinColumns = @JoinColumn(name = "workout_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private final Set<Tag> tags = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = WorkoutExercise.Fields.workout, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<WorkoutExercise> workoutExercises = new ArrayList<>();

    public void addToTags(Set<Tag> tagSet) {
        if (tagSet == null || tagSet.isEmpty()) {
            return;
        }

        this.tags.addAll(tagSet);
    }

    public void addToWorkoutExercises(WorkoutExercise workoutExercise) {
        if (workoutExercise == null) {
            return;
        }

        workoutExercise.setWorkout(this);
        this.workoutExercises.add(workoutExercise);
    }

}
