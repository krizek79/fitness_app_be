package sk.krizan.fitness_app_be.domain.cycle.helper;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.common.util.DefaultValues;
import sk.krizan.fitness_app_be.domain.cycle.entity.Cycle;
import sk.krizan.fitness_app_be.domain.cycle.entity.Level;
import sk.krizan.fitness_app_be.domain.cycle.rest.dto.request.CycleFilterRequest;
import sk.krizan.fitness_app_be.domain.cycle.rest.dto.request.CycleInputRequest;
import sk.krizan.fitness_app_be.domain.cycle.rest.dto.response.CycleResponse;
import sk.krizan.fitness_app_be.domain.goal.entity.Goal;
import sk.krizan.fitness_app_be.domain.goal.helper.GoalHelper;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalInputRequest;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.helper.ProfileHelper;
import sk.krizan.fitness_app_be.domain.reference_data.helper.ReferenceDataHelper;
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week.helper.WeekHelper;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekInputRequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CycleHelper {

    public static Cycle createCycle(Profile author, Profile trainee, List<Week> weeks, List<Goal> goals, Level level) {
        Cycle cycle = new Cycle();
        cycle.setAuthor(author);
        cycle.setTrainee(trainee);
        cycle.setTitle(UUID.randomUUID().toString());
        cycle.setDescription(DefaultValues.DEFAULT_VALUE);
        cycle.setLevel(level);

        weeks.forEach(cycle::addToWeeks);
        goals.forEach(cycle::addToGoals);

        return cycle;
    }

    /**
     * <ol>
     *     <li>Author - profile1, Trainee - profile1</li>
     *     <li>Author - profile1, Trainee - profile2</li>
     *     <li>Author - profile2, Trainee - profile1</li>
     *     <li>Author - profile2, Trainee - profile2</li>
     * </ol>
     *
     * @return 4 new Cycles
     */
    public static List<Cycle> createMockCycleListForFilter(Profile profile1, Profile profile2) {
        return new ArrayList<>(List.of(
                createCycle(profile1, profile1, new ArrayList<>(), new ArrayList<>(), Level.BEGINNER),
                createCycle(profile1, profile2, new ArrayList<>(), new ArrayList<>(), Level.INTERMEDIATE),
                createCycle(profile2, profile1, new ArrayList<>(), new ArrayList<>(), Level.INTERMEDIATE),
                createCycle(profile2, profile2, new ArrayList<>(), new ArrayList<>(), Level.ADVANCED)
        ));
    }

    public static CycleInputRequest createInputRequest(
            Long traineeId,
            Level level,
            List<GoalInputRequest> goals,
            List<WeekInputRequest> weeks
    ) {
        return CycleInputRequest.builder()
                .traineeId(traineeId)
                .title(UUID.randomUUID().toString())
                .description(UUID.randomUUID().toString())
                .level(level)
                .goals(goals)
                .weeks(weeks)
                .build();
    }

    public static CycleFilterRequest createFilterRequest(
            @NotNull Integer page,
            @NotNull Integer size,
            @NotNull String sortBy,
            @NotNull String sortDirection,
            Long authorId,
            Long traineeId,
            String title,
            Level level
    ) {
        return CycleFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .authorId(authorId)
                .traineeId(traineeId)
                .title(title)
                .level(level)
                .build();
    }

    public static void assertCycleResponse(Cycle cycle, CycleResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(cycle.getId(), response.id());
        Assertions.assertEquals(cycle.getTitle(), response.title());
        Assertions.assertEquals(cycle.getDescription(), response.description());
        ProfileHelper.assertProfileSimpleResponse(cycle.getAuthor(), response.author());
        ProfileHelper.assertProfileSimpleResponse(cycle.getTrainee(), response.trainee());
        ReferenceDataHelper.assertReferenceDataResponse(cycle.getLevel(), response.level());
    }

    public static void assertInputToEntity(Cycle cycle, CycleInputRequest request) {
        Assertions.assertNotNull(cycle);
        Assertions.assertNotNull(cycle.getId());
        Assertions.assertEquals(request.title(), cycle.getTitle());
        Assertions.assertEquals(request.description(), cycle.getDescription());
        Assertions.assertEquals(request.level(), cycle.getLevel());

        if (request.traineeId() != null) {
            Assertions.assertEquals(request.traineeId(), cycle.getTrainee().getId());
        }

        assertGoalInputRequestsToGoals(cycle.getGoals(), request.goals());
        assertWeekInputRequestsToWeeks(cycle.getWeeks(), request.weeks());
    }

    private static void assertWeekInputRequestsToWeeks(List<Week> weeks, List<WeekInputRequest> weekInputRequests) {
        Assertions.assertEquals(weekInputRequests.size(), weeks.size());
        List<Week> sortedWeeks = weeks.stream()
                .sorted(Comparator.nullsLast(Comparator.comparing(Week::getOrder)))
                .toList();
        List<WeekInputRequest> sortedWeekInputRequests = weekInputRequests.stream()
                .sorted(Comparator.comparing(WeekInputRequest::order))
                .toList();

        for (int i = 0; i < sortedWeeks.size(); i++) {
            Week week = sortedWeeks.get(i);
            WeekInputRequest weekInputRequest = sortedWeekInputRequests.get(i);
            WeekHelper.assertInputToEntity(week, weekInputRequest);
        }
    }

    private static void assertGoalInputRequestsToGoals(List<Goal> goals, List<GoalInputRequest> goalInputRequests) {
        Assertions.assertEquals(goalInputRequests.size(), goals.size());
        List<Goal> sortedGoals = goals.stream()
                .sorted(Comparator.nullsLast(Comparator.comparing(Goal::getId)))
                .toList();
        List<GoalInputRequest> sortedGoalInputRequests = goalInputRequests.stream()
                .sorted(Comparator.comparing(
                        GoalInputRequest::id,
                        Comparator.nullsLast(Long::compareTo)
                ))
                .toList();

        for (int i = 0; i < sortedGoals.size(); i++) {
            Goal goal = sortedGoals.get(i);
            GoalInputRequest goalInputRequest = sortedGoalInputRequests.get(i);
            GoalHelper.assertInputToEntity(goal, goalInputRequest);
        }
    }

}