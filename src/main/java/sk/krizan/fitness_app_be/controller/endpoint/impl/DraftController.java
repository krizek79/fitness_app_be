package sk.krizan.fitness_app_be.controller.endpoint.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.draft.DraftCreateRequest;
import sk.krizan.fitness_app_be.controller.request.draft.DraftFilterRequest;
import sk.krizan.fitness_app_be.controller.request.draft.DraftUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.DraftResponse;
import sk.krizan.fitness_app_be.model.mapper.DraftMapper;
import sk.krizan.fitness_app_be.service.api.DraftService;

@RestController
@RequiredArgsConstructor
public class DraftController implements sk.krizan.fitness_app_be.controller.endpoint.api.DraftController {

    private final DraftService draftService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public PageResponse<DraftResponse> filterDrafts(DraftFilterRequest request) {
        return draftService.filterDrafts(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public DraftResponse getDraftById(Long id) {
        return DraftMapper.entityToResponse(draftService.getDraftById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public DraftResponse createDraft(DraftCreateRequest request) {
        return DraftMapper.entityToResponse(draftService.createDraft(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public DraftResponse updateDraft(Long id, DraftUpdateRequest request) {
        return DraftMapper.entityToResponse(draftService.updateDraft(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public void deleteDraft(Long id) {
        draftService.deleteDraft(id);
    }
}
