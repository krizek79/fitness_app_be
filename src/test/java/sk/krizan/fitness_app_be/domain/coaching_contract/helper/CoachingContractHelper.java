package sk.krizan.fitness_app_be.domain.coaching_contract.helper;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContract;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractCreateRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractFilterRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.response.CoachingContractResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.helper.ProfileHelper;

import java.time.Instant;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CoachingContractHelper {

    public static CoachingContract createCoachingContract(
            @NotNull Profile coach,
            @NotNull Profile client
    ) {
        CoachingContract coachingContract = new CoachingContract();
        coachingContract.setStartedAt(Instant.now());
        coach.addToCoaching(coachingContract);
        client.addToCoachedBy(coachingContract);

        return coachingContract;
    }

    public static CoachingContractFilterRequest createFilterRequest(
            @NotNull Integer page,
            @NotNull Integer size,
            @NotNull String sortBy,
            @NotNull String sortDirection,
            Long coachId,
            Long clientId
    ) {
        return CoachingContractFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .coachId(coachId)
                .clientId(clientId)
                .build();
    }

    public static CoachingContractCreateRequest createCreateRequest(
            @NotNull Long coachId,
            @NotNull Long clientId
    ) {
        return CoachingContractCreateRequest.builder()
                .coachId(coachId)
                .clientId(clientId)
                .build();
    }

    public static void assertCoachingContractResponse(
            CoachingContract coachingContract,
            CoachingContractResponse response
    ) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(coachingContract.getId(), response.id());

        ProfileHelper.assertProfileSimpleResponse(coachingContract.getCoach(), response.coach());
        ProfileHelper.assertProfileSimpleResponse(coachingContract.getClient(), response.client());

        Assertions.assertEquals(coachingContract.getActive(), response.active());
        Assertions.assertEquals(coachingContract.getStartedAt(), response.startedAt());
    }

    public static void assertCreate(
            CoachingContractResponse response,
            Profile coach,
            Profile client
    ) {
        Assertions.assertNotNull(response);

        ProfileHelper.assertProfileSimpleResponse(coach, response.coach());
        ProfileHelper.assertProfileSimpleResponse(client, response.client());

        Assertions.assertTrue(response.active());
        Assertions.assertNotNull(response.startedAt());

        Assertions.assertNotNull(coach.getCoaching());
        Assertions.assertFalse(coach.getCoaching().isEmpty());
        Assertions.assertEquals(1, coach.getCoaching().size());
        coach.getCoaching().stream()
                .map(CoachingContract::getId)
                .forEach(id -> Assertions.assertEquals(response.id(), id));

        Assertions.assertNotNull(client.getCoachedBy());
        Assertions.assertFalse(client.getCoachedBy().isEmpty());
        Assertions.assertEquals(1, client.getCoachedBy().size());
        client.getCoachedBy().stream()
                .map(CoachingContract::getId)
                .forEach(id -> Assertions.assertEquals(response.id(), id));
    }
}
