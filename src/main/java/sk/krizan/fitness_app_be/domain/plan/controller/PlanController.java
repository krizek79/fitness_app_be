package sk.krizan.fitness_app_be.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.request.PlanFilterRequest;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.request.PlanInputRequest;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.response.PlanResponse;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.plan.mapper.PlanMapper;
import sk.krizan.fitness_app_be.domain.plan.service.api.PlanService;

@RestController
@RequiredArgsConstructor
public class PlanController implements sk.krizan.fitness_app_be.domain.plan.rest.api.PlanController {

    private final PlanService planService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public PageResponse<PlanResponse> filterPlans(PlanFilterRequest request) {
        return planService.filterPlans(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public PlanResponse getPlanById(Long id) {
        return PlanMapper.entityToResponse(planService.getPlanById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public PlanResponse createPlan(PlanInputRequest request) {
        return PlanMapper.entityToResponse(planService.createUpdatePlan(null, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public PlanResponse updatePlan(Long id, PlanInputRequest request) {
        return PlanMapper.entityToResponse(planService.createUpdatePlan(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public void deletePlan(Long id) {
        planService.deletePlan(id);
    }
}
