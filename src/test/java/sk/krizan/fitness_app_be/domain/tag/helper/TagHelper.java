package sk.krizan.fitness_app_be.domain.tag.helper;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.domain.tag.entity.Tag;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.request.TagCreateRequest;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.request.TagFilterRequest;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.response.TagResponse;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TagHelper {

    public static Tag createTag() {
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

    public static void assertResponse(Tag tag, TagResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(tag.getId(), response.id());
        Assertions.assertEquals(tag.getName(), response.name());
    }

    public static void assertCreateRequestToEntity(Tag tag, TagCreateRequest request) {
        Assertions.assertNotNull(tag.getId());
        Assertions.assertEquals(request.name(), tag.getName().toLowerCase());
    }

}
