package sk.krizan.fitness_app_be.controller.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.CycleCreateRequest;
import sk.krizan.fitness_app_be.controller.request.CycleFilterRequest;
import sk.krizan.fitness_app_be.controller.request.CycleUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.CycleResponse;
import sk.krizan.fitness_app_be.model.mapper.CycleMapper;
import sk.krizan.fitness_app_be.service.api.CycleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("cycles")
public class CycleController {

    private final CycleService cycleService;

    @PostMapping("filter")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public PageResponse<CycleResponse> filterCycles(@Valid @RequestBody CycleFilterRequest request) {
        return cycleService.filterCycles(request);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public CycleResponse getCycleById(@PathVariable Long id) {
        return CycleMapper.entityToResponse(cycleService.getCycleById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public CycleResponse createCycle(@Valid @RequestBody CycleCreateRequest request) {
        return CycleMapper.entityToResponse(cycleService.createCycle(request));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public CycleResponse updateCycle(
            @PathVariable Long id,
            @Valid @RequestBody CycleUpdateRequest request
    ) {
        return CycleMapper.entityToResponse(cycleService.updateCycle(id, request));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Long deleteCycle(@PathVariable Long id) {
        return cycleService.deleteCycle(id);
    }
}
