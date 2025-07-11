package sk.krizan.fitness_app_be.controller.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.CoachClientCreateRequest;
import sk.krizan.fitness_app_be.controller.request.CoachClientFilterRequest;
import sk.krizan.fitness_app_be.controller.response.CoachClientResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.model.mapper.CoachClientMapper;
import sk.krizan.fitness_app_be.service.api.CoachClientService;

@RestController
@RequiredArgsConstructor
@RequestMapping("coach-clients")
public class CoachClientController {

    private final CoachClientService coachClientService;

    @PostMapping("filter")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public PageResponse<CoachClientResponse> filterCoachClients(@Valid @RequestBody CoachClientFilterRequest request) {
        return coachClientService.filterCoachClients(request);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public CoachClientResponse getCoachClientById(@PathVariable Long id) {
        return CoachClientMapper.entityToResponse(coachClientService.getCoachClientById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public CoachClientResponse createCoachClient(@Valid @RequestBody CoachClientCreateRequest request) {
        return CoachClientMapper.entityToResponse(coachClientService.createCoachClient(request));
    }
}
