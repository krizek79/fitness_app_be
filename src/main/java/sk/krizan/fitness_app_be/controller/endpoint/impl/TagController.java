package sk.krizan.fitness_app_be.controller.endpoint.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.TagCreateRequest;
import sk.krizan.fitness_app_be.controller.request.TagFilterRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.TagResponse;
import sk.krizan.fitness_app_be.model.mapper.TagMapper;
import sk.krizan.fitness_app_be.service.api.TagService;

@RestController
@RequiredArgsConstructor
public class TagController implements sk.krizan.fitness_app_be.controller.endpoint.api.TagController {

    private final TagService tagService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public PageResponse<TagResponse> filterTags(TagFilterRequest request) {
        return tagService.filterTags(request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public TagResponse createTag(TagCreateRequest request) {
        return TagMapper.entityToResponse(tagService.createTag(request));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public Long deleteTag(Long id) {
        return tagService.deleteTag(id);
    }
}
