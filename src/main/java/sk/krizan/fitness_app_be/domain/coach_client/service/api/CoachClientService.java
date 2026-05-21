package sk.krizan.fitness_app_be.domain.coach_client.service.api;

import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.coach_client.rest.dto.request.CoachClientCreateRequest;
import sk.krizan.fitness_app_be.domain.coach_client.rest.dto.request.CoachClientFilterRequest;
import sk.krizan.fitness_app_be.domain.coach_client.rest.dto.response.CoachClientResponse;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.coach_client.entity.CoachClient;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;

public interface CoachClientService {

    /**
     * Retrieves a paginated list of coach-client relationships based on the provided filter criteria.
     *
     * @param request the request containing the necessary information to filter coach-client relationships.
     * @return a paginated response containing the filtered coach-client relationships matching the criteria specified in the request.
     */
    PageResponse<CoachClientResponse> filterCoachClients(CoachClientFilterRequest request);

    /**
     * Retrieves a coach-client relationship by its ID.
     *
     * @param id ID of the coach-client relationship to retrieve
     * @return the {@link CoachClient} entity with the specified ID
     */
    CoachClient getCoachClientById(Long id);

    /**
     * Creates a new coach-client relationship based on the provided request.
     *
     * @param request the request containing the necessary information to create a coach-client relationship.
     * @return the created {@link CoachClient} entity.
     */
    CoachClient createCoachClient(CoachClientCreateRequest request);

    /**
     * Resolves the correct trainee profile to use in a cycle or workout.
     * <p>
     * If {@code requestTraineeId} is {@code null}, returns the provided {@code defaultTrainee}.
     * If {@code requestTraineeId} matches the author's ID, returns the author.
     * Otherwise, checks whether the author is a coach of the specified trainee;
     * throws {@link ApplicationException} with HttpStatus 403 if not.
     *
     * @param requestTraineeId ID of the trainee from the request (can be {@code null})
     * @param author profile of the cycle author (typically the coach)
     * @param defaultTrainee fallback profile used if {@code requestTraineeId} is {@code null}
     * @return the resolved trainee profile
     * @throws ApplicationException with HttpStatus 403 if the author is not the trainee's coach
     */
    Profile resolveTrainee(Long requestTraineeId, Profile author, Profile defaultTrainee);

}
