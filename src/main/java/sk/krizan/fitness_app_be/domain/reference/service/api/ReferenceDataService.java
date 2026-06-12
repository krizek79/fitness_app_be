package sk.krizan.fitness_app_be.domain.reference.service.api;

import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;

import java.util.List;

public interface ReferenceDataService {

    List<String> getAvailableTypes();

    List<ReferenceDataResponse> getReferenceData(String type);

}
