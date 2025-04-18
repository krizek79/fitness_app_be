package sk.krizan.fitness_app_be.model.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.model.enums.MuscleGroup;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection(targetClass = MuscleGroup.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
        name = "exercise_muscle_group",
        joinColumns = @JoinColumn(name = "exercise_id")
    )
    @Column(name = "muscle_group")
    private final Set<MuscleGroup> muscleGroupSet = new HashSet<>();

    public void addToMuscleGroupSet(Set<MuscleGroup> muscleGroupSet) {
        this.getMuscleGroupSet().addAll(muscleGroupSet);
    }
}
