package sk.krizan.fitness_app_be.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Cycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Profile author;

    @ManyToOne
    private Profile trainee;

    @NotEmpty
    private String name;

    @Size(max = 2000)
    private String description;

    @OneToMany(mappedBy = Week.Fields.cycle, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Week> weekList = new ArrayList<>();

    @OneToMany(mappedBy = Goal.Fields.cycle, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Goal> goalList = new ArrayList<>();

    public void addToWeekList(List<Week> weekList) {
        weekList.forEach(week -> week.setCycle(this));
        this.getWeekList().addAll(weekList);
    }

    public void addToGoalList(List<Goal> goalList) {
        goalList.forEach(goal -> goal.setCycle(this));
        this.getGoalList().addAll(goalList);
    }
}
