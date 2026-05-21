package sk.krizan.fitness_app_be.domain.cycle.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.cycle.rest.dto.request.CycleInputRequest;
import sk.krizan.fitness_app_be.domain.cycle.rest.dto.response.CycleResponse;
import sk.krizan.fitness_app_be.domain.cycle.entity.Cycle;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.mapper.ProfileMapper;
import sk.krizan.fitness_app_be.domain.reference.mapper.ReferenceDataMapper;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CycleMapper {

    public static CycleResponse entityToResponse(Cycle cycle) {
        return CycleResponse.builder()
                .id(cycle.getId())
                .author(ProfileMapper.entityToSimpleResponse(cycle.getAuthor()))
                .trainee(ProfileMapper.entityToSimpleResponse(cycle.getTrainee()))
                .title(cycle.getTitle())
                .description(cycle.getDescription())
                .numberOfWeeks(cycle.getWeeks() != null ? cycle.getWeeks().size() : 0)
                .level(cycle.getLevel() != null ? ReferenceDataMapper.enumToResponse(cycle.getLevel()) : null)
                .build();
    }

    public static void inputRequestToEntity(boolean isNew, CycleInputRequest request, Cycle cycle, Profile author, Profile trainee) {
        if (isNew) {
            author.addToAuthoredCycles(cycle);
            trainee.addToAssignedCycles(cycle);
        }

        //  If the cycle already has a trainee, and it's different from the new trainee, we need to update the assigned cycles for both trainees
        Profile originalTrainee = cycle.getTrainee();
        if (originalTrainee != null && !originalTrainee.equals(trainee)) {
            originalTrainee.removeFromAssignedCycles(cycle);
            trainee.addToAssignedCycles(cycle);
        }

        cycle.setTitle(request.title());
        cycle.setDescription(request.description());
        cycle.setLevel(request.level());
    }

}
