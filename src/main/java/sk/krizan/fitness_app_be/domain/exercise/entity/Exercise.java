package sk.krizan.fitness_app_be.domain.exercise.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.common.audit.AuditableEntity;
import sk.krizan.fitness_app_be.domain.equipment.entity.Equipment;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity.ExerciseMuscleRole;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Exercise extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExerciseCategory exerciseCategory;

    @Builder.Default
    @OneToMany(mappedBy = ExerciseMuscleRole.Fields.exercise, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final Set<ExerciseMuscleRole> muscles = new HashSet<>();

    @Builder.Default
    @ElementCollection(targetClass = MovementPattern.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "exercise_movement_pattern",
            joinColumns = @JoinColumn(name = "exercise_id")
    )
    @Column(name = "movement_pattern")
    private final Set<MovementPattern> movementPatterns = new HashSet<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "exercise_required_equipment",
            joinColumns = @JoinColumn(name = "exercise_id"),
            inverseJoinColumns = @JoinColumn(name = "equipment_id")
    )
    private final Set<Equipment> requiredEquipment = new HashSet<>();

    @NotNull
    @Builder.Default
    private boolean deleted = false;

    public void addToMuscles(ExerciseMuscleRole exerciseMuscleRole) {
        if (exerciseMuscleRole == null) {
            return;
        }

        this.muscles.add(exerciseMuscleRole);
        exerciseMuscleRole.setExercise(this);
    }

    public void addToMovementPatterns(Set<MovementPattern> movementPatternSet) {
        this.getMovementPatterns().addAll(movementPatternSet);
    }

    public void addToRequiredEquipment(Equipment equipment) {
        this.getRequiredEquipment().add(equipment);
    }

}
