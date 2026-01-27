package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.controller.request.draft.DraftCreateRequest;
import sk.krizan.fitness_app_be.controller.request.draft.DraftUpdateRequest;
import sk.krizan.fitness_app_be.controller.request.draft.DraftFilterRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.DraftResponse;
import sk.krizan.fitness_app_be.model.entity.Draft;

public interface DraftService {

    PageResponse<DraftResponse> filterDrafts(DraftFilterRequest request);

    Draft getDraftById(Long id);

    Draft createDraft(DraftCreateRequest request);

    Draft updateDraft(Long id, DraftUpdateRequest request);

    void deleteDraft(Long id);
}
