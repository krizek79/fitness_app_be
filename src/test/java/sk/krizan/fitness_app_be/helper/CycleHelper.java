package sk.krizan.fitness_app_be.helper;

import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.controller.request.CycleCreateRequest;
import sk.krizan.fitness_app_be.controller.request.CycleFilterRequest;
import sk.krizan.fitness_app_be.controller.request.CycleUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.CycleResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Profile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CycleHelper {

    public static Cycle createMockCycle(Profile author, Profile trainee) {
        Cycle cycle = new Cycle();
        cycle.setAuthor(author);
        cycle.setTrainee(trainee);
        cycle.setName(UUID.randomUUID().toString());
        cycle.setDescription(DefaultValues.DEFAULT_VALUE);
        return cycle;
    }


    public static CycleCreateRequest createCreateRequest() {
        return CycleCreateRequest.builder()
                .name(DefaultValues.DEFAULT_VALUE)
                .build();
    }

    public static CycleUpdateRequest createUpdateRequest() {
        return CycleUpdateRequest.builder()
                .name(DefaultValues.DEFAULT_UPDATE_VALUE)
                .description(DefaultValues.DEFAULT_UPDATE_VALUE)
                .build();
    }

    public static void assertCycleResponse_get(Cycle cycle, CycleResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(cycle.getId(), response.id());
        Assertions.assertEquals(cycle.getName(), response.name());
        Assertions.assertEquals(cycle.getDescription(), response.description());
        Assertions.assertEquals(cycle.getAuthor().getId(), response.authorId());
        Assertions.assertEquals(cycle.getAuthor().getName(), response.authorName());
        Assertions.assertEquals(cycle.getTrainee().getId(), response.traineeId());
        Assertions.assertEquals(cycle.getTrainee().getName(), response.traineeName());
    }

    public static void assertCycleResponse_create(CycleCreateRequest request, Profile mockProfile, CycleResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertNotNull(response.authorId());
        Assertions.assertEquals(mockProfile.getId(), response.authorId());
        Assertions.assertNotNull(response.authorName());
        Assertions.assertEquals(mockProfile.getName(), response.authorName());
        Assertions.assertNotNull(response.traineeId());
        Assertions.assertEquals(mockProfile.getId(), response.traineeId());
        Assertions.assertNotNull(response.traineeName());
        Assertions.assertEquals(mockProfile.getName(), response.traineeName());
        Assertions.assertNotNull(response.name());
        Assertions.assertEquals(request.name(), response.name());
        Assertions.assertNull(response.description());
    }

    public static void assertCycleResponse_update(CycleUpdateRequest request, Profile mockProfile, CycleResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertNotNull(response.authorId());
        Assertions.assertEquals(mockProfile.getId(), response.authorId());
        Assertions.assertNotNull(response.authorName());
        Assertions.assertEquals(mockProfile.getName(), response.authorName());
        Assertions.assertNotNull(response.traineeId());
        Assertions.assertEquals(mockProfile.getId(), response.traineeId());
        Assertions.assertNotNull(response.traineeName());
        Assertions.assertEquals(mockProfile.getName(), response.traineeName());
        Assertions.assertNotNull(response.name());
        Assertions.assertEquals(request.name(), response.name());
        Assertions.assertNotNull(response.description());
        Assertions.assertEquals(request.description(), response.description());
    }

    public static void assertDelete(boolean exists, Cycle cycle, Long deletedCycleId) {
        assertFalse(exists);
        assertEquals(cycle.getId(), deletedCycleId);
    }

    /**
     * <ol>
     *     <li>Author - profile1, Trainee - profile1</li>
     *     <li>Author - profile1, Trainee - profile2</li>
     *     <li>Author - profile2, Trainee - profile1</li>
     *     <li>Author - profile2, Trainee - profile2</li>
     * </ol>
     * @return 4 Cycles
     */
    public static List<Cycle> createMockCycleListForFilter(Profile profile1, Profile profile2) {
        return new ArrayList<>(List.of(
                createMockCycle(profile1, profile1),
                createMockCycle(profile1, profile2),
                createMockCycle(profile2, profile1),
                createMockCycle(profile2, profile2)
        ));
    }

    public static CycleFilterRequest createFilterRequest(
            @NotNull Integer page,
            @NotNull Integer size,
            @NotNull String sortBy,
            @NotNull String sortDirection,
            Long authorId,
            Long traineeId,
            String name
    ) {
        return CycleFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .authorId(authorId)
                .traineeId(traineeId)
                .name(name)
                .build();
    }

    public static void assertFilter(List<Cycle> expectedList, CycleFilterRequest request, PageResponse<CycleResponse> response) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.pageNumber());
        Assertions.assertNotNull(response.pageSize());
        Assertions.assertNotNull(response.totalElements());
        Assertions.assertNotNull(response.totalPages());
        Assertions.assertNotNull(response.results());
        Assertions.assertFalse(response.results().isEmpty());
        Assertions.assertEquals(request.page(), response.pageNumber());
        Assertions.assertEquals(expectedList.size(), response.results().size());

        List<CycleResponse> results = response.results();
        results.sort(Comparator.comparingLong(CycleResponse::id));
        expectedList.sort(Comparator.comparingLong(Cycle::getId));
        for (int i = 0; i < results.size(); i++) {
            CycleResponse cycleResponse = results.get(i);
            Cycle cycle = expectedList.get(i);
            assertCycleResponse_get(cycle, cycleResponse);
        }
    }
}
