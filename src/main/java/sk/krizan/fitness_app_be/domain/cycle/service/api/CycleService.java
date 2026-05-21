package sk.krizan.fitness_app_be.domain.cycle.service.api;

import sk.krizan.fitness_app_be.domain.cycle.rest.dto.request.CycleFilterRequest;
import sk.krizan.fitness_app_be.domain.cycle.rest.dto.request.CycleInputRequest;
import sk.krizan.fitness_app_be.domain.cycle.rest.dto.response.CycleResponse;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.cycle.entity.Cycle;

public interface CycleService {

    PageResponse<CycleResponse> filterCycles(CycleFilterRequest request);

    Cycle getCycleById(Long id);

    Cycle createUpdateCycle(Long id, CycleInputRequest request);

    void deleteCycle(Long id);
}
