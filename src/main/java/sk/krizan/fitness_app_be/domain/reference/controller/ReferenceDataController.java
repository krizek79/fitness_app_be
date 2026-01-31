package sk.krizan.fitness_app_be.domain.reference.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;
import sk.krizan.fitness_app_be.domain.reference.service.api.ReferenceDataService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReferenceDataController implements sk.krizan.fitness_app_be.domain.reference.rest.api.ReferenceDataController {

    private final ReferenceDataService referenceDataService;

    @PreAuthorize(("hasAnyRole('ADMIN', 'USER')"))
    @Override
    public List<String> getAvailableReferenceTypes() {
        return referenceDataService.getAvailableTypes();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public List<ReferenceDataResponse> getReferenceData(String type) {
        return referenceDataService.getReferenceData(type);
    }
}
