package sk.krizan.fitness_app_be.helper;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.controller.request.CycleCreateRequest;
import sk.krizan.fitness_app_be.controller.request.CycleFilterRequest;
import sk.krizan.fitness_app_be.controller.request.CycleUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.CycleResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.enums.Level;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CycleHelper {

    public static Cycle createMockCycle(Profile author, Profile trainee, Level level) {
        Cycle cycle = new Cycle();
        cycle.setAuthor(author);
        cycle.setTrainee(trainee);
        cycle.setName(UUID.randomUUID().toString());
        cycle.setDescription(DefaultValues.DEFAULT_VALUE);
        cycle.setLevel(level);
        return cycle;
    }


    public static CycleCreateRequest createCreateRequest() {
        return CycleCreateRequest.builder()
                .name(DefaultValues.DEFAULT_VALUE)
                .build();
    }

    public static CycleUpdateRequest createUpdateRequest(Level level, Long traineeId) {
        return CycleUpdateRequest.builder()
                .traineeId(traineeId)
                .name(DefaultValues.DEFAULT_UPDATE_VALUE)
                .description(DefaultValues.DEFAULT_UPDATE_VALUE)
                .levelKey(level.getKey())
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
        EnumHelper.assertEnumResponse(cycle.getLevel().getKey(), response.levelResponse());
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
        Assertions.assertNull(response.levelResponse());
        Assertions.assertFalse(mockProfile.getAuthoredCycleList().isEmpty());
        Assertions.assertFalse(mockProfile.getAssignedCycleList().isEmpty());
    }

    public static void assertCycleResponse_update(CycleUpdateRequest request, Profile author, Profile trainee, CycleResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertEquals(author.getId(), response.authorId());
        Assertions.assertEquals(author.getName(), response.authorName());
        Assertions.assertEquals(trainee.getId(), response.traineeId());
        Assertions.assertEquals(trainee.getName(), response.traineeName());
        Assertions.assertEquals(request.name(), response.name());
        Assertions.assertEquals(request.description(), response.description());
        EnumHelper.assertEnumResponse(request.levelKey(), response.levelResponse());
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
                createMockCycle(profile1, profile1, Level.BEGINNER),
                createMockCycle(profile1, profile2, Level.INTERMEDIATE),
                createMockCycle(profile2, profile1, Level.INTERMEDIATE),
                createMockCycle(profile2, profile2, Level.ADVANCED)
        ));
    }

    public static CycleFilterRequest createFilterRequest(
            @NotNull Integer page,
            @NotNull Integer size,
            @NotNull String sortBy,
            @NotNull String sortDirection,
            Long authorId,
            Long traineeId,
            String name,
            String levelKey
    ) {
        return CycleFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .authorId(authorId)
                .traineeId(traineeId)
                .name(name)
                .levelKey(levelKey)
                .build();
    }

    public static void assertFilter(List<Cycle> expectedList, CycleFilterRequest request, PageResponse<CycleResponse> response) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getPageNumber());
        Assertions.assertNotNull(response.getPageSize());
        Assertions.assertNotNull(response.getTotalElements());
        Assertions.assertNotNull(response.getTotalPages());
        Assertions.assertNotNull(response.getResults());
        Assertions.assertFalse(response.getResults().isEmpty());
        Assertions.assertEquals(request.page(), response.getPageNumber());
        Assertions.assertEquals(expectedList.size(), response.getResults().size());

        List<CycleResponse> results = response.getResults();
        results.sort(Comparator.comparingLong(CycleResponse::id));
        expectedList.sort(Comparator.comparingLong(Cycle::getId));
        for (int i = 0; i < results.size(); i++) {
            CycleResponse cycleResponse = results.get(i);
            Cycle cycle = expectedList.get(i);
            assertCycleResponse_get(cycle, cycleResponse);
        }
    }
}
