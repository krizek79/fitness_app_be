package sk.krizan.fitness_app_be.domain.draft.helper;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.domain.draft.entity.Draft;
import sk.krizan.fitness_app_be.domain.draft.entity.DraftEntityType;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.request.DraftCreateRequest;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.request.DraftFilterRequest;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.request.DraftUpdateRequest;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.response.DraftResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DraftHelper {

    public static Draft createDraft(
            Profile profile,
            DraftEntityType entityType,
            String title,
            java.util.Map<String, Object> content
    ) {
        Draft draft = new Draft();
        draft.setProfile(profile);
        draft.setEntityType(entityType);
        draft.setTitle(title);
        draft.setContent(content);

        return draft;
    }

    public static DraftFilterRequest createFilterRequest(
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection,
            DraftEntityType entityType,
            String title
    ) {
        return DraftFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .entityType(entityType)
                .title(title)
                .build();
    }

    public static DraftCreateRequest createCreateRequest(
            DraftEntityType entityType,
            java.util.Map<String, Object> content
    ) {
        return DraftCreateRequest.builder()
                .entityType(entityType)
                .content(content)
                .build();
    }

    public static void assertCreateRequest(
            DraftCreateRequest request,
            DraftResponse response,
            Profile profile,
            String expectedTitle
    ) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(profile.getId(), response.profileId());
        Assertions.assertEquals(request.entityType(), response.entityType());
        Assertions.assertEquals(request.content(), response.content());
        Assertions.assertEquals(expectedTitle, response.title());
    }

    public static DraftUpdateRequest createUpdateRequest(@NotNull java.util.Map<String, Object> content) {
        return DraftUpdateRequest.builder()
                .content(content)
                .build();
    }

    public static void assertUpdateRequest(
            DraftUpdateRequest request,
            DraftResponse response,
            Draft draft,
            String expectedTitle
    ) {
        Assertions.assertNotNull(draft);
        Assertions.assertEquals(request.content(), draft.getContent());
        Assertions.assertEquals(expectedTitle, draft.getTitle());
        assertResponse(draft, response);
    }

    public static void assertResponse(Draft draft, DraftResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(draft.getId(), response.id());
        Assertions.assertEquals(draft.getEntityType(), response.entityType());
        Assertions.assertEquals(draft.getProfile().getId(), response.profileId());
        Assertions.assertEquals(draft.getTitle(), response.title());
        Assertions.assertEquals(draft.getContent(), response.content());
    }

    /**
     * Create a list of mock Drafts used for filter tests.
     * <br>
     * Each created Draft belongs to one of two profiles:
     * <ul>
     *     <li>profile1: contains "Plan A" and "Workout Plan A"</li>
     *     <li>profile2: contains "Plan A", "Workout Plan A" and "Workout Plan B"</li>
     * </ul>
     * Content is a simple map with keys `title` and `body`. Entity IDs are not set
     * by this helper (they are intended to be set by persistence in tests).
     *
     * @param profile1 owner of the first set of drafts
     * @param profile2 owner of the second set of drafts
     * @return list of mock Draft instances for filtering tests
     */
    public static List<Draft> createMockDraftListForFilter(Profile profile1, Profile profile2) {
        Draft draft1 = createDraft(profile1, DraftEntityType.PLAN, "Plan A", createMockContent("Plan A", "{\"someAttribute\":\"someValue1\"}"));
        Draft draft2 = createDraft(profile1, DraftEntityType.WORKOUT, "Workout Plan A", createMockContent("Workout Plan A", "{\"someAttribute\":\"someValue2\"}"));
        Draft draft3 = createDraft(profile2, DraftEntityType.PLAN, "Plan A", createMockContent("Plan A", "{\"someAttribute\":\"someValue1\"}"));
        Draft draft4 = createDraft(profile2, DraftEntityType.WORKOUT, "Workout Plan A", createMockContent("Workout Plan A", "{\"someAttribute\":\"someValue2\"}"));
        Draft draft5 = createDraft(profile2, DraftEntityType.WORKOUT, "Workout Plan B", createMockContent("Workout Plan B", "{\"someAttribute\":\"someValue3\"}"));
        Draft draft6 = createDraft(profile2, DraftEntityType.WORKOUT_TEMPLATE, "Workout template A", createMockContent("Workout template A", "{\"someAttribute\":\"someValue4\"}"));

        return new ArrayList<>(List.of(draft1, draft2, draft3, draft4, draft5, draft6));
    }

    public static Map<String, Object> createMockContent(String title, String body) {
        return Map.of(
                "title", title,
                "body", body
        );
    }
}
