package sk.krizan.fitness_app_be.domain.tag.service.api;

import sk.krizan.fitness_app_be.domain.tag.rest.dto.request.TagCreateRequest;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.request.TagFilterRequest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.response.TagResponse;
import sk.krizan.fitness_app_be.domain.tag.entity.Tag;

import java.util.Optional;

public interface TagService {

    PageResponse<TagResponse> filterTags(TagFilterRequest request);

    Tag getTagById(Long id);

    Optional<Tag> findTagByName(String name);

    Tag createTag(TagCreateRequest request);

    Long deleteTag(Long id);
}
