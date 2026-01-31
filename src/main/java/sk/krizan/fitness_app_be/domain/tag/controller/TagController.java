package sk.krizan.fitness_app_be.domain.tag.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.request.TagCreateRequest;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.request.TagFilterRequest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.response.TagResponse;
import sk.krizan.fitness_app_be.domain.tag.mapper.TagMapper;
import sk.krizan.fitness_app_be.domain.tag.service.api.TagService;

@RestController
@RequiredArgsConstructor
public class TagController implements sk.krizan.fitness_app_be.domain.tag.rest.api.TagController {

    private final TagService tagService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public PageResponse<TagResponse> filterTags(TagFilterRequest request) {
        return tagService.filterTags(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public TagResponse createTag(TagCreateRequest request) {
        return TagMapper.entityToResponse(tagService.createTag(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Override
    public Long deleteTag(Long id) {
        return tagService.deleteTag(id);
    }
}
