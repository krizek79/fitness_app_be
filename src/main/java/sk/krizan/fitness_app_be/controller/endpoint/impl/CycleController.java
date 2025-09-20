package sk.krizan.fitness_app_be.controller.endpoint.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.CycleCreateRequest;
import sk.krizan.fitness_app_be.controller.request.CycleFilterRequest;
import sk.krizan.fitness_app_be.controller.request.CycleUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.CycleResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.model.mapper.CycleMapper;
import sk.krizan.fitness_app_be.service.api.CycleService;

@RestController
@RequiredArgsConstructor
public class CycleController implements sk.krizan.fitness_app_be.controller.endpoint.api.CycleController {

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
    public CycleResponse createCycle(CycleCreateRequest request) {
        return CycleMapper.entityToResponse(cycleService.createCycle(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public CycleResponse updateCycle(Long id, CycleUpdateRequest request) {
        return CycleMapper.entityToResponse(cycleService.updateCycle(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public Long deleteCycle(Long id) {
        return cycleService.deleteCycle(id);
    }
}
