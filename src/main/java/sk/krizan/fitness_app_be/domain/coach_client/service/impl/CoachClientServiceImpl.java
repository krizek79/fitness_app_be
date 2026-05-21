package sk.krizan.fitness_app_be.domain.coach_client.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.PageUtils;
import sk.krizan.fitness_app_be.domain.coach_client.entity.CoachClient;
import sk.krizan.fitness_app_be.domain.coach_client.mapper.CoachClientMapper;
import sk.krizan.fitness_app_be.domain.coach_client.repository.CoachClientRepository;
import sk.krizan.fitness_app_be.domain.coach_client.rest.dto.request.CoachClientCreateRequest;
import sk.krizan.fitness_app_be.domain.coach_client.rest.dto.request.CoachClientFilterRequest;
import sk.krizan.fitness_app_be.domain.coach_client.rest.dto.response.CoachClientResponse;
import sk.krizan.fitness_app_be.domain.coach_client.service.api.CoachClientService;
import sk.krizan.fitness_app_be.domain.coach_client.specification.CoachClientSpecification;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.service.api.ProfileService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoachClientServiceImpl implements CoachClientService {

    private final ProfileService profileService;
    private final CoachClientRepository coachClientRepository;

    private static final List<String> supportedSortFields = List.of(
            CoachClient.Fields.id
    );

    /**
     * Retrieves a paginated list of coach-client relationships based on the provided filter criteria.
     *
     * @param request the request containing the necessary information to filter coach-client relationships.
     * @return a paginated response containing the filtered coach-client relationships matching the criteria specified in the request.
     */
    @Override
    public PageResponse<CoachClientResponse> filterCoachClients(CoachClientFilterRequest request) {
        Specification<CoachClient> specification = CoachClientSpecification.filter(request);
        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );
        Page<CoachClient> page = coachClientRepository.findAll(specification, pageable);
        List<CoachClientResponse> responseList = page.stream()
                .map(CoachClientMapper::entityToResponse).collect(Collectors.toList());

        return PageResponse.<CoachClientResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .results(responseList)
                .build();
    }

    /**
     * Retrieves a coach-client relationship by its ID.
     *
     * @param id ID of the coach-client relationship to retrieve
     * @return the {@link CoachClient} entity with the specified ID
     */
    @Override
    public CoachClient getCoachClientById(Long id) {
        return coachClientRepository.getByIdOrThrow(id);
    }

    /**
     * Creates a new coach-client relationship based on the provided request.
     *
     * @param request the request containing the necessary information to create a coach-client relationship.
     * @return the created {@link CoachClient} entity.
     */
    @Override
    public CoachClient createCoachClient(CoachClientCreateRequest request) {
        Profile coach  = profileService.getProfileById(request.coachId());
        Profile client  = profileService.getProfileById(request.clientId());

        CoachClient coachClient = CoachClientMapper.createRequestToEntity(coach, client);

        return coachClientRepository.save(coachClient);
    }

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
    @Override
    public Profile resolveTrainee(Long requestTraineeId, Profile author, Profile defaultTrainee) {
        if (requestTraineeId == null) {
            return defaultTrainee;
        }

        if (requestTraineeId.equals(author.getId())) {
            return author;
        }

        return coachClientRepository.findByCoachIdAndClientIdAndActiveTrue(author.getId(), requestTraineeId)
                .map(CoachClient::getClient)
                .orElseThrow(() -> new ApplicationException(HttpStatus.FORBIDDEN, ""));
    }
}
