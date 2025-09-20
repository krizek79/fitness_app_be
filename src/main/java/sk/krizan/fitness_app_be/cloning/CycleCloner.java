package sk.krizan.fitness_app_be.cloning;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Week;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CycleCloner extends AbstractCloner<Cycle> {

    private final WeekCloner weekCloner;

    @Override
    public Class<Cycle> getHandledClass() {
        return Cycle.class;
    }

    @Override
    public Cycle clone(Cycle original) {
        Cycle clone = new Cycle();
        clone.setName(original.getName());
        clone.setDescription(original.getDescription());
        clone.setLevel(original.getLevel());
        List<Week> clonedWeeks = original.getWeekList().stream()
                .map(weekCloner::clone)
                .toList();
        clone.addToWeekList(clonedWeeks);
        original.getAuthor().addToAuthoredCycleList(List.of(clone));
        //  skip goals

        return clone;
    }
}
