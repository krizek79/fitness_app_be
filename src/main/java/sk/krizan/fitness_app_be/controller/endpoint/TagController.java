package sk.krizan.fitness_app_be.controller.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.TagCreateRequest;
import sk.krizan.fitness_app_be.controller.request.TagFilterRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.TagResponse;
import sk.krizan.fitness_app_be.model.mapper.TagMapper;
import sk.krizan.fitness_app_be.service.api.TagService;

@RestController
@RequiredArgsConstructor
@RequestMapping("tags")
public class TagController {

    private final TagService tagService;

    @PostMapping("filter")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public PageResponse<TagResponse> filterTags(@Valid TagFilterRequest request) {
        return tagService.filterTags(request);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TagResponse createTag(@Valid @RequestBody TagCreateRequest request) {
        return TagMapper.entityToResponse(tagService.createTag(request));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Long deleteTag(@PathVariable Long id) {
        return tagService.deleteTag(id);
    }
}
