package sk.krizan.fitness_app_be.domain.coaching_contract.service.api;

import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContract;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractCreateRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractFilterConnectionsRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractFilterRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractStatusUpdateRequest;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileSimpleResponse;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.response.CoachingContractResponse;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;

public interface CoachingContractService {

    /**
     * Retrieves a paginated list of coaching contracts based on the provided filter criteria.
     *
     * @param request the request containing the necessary information to filter coaching contracts.
     * @return a paginated response containing the filtered coaching contracts matching the criteria specified in the request.
     */
    PageResponse<CoachingContractResponse> filterCoachingContracts(CoachingContractFilterRequest request);

    /**
     * Retrieves a coaching contract by its ID.
     *
     * @param id ID of the coaching contract to retrieve
     * @return the {@link CoachingContract} entity with the specified ID
     */
    CoachingContract getCoachingContractById(Long id);

    /**
     * Creates a new coaching contract based on the provided request.
     *
     * @param request the request containing the necessary information to create a coaching contract.
     * @return the created {@link CoachingContract} entity.
     */
    CoachingContract createCoachingContract(CoachingContractCreateRequest request);

    /**
     * Transitions a coaching contract to a new status based on the requested action.
     * <p>
     * Validates that the action is legal given the contract's current status and the caller's
     * role (coach vs client) on the contract, per the coaching contract state machine.
     *
     * @param id      ID of the coaching contract to transition
     * @param request the request containing the action to apply
     * @return the updated {@link CoachingContract} entity
     * @throws ApplicationException with HttpStatus 409 if the action is not valid for the contract's current status/caller role
     */
    CoachingContract updateStatus(Long id, CoachingContractStatusUpdateRequest request);

    /**
     * Resolves the correct trainee profile to use in a plan or workout.
     * <p>
     * If {@code requestTraineeId} is {@code null}, returns the provided {@code defaultTrainee}.
     * If {@code requestTraineeId} matches the author's ID, returns the author.
     * Otherwise, checks whether the author is a coach of the specified trainee;
     * throws {@link ApplicationException} with HttpStatus 403 if not.
     *
     * @param requestTraineeId ID of the trainee from the request (can be {@code null})
     * @param author profile of the plan author (typically the coach)
     * @param defaultTrainee fallback profile used if {@code requestTraineeId} is {@code null}
     * @return the resolved trainee profile
     * @throws ApplicationException with HttpStatus 403 if the author is not the trainee's coach
     */
    Profile resolveTrainee(Long requestTraineeId, Profile author, Profile defaultTrainee);

    /**
     * Retrieves a paginated list of profiles - the current user's own profile (page 0 only) plus all
     * active clients of the current user acting as coach.
     *
     * @param request the request containing the necessary information to filter clients.
     * @return a paginated response containing the current user's profile (on page 0) and their active clients.
     */
    PageResponse<ProfileSimpleResponse> filterClients(CoachingContractFilterConnectionsRequest request);

    /**
     * Retrieves a paginated list of profiles - the current user's own profile (page 0 only) plus all
     * active coaches of the current user acting as client.
     *
     * @param request the request containing the necessary information to filter coaches.
     * @return a paginated response containing the current user's profile (on page 0) and their active coaches.
     */
    PageResponse<ProfileSimpleResponse> filterCoaches(CoachingContractFilterConnectionsRequest request);

}
