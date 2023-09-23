package sk.krizan.fitness_app_be.model.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.request.CreateTagRequest;
import sk.krizan.fitness_app_be.controller.response.TagResponse;
import sk.krizan.fitness_app_be.model.entity.Tag;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TagMapper {

    public static TagResponse tagToResponse(Tag tag) {
        if (tag == null) {
            return null;
        }
        return TagResponse.builder()
            .id(tag.getId())
            .name(tag.getName())
            .build();
    }

    public static Tag createRequestToTag(CreateTagRequest request) {
        return Tag.builder()
            .name(request.name().toLowerCase())
            .build();
    }
}
