package sk.krizan.fitness_app_be.domain.cycle.cloner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.common.cloning.AbstractCloner;
import sk.krizan.fitness_app_be.domain.week.cloner.WeekCloner;
import sk.krizan.fitness_app_be.domain.cycle.entity.Cycle;
import sk.krizan.fitness_app_be.domain.week.entity.Week;

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
        clone.setTitle(original.getTitle());
        clone.setDescription(original.getDescription());
        clone.setLevel(original.getLevel());
        List<Week> clonedWeeks = original.getWeeks().stream()
                .map(weekCloner::clone)
                .toList();
        clonedWeeks.forEach(clone::addToWeeks);
        original.getAuthor().addToAuthoredCycles(clone);

        //  skip goals

        return clone;
    }
}
