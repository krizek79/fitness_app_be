package sk.krizan.fitness_app_be.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.plan.mapper.PlanMapper;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.request.PlanFilterRequest;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.request.PlanInputRequest;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.response.PlanDetailResponse;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.response.PlanSimpleResponse;
import sk.krizan.fitness_app_be.domain.plan.service.api.PlanService;

@RestController
@RequiredArgsConstructor
public class PlanController implements sk.krizan.fitness_app_be.domain.plan.rest.api.PlanController {

    private final PlanService planService;

    @Override
    public PageResponse<PlanSimpleResponse> filterPlans(PlanFilterRequest request) {
        return planService.filterPlans(request);
    }

    @Override
    @PreAuthorize("hasPermission(#id, 'PLAN', 'READ')")
    public PlanDetailResponse getPlanById(Long id) {
        return PlanMapper.entityToDetailResponse(planService.getPlanById(id));
    }

    @Override
    public PlanDetailResponse createPlan(PlanInputRequest request) {
        return PlanMapper.entityToDetailResponse(planService.createUpdatePlan(null, request));
    }

    @Override
    @PreAuthorize("hasPermission(#id, 'PLAN', 'UPDATE')")
    public PlanDetailResponse updatePlan(Long id, PlanInputRequest request) {
        return PlanMapper.entityToDetailResponse(planService.createUpdatePlan(id, request));
    }

    @Override
    @PreAuthorize("hasPermission(#id, 'PLAN', 'DELETE')")
    public void deletePlan(Long id) {
        planService.deletePlan(id);
    }

}
