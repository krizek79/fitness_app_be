package sk.krizan.fitness_app_be.domain.plan.service.api;

import sk.krizan.fitness_app_be.domain.plan.rest.dto.request.PlanFilterRequest;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.request.PlanInputRequest;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.response.PlanResponse;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.plan.entity.Plan;

public interface PlanService {

    PageResponse<PlanResponse> filterPlans(PlanFilterRequest request);

    Plan getPlanById(Long id);

    Plan createUpdatePlan(Long id, PlanInputRequest request);

    void deletePlan(Long id);

}
