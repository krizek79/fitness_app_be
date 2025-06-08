package sk.krizan.fitness_app_be.cloning;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Profile;
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
    public Cycle clone(Cycle original, CloneContext context) {
        if (context.isAlreadyCloned(original)) {
            return context.getCachedClone(original);
        }

        Cycle clone = new Cycle();
        clone.setAuthor(original.getAuthor());
        clone.setName(original.getName());
        clone.setDescription(original.getDescription());
        clone.setLevel(original.getLevel());
        List<Week> clonedWeeks = original.getWeekList().stream()
                .map(week -> weekCloner.clone(week, context))
                .toList();
        clone.addToWeekList(clonedWeeks);
        Profile targetProfile = context.getTargetProfile();
        clone.setTrainee(targetProfile);
        targetProfile.addToAssignedCycleList(List.of(clone));
        //  skip goals

        context.cacheClone(original, clone);

        return clone;
    }
}
