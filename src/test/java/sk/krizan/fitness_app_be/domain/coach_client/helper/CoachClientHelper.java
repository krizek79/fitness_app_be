package sk.krizan.fitness_app_be.domain.coach_client.helper;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.domain.coach_client.entity.CoachClient;
import sk.krizan.fitness_app_be.domain.coach_client.rest.dto.request.CoachClientCreateRequest;
import sk.krizan.fitness_app_be.domain.coach_client.rest.dto.request.CoachClientFilterRequest;
import sk.krizan.fitness_app_be.domain.coach_client.rest.dto.response.CoachClientResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;

import java.time.Instant;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CoachClientHelper {

    public static CoachClient createCoachClient(
            @NotNull Profile coach,
            @NotNull Profile client
    ) {
        CoachClient coachClient = new CoachClient();
        coachClient.setStartedAt(Instant.now());
        coach.addToCoaching(coachClient);
        client.addToCoachedBy(coachClient);

        return coachClient;
    }

    public static CoachClientFilterRequest createFilterRequest(
            @NotNull Integer page,
            @NotNull Integer size,
            @NotNull String sortBy,
            @NotNull String sortDirection,
            Long coachId,
            Long clientId
    ) {
        return CoachClientFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .coachId(coachId)
                .clientId(clientId)
                .build();
    }

    public static CoachClientCreateRequest createCreateRequest(
            @NotNull Long coachId,
            @NotNull Long clientId
    ) {
        return CoachClientCreateRequest.builder()
                .coachId(coachId)
                .clientId(clientId)
                .build();
    }

    public static void assertCoachClientResponse(
            CoachClient coachClient,
            CoachClientResponse response
    ) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(coachClient.getId(), response.id());
        Assertions.assertEquals(coachClient.getCoach().getId(), response.coachId());
        Assertions.assertEquals(coachClient.getCoach().getName(), response.coachName());
        Assertions.assertEquals(coachClient.getCoach().getProfilePictureUrl(), response.coachPictureUrl());
        Assertions.assertEquals(coachClient.getClient().getId(), response.clientId());
        Assertions.assertEquals(coachClient.getClient().getName(), response.clientName());
        Assertions.assertEquals(coachClient.getClient().getProfilePictureUrl(), response.clientPictureUrl());
        Assertions.assertEquals(coachClient.getActive(), response.active());
        Assertions.assertEquals(coachClient.getStartedAt(), response.startedAt());
    }

    public static void assertCreate(
            CoachClientCreateRequest request,
            CoachClientResponse response,
            Profile coach,
            Profile client
    ) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(request.coachId(), response.coachId());
        Assertions.assertEquals(coach.getName(), response.coachName());
        Assertions.assertEquals(coach.getProfilePictureUrl(), response.coachPictureUrl());
        Assertions.assertEquals(request.clientId(), response.clientId());
        Assertions.assertEquals(client.getName(), response.clientName());
        Assertions.assertEquals(client.getProfilePictureUrl(), response.clientPictureUrl());
        Assertions.assertTrue(response.active());
        Assertions.assertNotNull(response.startedAt());

        Assertions.assertNotNull(coach.getCoaching());
        Assertions.assertFalse(coach.getCoaching().isEmpty());
        Assertions.assertEquals(1, coach.getCoaching().size());
        coach.getCoaching().stream()
                .map(CoachClient::getId)
                .forEach(id -> Assertions.assertEquals(response.id(), id));

        Assertions.assertNotNull(client.getCoachedBy());
        Assertions.assertFalse(client.getCoachedBy().isEmpty());
        Assertions.assertEquals(1, client.getCoachedBy().size());
        client.getCoachedBy().stream()
                .map(CoachClient::getId)
                .forEach(id -> Assertions.assertEquals(response.id(), id));
    }
}
