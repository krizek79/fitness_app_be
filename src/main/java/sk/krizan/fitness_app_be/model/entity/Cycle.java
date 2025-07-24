package sk.krizan.fitness_app_be.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import sk.krizan.fitness_app_be.model.enums.Level;

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

    @Enumerated(EnumType.STRING)
    private Level level;

    @Builder.Default
    @OneToMany(mappedBy = Week.Fields.cycle, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Week> weekList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = Goal.Fields.cycle, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Goal> goalList = new ArrayList<>();

    public void addToWeekList(List<Week> weekList) {
        weekList.forEach(week -> week.setCycle(this));
        this.getWeekList().addAll(weekList);
    }

    public void removeFromWeekList(Week week) {
        if (week == null) {
            return;
        }
        this.weekList.remove(week);
    }

    public void addToGoalList(List<Goal> goalList) {
        goalList.forEach(goal -> goal.setCycle(this));
        this.getGoalList().addAll(goalList);
    }

    public void removeFromGoalList(Goal goal) {
        if (goal == null) {
            return;
        }
        this.goalList.remove(goal);
    }
}
