package sk.krizan.fitness_app_be.domain.coach_client.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.domain.coach_client.rest.dto.request.CoachClientCreateRequest;
import sk.krizan.fitness_app_be.domain.coach_client.rest.dto.request.CoachClientFilterRequest;
import sk.krizan.fitness_app_be.domain.coach_client.rest.dto.response.CoachClientResponse;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.coach_client.mapper.CoachClientMapper;
import sk.krizan.fitness_app_be.domain.coach_client.service.api.CoachClientService;

@RestController
@RequiredArgsConstructor
public class CoachClientController implements sk.krizan.fitness_app_be.domain.coach_client.rest.api.CoachClientController {

    private final CoachClientService coachClientService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public PageResponse<CoachClientResponse> filterCoachClients(CoachClientFilterRequest request) {
        return coachClientService.filterCoachClients(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public CoachClientResponse getCoachClientById(Long id) {
        return CoachClientMapper.entityToResponse(coachClientService.getCoachClientById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public CoachClientResponse createCoachClient(CoachClientCreateRequest request) {
        return CoachClientMapper.entityToResponse(coachClientService.createCoachClient(request));
    }
}
