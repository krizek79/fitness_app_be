package sk.krizan.fitness_app_be.helper;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.controller.request.CoachClientCreateRequest;
import sk.krizan.fitness_app_be.controller.request.CoachClientFilterRequest;
import sk.krizan.fitness_app_be.controller.response.CoachClientResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.model.entity.CoachClient;
import sk.krizan.fitness_app_be.model.entity.Profile;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoachClientHelper {

    public static CoachClient createMockCoachClient(
            @NotNull Profile coach,
            @NotNull Profile client
    ) {
        CoachClient coachClient = new CoachClient();
        coachClient.setStartedAt(Instant.now());
        coach.addToCoachingSet(Set.of(coachClient));
        client.addToBeingCoachedSet(Set.of(coachClient));

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

    public static void assertFilter(
            List<CoachClient> expectedList,
            CoachClientFilterRequest request,
            PageResponse<CoachClientResponse> response
    ) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.pageNumber());
        Assertions.assertNotNull(response.pageSize());
        Assertions.assertNotNull(response.totalElements());
        Assertions.assertNotNull(response.totalPages());
        Assertions.assertNotNull(response.results());
        Assertions.assertFalse(response.results().isEmpty());
        Assertions.assertEquals(request.page(), response.pageNumber());
        Assertions.assertEquals(expectedList.size(), response.results().size());

        List<CoachClientResponse> results = response.results();
        results.sort(Comparator.comparingLong(CoachClientResponse::id));
        expectedList.sort(Comparator.comparingLong(CoachClient::getId));
        for (int i = 0; i < results.size(); i++) {
            CoachClientResponse coachClientResponse = results.get(i);
            CoachClient coachClient = expectedList.get(i);
            assertCoachClientResponse(coachClientResponse, coachClient);
        }
    }

    public static void assertCoachClientResponse(
            CoachClientResponse response,
            CoachClient coachClient
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

        Assertions.assertNotNull(coach.getCoachingSet());
        Assertions.assertFalse(coach.getCoachingSet().isEmpty());
        Assertions.assertEquals(1, coach.getCoachingSet().size());
        coach.getCoachingSet().stream()
                .map(CoachClient::getId)
                .forEach(id -> Assertions.assertEquals(response.id(), id));

        Assertions.assertNotNull(client.getBeingCoachedSet());
        Assertions.assertFalse(client.getBeingCoachedSet().isEmpty());
        Assertions.assertEquals(1, client.getBeingCoachedSet().size());
        client.getBeingCoachedSet().stream()
                .map(CoachClient::getId)
                .forEach(id -> Assertions.assertEquals(response.id(), id));
    }
}
