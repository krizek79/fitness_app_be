package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.controller.exception.ForbiddenException;
import sk.krizan.fitness_app_be.controller.request.CoachClientCreateRequest;
import sk.krizan.fitness_app_be.controller.request.CoachClientFilterRequest;
import sk.krizan.fitness_app_be.controller.response.CoachClientResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.model.entity.CoachClient;
import sk.krizan.fitness_app_be.model.entity.Profile;

public interface CoachClientService {

    PageResponse<CoachClientResponse> filterCoachClients(CoachClientFilterRequest request);

    CoachClient getCoachClientById(Long id);

    CoachClient createCoachClient(CoachClientCreateRequest request);

    /**
     * Resolves the correct trainee profile to use in a cycle or workout.
     * <p>
     * If {@code requestTraineeId} is {@code null}, returns the provided {@code defaultTrainee}.
     * If {@code requestTraineeId} matches the author's ID, returns the author.
     * Otherwise, checks whether the author is a coach of the specified trainee;
     * throws {@link ForbiddenException} if not.
     *
     * @param requestTraineeId ID of the trainee from the request (can be {@code null})
     * @param author profile of the cycle author (typically the coach)
     * @param defaultTrainee fallback profile used if {@code requestTraineeId} is {@code null}
     * @return the resolved trainee profile
     * @throws ForbiddenException if the author is not the trainee's coach
     */
    Profile resolveTrainee(Long requestTraineeId, Profile author, Profile defaultTrainee);
}
