package sk.krizan.fitness_app_be.domain.draft.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.draft.mapper.DraftMapper;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.request.DraftCreateRequest;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.request.DraftFilterRequest;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.request.DraftUpdateRequest;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.response.DraftResponse;
import sk.krizan.fitness_app_be.domain.draft.service.api.DraftService;

@RestController
@RequiredArgsConstructor
public class DraftController implements sk.krizan.fitness_app_be.domain.draft.rest.api.DraftController {

    private final DraftService draftService;

    @Override
    public PageResponse<DraftResponse> filterDrafts(DraftFilterRequest request) {
        return draftService.filterDrafts(request);
    }

    @PreAuthorize("hasPermission(#id, 'DRAFT', 'READ')")
    @Override
    public DraftResponse getDraftById(Long id) {
        return DraftMapper.entityToResponse(draftService.getDraftById(id));
    }

    @Override
    public DraftResponse createDraft(DraftCreateRequest request) {
        return DraftMapper.entityToResponse(draftService.createDraft(request));
    }

    @PreAuthorize("hasPermission(#id, 'DRAFT', 'UPDATE')")
    @Override
    public DraftResponse updateDraft(Long id, DraftUpdateRequest request) {
        return DraftMapper.entityToResponse(draftService.updateDraft(id, request));
    }

    @PreAuthorize("hasPermission(#id, 'DRAFT', 'DELETE')")
    @Override
    public void deleteDraft(Long id) {
        draftService.deleteDraft(id);
    }

}
