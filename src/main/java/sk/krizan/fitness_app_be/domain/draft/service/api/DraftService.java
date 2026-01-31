package sk.krizan.fitness_app_be.domain.draft.service.api;

import sk.krizan.fitness_app_be.domain.draft.rest.dto.request.DraftCreateRequest;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.request.DraftUpdateRequest;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.request.DraftFilterRequest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.response.DraftResponse;
import sk.krizan.fitness_app_be.domain.draft.entity.Draft;

public interface DraftService {

    PageResponse<DraftResponse> filterDrafts(DraftFilterRequest request);

    Draft getDraftById(Long id);

    Draft createDraft(DraftCreateRequest request);

    Draft updateDraft(Long id, DraftUpdateRequest request);

    void deleteDraft(Long id);
}
