package sk.krizan.fitness_app_be.helper;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.controller.request.TagCreateRequest;
import sk.krizan.fitness_app_be.controller.request.TagFilterRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.TagResponse;
import sk.krizan.fitness_app_be.model.entity.Tag;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TagHelper {

    public static Tag createMockTag() {
        Tag tag = new Tag();
        tag.setName(UUID.randomUUID().toString());
        return tag;
    }

    public static TagCreateRequest createCreateRequest() {
        return TagCreateRequest.builder()
                .name(UUID.randomUUID().toString())
                .build();
    }

    public static TagFilterRequest createFilterRequest(
            @NotNull Integer page,
            @NotNull Integer size,
            @NotNull String sortBy,
            @NotNull String sortDirection,
            String name
    ) {
        return TagFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .name(name)
                .build();
    }

    public static void assertFilter(
            List<Tag> expectedList,
            TagFilterRequest request,
            PageResponse<TagResponse> response
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

        List<TagResponse> results = response.results();
        results.sort(Comparator.comparingLong(TagResponse::id));
        expectedList.sort(Comparator.comparingLong(Tag::getId));
        for (int i = 0; i < results.size(); i++) {
            TagResponse tagResponse = results.get(i);
            Tag tag = expectedList.get(i);
            assertTagResponse(tag, tagResponse);
        }
    }

    private static void assertTagResponse(Tag tag, TagResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(tag.getId(), response.id());
        Assertions.assertEquals(tag.getName(), response.name());
    }

    public static void assertTag_create(TagCreateRequest request, TagResponse response, Tag tag) {
        Assertions.assertNotNull(tag.getId());
        Assertions.assertEquals(request.name(), tag.getName().toLowerCase());
        assertTagResponse(tag, response);
    }

    public static void assertDelete(boolean exists, Tag savedMockTag, Long deletedTagId) {
        assertFalse(exists);
        assertEquals(savedMockTag.getId(), deletedTagId);
    }
}
