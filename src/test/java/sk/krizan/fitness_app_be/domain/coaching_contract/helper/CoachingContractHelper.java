package sk.krizan.fitness_app_be.domain.coaching_contract.helper;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContract;
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContractStatus;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractCallerRole;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractCreateRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractFilterConnectionsRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractFilterRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.response.CoachingContractResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.helper.ProfileHelper;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CoachingContractHelper {

    public static CoachingContract createCoachingContract(
            @NotNull Profile coach,
            @NotNull Profile client
    ) {
        return createCoachingContract(coach, client, CoachingContractStatus.ACTIVE);
    }

    public static CoachingContract createCoachingContract(
            @NotNull Profile coach,
            @NotNull Profile client,
            @NotNull CoachingContractStatus status
    ) {
        CoachingContract coachingContract = new CoachingContract();
        coachingContract.setStatus(status);
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
        return createFilterRequest(page, size, sortBy, sortDirection, coachId, clientId, null, null);
    }

    public static CoachingContractFilterRequest createFilterRequest(
            @NotNull Integer page,
            @NotNull Integer size,
            @NotNull String sortBy,
            @NotNull String sortDirection,
            Long coachId,
            Long clientId,
            List<CoachingContractStatus> statuses,
            CoachingContractCallerRole callerRole
    ) {
        return CoachingContractFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .coachId(coachId)
                .clientId(clientId)
                .statuses(statuses)
                .callerRole(callerRole)
                .build();
    }

    public static CoachingContractFilterConnectionsRequest createFilterConnectionsRequest(
            @NotNull Integer page,
            @NotNull Integer size,
            @NotNull String sortBy,
            @NotNull String sortDirection,
            String name
    ) {
        return CoachingContractFilterConnectionsRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .name(name)
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

        Assertions.assertEquals(coachingContract.getStatus(), response.status());
    }

    public static void assertCreate(
            CoachingContractResponse response,
            Profile coach,
            Profile client
    ) {
        Assertions.assertNotNull(response);

        ProfileHelper.assertProfileSimpleResponse(coach, response.coach());
        ProfileHelper.assertProfileSimpleResponse(client, response.client());

        Assertions.assertEquals(CoachingContractStatus.PENDING, response.status());

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
