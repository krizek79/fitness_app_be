package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.controller.request.CycleCreateRequest;
import sk.krizan.fitness_app_be.controller.request.CycleFilterRequest;
import sk.krizan.fitness_app_be.controller.request.CycleUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.CycleResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.model.entity.Cycle;

public interface CycleService {

    PageResponse<CycleResponse> filterCycles(CycleFilterRequest request);

    Cycle getCycleById(Long id);

    Cycle createCycle(CycleCreateRequest request);

    Cycle updateCycle(Long id, CycleUpdateRequest request);

    Long deleteCycle(Long id);
}
