package sk.krizan.fitness_app_be.domain.tag.service.api;

import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.tag.entity.Tag;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.request.TagCreateRequest;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.request.TagFilterRequest;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.response.TagResponse;

import java.util.Set;

public interface TagService {

    PageResponse<TagResponse> filterTags(TagFilterRequest request);

    Tag getTagById(Long id);

    Tag createTag(TagCreateRequest request);

    void deleteTag(Long id);

    Set<Tag> getOrCreateTags(Set<String> tagNames);

}
