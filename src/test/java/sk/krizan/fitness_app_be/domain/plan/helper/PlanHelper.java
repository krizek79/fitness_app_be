package sk.krizan.fitness_app_be.domain.plan.helper;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.common.util.DefaultValues;
import sk.krizan.fitness_app_be.domain.plan.entity.Plan;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.request.PlanFilterRequest;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.request.PlanInputRequest;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.response.PlanDetailResponse;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.response.PlanSimpleResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.helper.ProfileHelper;
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week.helper.WeekHelper;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekInputRequest;
import sk.krizan.fitness_app_be.domain.week.rest.dto.response.WeekSimpleResponse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlanHelper {

    public static Plan createPlan(Profile author, Profile trainee, List<Week> weeks) {
        Plan plan = new Plan();
        plan.setAuthor(author);
        plan.setTrainee(trainee);
        plan.setTitle(UUID.randomUUID().toString());
        plan.setDescription(DefaultValues.DEFAULT_VALUE);

        weeks.forEach(plan::addToWeeks);

        return plan;
    }

    /**
     * <ol>
     *     <li>Author - profile1, Trainee - profile1</li>
     *     <li>Author - profile1, Trainee - profile2</li>
     *     <li>Author - profile2, Trainee - profile1</li>
     *     <li>Author - profile2, Trainee - profile2</li>
     * </ol>
     *
     * @return 4 new Plans
     */
    public static List<Plan> createMockPlanListForFilter(Profile profile1, Profile profile2) {
        return new ArrayList<>(List.of(
                createPlan(profile1, profile1, new ArrayList<>()),
                createPlan(profile1, profile2, new ArrayList<>()),
                createPlan(profile2, profile1, new ArrayList<>()),
                createPlan(profile2, profile2, new ArrayList<>())
        ));
    }

    public static PlanInputRequest createInputRequest(
            Long traineeId,
            List<WeekInputRequest> weeks
    ) {
        return PlanInputRequest.builder()
                .traineeId(traineeId)
                .title(UUID.randomUUID().toString())
                .description(UUID.randomUUID().toString())
                .weeks(weeks)
                .build();
    }

    public static PlanFilterRequest createFilterRequest(
            @NotNull Integer page,
            @NotNull Integer size,
            @NotNull String sortBy,
            @NotNull String sortDirection,
            Long authorId,
            Long traineeId,
            String title
    ) {
        return PlanFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .authorId(authorId)
                .traineeId(traineeId)
                .title(title)
                .build();
    }

    public static void assertPlanSimpleResponse(Plan plan, PlanSimpleResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(plan.getId(), response.id());
        Assertions.assertEquals(plan.getTitle(), response.title());
        ProfileHelper.assertProfileSimpleResponse(plan.getAuthor(), response.author());
        ProfileHelper.assertProfileSimpleResponse(plan.getTrainee(), response.trainee());
        Assertions.assertEquals(plan.getWeeks().size(), response.numberOfWeeks());
        Integer numberOfCompletedWeeks = Math.toIntExact(plan.getWeeks().stream()
                .filter(Week::getCompleted)
                .count());
        Assertions.assertEquals(numberOfCompletedWeeks, response.numberOfCompletedWeeks());
    }

    public static void assertPlanDetailResponse(Plan plan, PlanDetailResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(plan.getId(), response.id());
        Assertions.assertEquals(plan.getTitle(), response.title());
        ProfileHelper.assertProfileSimpleResponse(plan.getAuthor(), response.author());
        ProfileHelper.assertProfileSimpleResponse(plan.getTrainee(), response.trainee());

        Assertions.assertEquals(plan.getWeeks().size(), response.weeks().size());
        List<Week> sortedWeeks = plan.getWeeks().stream().sorted(Comparator.nullsLast(Comparator.comparing(Week::getOrder))).toList();
        for (int i = 0; i < sortedWeeks.size(); i++) {
            Week week = sortedWeeks.get(i);
            WeekSimpleResponse weekSimpleResponse = response.weeks().get(i);
            WeekHelper.assertWeekSimpleResponse(week, weekSimpleResponse);
        }
    }

    public static void assertInputToEntity(Plan plan, PlanInputRequest request) {
        Assertions.assertNotNull(plan);
        Assertions.assertNotNull(plan.getId());
        Assertions.assertEquals(request.title(), plan.getTitle());
        Assertions.assertEquals(request.description(), plan.getDescription());

        if (request.traineeId() != null) {
            Assertions.assertEquals(request.traineeId(), plan.getTrainee().getId());
        }

        assertWeekInputRequestsToWeeks(plan.getWeeks(), request.weeks());
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

    public static void assertDelete(boolean planExists, boolean weekExists) {
        Assertions.assertFalse(planExists);
        Assertions.assertFalse(weekExists);
    }

}