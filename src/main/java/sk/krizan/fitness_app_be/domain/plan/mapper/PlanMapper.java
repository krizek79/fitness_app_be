package sk.krizan.fitness_app_be.domain.plan.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.plan.entity.Plan;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.request.PlanInputRequest;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.response.PlanResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.mapper.ProfileMapper;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlanMapper {

    public static PlanResponse entityToResponse(Plan plan) {
        return PlanResponse.builder()
                .id(plan.getId())
                .author(ProfileMapper.entityToSimpleResponse(plan.getAuthor()))
                .trainee(ProfileMapper.entityToSimpleResponse(plan.getTrainee()))
                .title(plan.getTitle())
                .description(plan.getDescription())
                .numberOfWeeks(plan.getWeeks() != null ? plan.getWeeks().size() : 0)
                .build();
    }

    public static void inputRequestToEntity(boolean isNew, PlanInputRequest request, Plan plan, Profile author, Profile trainee) {
        if (isNew) {
            author.addToAuthoredPlans(plan);
            trainee.addToAssignedPlans(plan);
        }

        //  If the plan already has a trainee, and it's different from the new trainee, we need to update the assigned plans for both trainees
        Profile originalTrainee = plan.getTrainee();
        if (originalTrainee != null && !originalTrainee.equals(trainee)) {
            originalTrainee.removeFromAssignedPlans(plan);
            trainee.addToAssignedPlans(plan);
        }

        plan.setTitle(request.title());
        plan.setDescription(request.description());
    }

}
