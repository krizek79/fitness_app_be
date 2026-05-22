package sk.krizan.fitness_app_be.domain.coaching_contract.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractCreateRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractFilterRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.response.CoachingContractResponse;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.coaching_contract.mapper.CoachingContractMapper;
import sk.krizan.fitness_app_be.domain.coaching_contract.service.api.CoachingContractService;

@RestController
@RequiredArgsConstructor
public class CoachingContractController implements sk.krizan.fitness_app_be.domain.coaching_contract.rest.api.CoachingContractController {

    private final CoachingContractService coachingContractService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public PageResponse<CoachingContractResponse> filterCoachingContracts(CoachingContractFilterRequest request) {
        return coachingContractService.filterCoachingContracts(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public CoachingContractResponse getCoachingContractById(Long id) {
        return CoachingContractMapper.entityToResponse(coachingContractService.getCoachingContractById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public CoachingContractResponse createCoachingContract(CoachingContractCreateRequest request) {
        return CoachingContractMapper.entityToResponse(coachingContractService.createCoachingContract(request));
    }
}
