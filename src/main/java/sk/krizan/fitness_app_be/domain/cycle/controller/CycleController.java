package sk.krizan.fitness_app_be.domain.cycle.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.domain.cycle.rest.dto.request.CycleFilterRequest;
import sk.krizan.fitness_app_be.domain.cycle.rest.dto.request.CycleInputRequest;
import sk.krizan.fitness_app_be.domain.cycle.rest.dto.response.CycleResponse;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.cycle.mapper.CycleMapper;
import sk.krizan.fitness_app_be.domain.cycle.service.api.CycleService;

@RestController
@RequiredArgsConstructor
public class CycleController implements sk.krizan.fitness_app_be.domain.cycle.rest.api.CycleController {

    private final CycleService cycleService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public PageResponse<CycleResponse> filterCycles(CycleFilterRequest request) {
        return cycleService.filterCycles(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public CycleResponse getCycleById(Long id) {
        return CycleMapper.entityToResponse(cycleService.getCycleById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public CycleResponse createCycle(CycleInputRequest request) {
        return CycleMapper.entityToResponse(cycleService.createUpdateCycle(null, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public CycleResponse updateCycle(Long id, CycleInputRequest request) {
        return CycleMapper.entityToResponse(cycleService.createUpdateCycle(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public void deleteCycle(Long id) {
        cycleService.deleteCycle(id);
    }
}
