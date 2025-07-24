package sk.krizan.fitness_app_be.controller.endpoint.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.CoachClientCreateRequest;
import sk.krizan.fitness_app_be.controller.request.CoachClientFilterRequest;
import sk.krizan.fitness_app_be.controller.response.CoachClientResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.model.mapper.CoachClientMapper;
import sk.krizan.fitness_app_be.service.api.CoachClientService;

@RestController
@RequiredArgsConstructor
public class CoachClientController implements sk.krizan.fitness_app_be.controller.endpoint.api.CoachClientController {

    private final CoachClientService coachClientService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public PageResponse<CoachClientResponse> filterCoachClients(CoachClientFilterRequest request) {
        return coachClientService.filterCoachClients(request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public CoachClientResponse getCoachClientById(Long id) {
        return CoachClientMapper.entityToResponse(coachClientService.getCoachClientById(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public CoachClientResponse createCoachClient(CoachClientCreateRequest request) {
        return CoachClientMapper.entityToResponse(coachClientService.createCoachClient(request));
    }
}
