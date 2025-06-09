package sk.krizan.fitness_app_be.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Week implements OrderableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cycle cycle;

    @Min(0)
    @NotNull
    @Column(name = "order_number")
    private Integer order;

    @NotNull
    private Boolean completed = false;

    @Length(max = 1024)
    private String note;

    @OneToMany(mappedBy = WeekWorkout.Fields.week, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<WeekWorkout> weekWorkoutList = new ArrayList<>();

    public void addToWeekWorkoutList(List<WeekWorkout> weekWorkoutList) {
        weekWorkoutList.forEach(weekWorkout -> weekWorkout.setWeek(this));
        this.getWeekWorkoutList().addAll(weekWorkoutList);
    }

    public void removeFromWeekWorkoutList(WeekWorkout weekWorkout) {
        if (weekWorkout == null) {
            return;
        }
        this.weekWorkoutList.remove(weekWorkout);
    }
}
