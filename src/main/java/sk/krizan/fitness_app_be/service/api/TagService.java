package sk.krizan.fitness_app_be.service.api;

import java.util.Optional;
import sk.krizan.fitness_app_be.controller.request.TagCreateRequest;
import sk.krizan.fitness_app_be.controller.request.TagFilterRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.TagResponse;
import sk.krizan.fitness_app_be.model.entity.Tag;

public interface TagService {

    PageResponse<TagResponse> filterTags(TagFilterRequest request);
    Tag getTagById(Long id);
    Optional<Tag> findTagByName(String name);
    Tag createTag(TagCreateRequest request);
    Long deleteTag(Long id);
}
