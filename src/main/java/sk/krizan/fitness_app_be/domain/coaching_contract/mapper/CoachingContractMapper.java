package sk.krizan.fitness_app_be.domain.coaching_contract.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContract;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.response.CoachingContractResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.mapper.ProfileMapper;

import java.time.Instant;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoachingContractMapper {

    public static CoachingContractResponse entityToResponse(CoachingContract coachingContract) {
        return CoachingContractResponse.builder()
                .id(coachingContract.getId())
                .coach(ProfileMapper.entityToSimpleResponse(coachingContract.getCoach()))
                .client(ProfileMapper.entityToSimpleResponse(coachingContract.getClient()))
                .active(coachingContract.getActive())
                .startedAt(coachingContract.getStartedAt())
                .build();
    }

    public static CoachingContract createRequestToEntity(Profile coach, Profile client) {
        CoachingContract coachingContract = new CoachingContract();
        coachingContract.setStartedAt(Instant.now());
        coach.addToCoaching(coachingContract);
        client.addToCoachedBy(coachingContract);

        return coachingContract;
    }
}
