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
import sk.krizan.fitness_app_be.controller.request.BatchUpdateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WeekUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.SimpleListResponse;
import sk.krizan.fitness_app_be.controller.response.WeekResponse;
import sk.krizan.fitness_app_be.model.mapper.WeekMapper;
import sk.krizan.fitness_app_be.service.api.WeekService;

@RestController
@RequiredArgsConstructor
@RequestMapping("weeks")
public class WeekController {

    private final WeekService weekService;

    @PostMapping("filter")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public PageResponse<WeekResponse> filterWeeks(@Valid @RequestBody WeekFilterRequest request) {
        return weekService.filterWeeks(request);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public WeekResponse getWeekById(@PathVariable Long id) {
        return WeekMapper.entityToResponse(weekService.getWeekById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public WeekResponse createWeek(@Valid @RequestBody WeekCreateRequest request) {
        return WeekMapper.entityToResponse(weekService.createWeek(request));
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public WeekResponse updateWeek(@Valid @RequestBody WeekUpdateRequest request) {
        return WeekMapper.entityToResponse(weekService.updateWeek(request));
    }

    @PutMapping("batch-update")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public SimpleListResponse<WeekResponse> batchUpdateWeeks(@Valid @RequestBody BatchUpdateRequest<WeekUpdateRequest> request) {
        return WeekMapper.entityListToSimpleListResponse(weekService.batchUpdateWeeks(request));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Long deleteWeek(@PathVariable Long id) {
        return weekService.deleteWeek(id);
    }

    @PutMapping("{id}/trigger-completed")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public WeekResponse triggerCompleted(@PathVariable Long id) {
        return WeekMapper.entityToResponse(weekService.triggerCompleted(id));
    }
}
