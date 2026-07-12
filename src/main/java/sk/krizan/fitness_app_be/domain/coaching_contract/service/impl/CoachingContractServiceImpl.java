package sk.krizan.fitness_app_be.domain.coaching_contract.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.PageUtils;
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContract;
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContractAction;
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContractStatus;
import sk.krizan.fitness_app_be.domain.coaching_contract.mapper.CoachingContractMapper;
import sk.krizan.fitness_app_be.domain.coaching_contract.repository.CoachingContractRepository;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractCallerRole;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractCreateRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractFilterConnectionsRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractFilterRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractStatusUpdateRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.response.CoachingContractResponse;
import sk.krizan.fitness_app_be.domain.coaching_contract.service.api.CoachingContractService;
import sk.krizan.fitness_app_be.domain.coaching_contract.specification.CoachingContractSpecification;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.mapper.ProfileMapper;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileSimpleResponse;
import sk.krizan.fitness_app_be.domain.profile.service.api.ProfileService;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoachingContractServiceImpl implements CoachingContractService {

    private final UserService userService;
    private final ProfileService profileService;
    private final CoachingContractRepository coachingContractRepository;

    private static final List<String> supportedSortFields = List.of(
            CoachingContract.Fields.id
    );

    /**
     * Retrieves a paginated list of coaching contracts based on the provided filter criteria.
     *
     * @param request the request containing the necessary information to filter coaching contracts.
     * @return a paginated response containing the filtered coaching contracts matching the criteria specified in the request.
     */
    @Override
    public PageResponse<CoachingContractResponse> filterCoachingContracts(CoachingContractFilterRequest request) {
        User currentUser = userService.getOrCreateCurrentUser();
        boolean isUserAdmin = userService.isUserAdmin(currentUser);
        Specification<CoachingContract> specification = CoachingContractSpecification.filter(request, currentUser, isUserAdmin);
        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );
        Page<CoachingContract> page = coachingContractRepository.findAll(specification, pageable);
        List<CoachingContractResponse> responseList = page.stream()
                .map(CoachingContractMapper::entityToResponse).collect(Collectors.toList());

        return PageResponse.<CoachingContractResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .results(responseList)
                .build();
    }

    /**
     * Retrieves a coaching contract by its ID.
     *
     * @param id ID of the coaching contract to retrieve
     * @return the {@link CoachingContract} entity with the specified ID
     */
    @Override
    public CoachingContract getCoachingContractById(Long id) {
        return coachingContractRepository.getByIdOrThrow(id);
    }

    /**
     * Creates a new coaching contract based on the provided request.
     *
     * @param request the request containing the necessary information to create a coaching contract.
     * @return the created {@link CoachingContract} entity.
     */
    @Override
    public CoachingContract createCoachingContract(CoachingContractCreateRequest request) {
        Profile currentProfile = userService.getOrCreateCurrentUser().getProfile();
        if (!request.coachId().equals(currentProfile.getId())) {
            throw new ApplicationException(HttpStatus.FORBIDDEN, "Only the coach can initiate a coaching contract request.");
        }

        if (coachingContractRepository.existsByCoachIdAndClientIdAndStatusIn(request.coachId(), request.clientId(), List.of(CoachingContractStatus.PENDING, CoachingContractStatus.ACTIVE))) {
            throw new ApplicationException(HttpStatus.CONFLICT, "A pending or active coaching contract already exists between this coach and client.");
        }

        Profile coach  = profileService.getProfileById(request.coachId());
        Profile client  = profileService.getProfileById(request.clientId());

        CoachingContract coachingContract = CoachingContractMapper.createRequestToEntity(coach, client);

        return coachingContractRepository.save(coachingContract);
    }

    /**
     * Transitions a coaching contract to a new status based on the requested action.
     *
     * @param id      ID of the coaching contract to transition
     * @param request the request containing the action to apply
     * @return the updated {@link CoachingContract} entity
     * @throws ApplicationException with HttpStatus 409 if the action is not valid for the contract's current status/caller role
     */
    @Override
    public CoachingContract updateStatus(Long id, CoachingContractStatusUpdateRequest request) {
        CoachingContract coachingContract = coachingContractRepository.getByIdOrThrow(id);
        Profile currentProfile = userService.getOrCreateCurrentUser().getProfile();
        boolean isCoach = coachingContract.getCoach().getId().equals(currentProfile.getId());
        boolean isClient = coachingContract.getClient().getId().equals(currentProfile.getId());

        CoachingContractStatus currentStatus = coachingContract.getStatus();
        CoachingContractAction action = request.action();

        CoachingContractStatus newStatus = switch (action) {
            case ACCEPT -> {
                requireTransition(currentStatus == CoachingContractStatus.PENDING && isClient, action, currentStatus);
                yield CoachingContractStatus.ACTIVE;
            }
            case DENY -> {
                requireTransition(currentStatus == CoachingContractStatus.PENDING && isClient, action, currentStatus);
                yield CoachingContractStatus.DENIED;
            }
            case CANCEL -> {
                requireTransition(currentStatus == CoachingContractStatus.PENDING && isCoach, action, currentStatus);
                yield CoachingContractStatus.CANCELLED;
            }
            case TERMINATE -> {
                requireTransition(currentStatus == CoachingContractStatus.ACTIVE && (isCoach || isClient), action, currentStatus);
                yield CoachingContractStatus.TERMINATED;
            }
        };

        coachingContract.setStatus(newStatus);
        return coachingContractRepository.save(coachingContract);
    }

    private void requireTransition(boolean isValid, CoachingContractAction action, CoachingContractStatus currentStatus) {
        if (!isValid) {
            throw new ApplicationException(HttpStatus.CONFLICT,
                    "Action %s is not valid for a coaching contract with status %s, or the caller does not have the required role.".formatted(action, currentStatus));
        }
    }

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
    @Override
    public Profile resolveTrainee(Long requestTraineeId, Profile author, Profile defaultTrainee) {
        if (requestTraineeId == null) {
            return defaultTrainee;
        }

        if (requestTraineeId.equals(author.getId())) {
            return author;
        }

        return coachingContractRepository.findByCoachIdAndClientIdAndStatus(author.getId(), requestTraineeId, CoachingContractStatus.ACTIVE)
                .map(CoachingContract::getClient)
                .orElseThrow(() -> new ApplicationException(HttpStatus.FORBIDDEN, ""));
    }

    @Override
    public PageResponse<ProfileSimpleResponse> filterClients(CoachingContractFilterConnectionsRequest request) {
        return filterConnections(request, CoachingContractCallerRole.COACH);
    }

    @Override
    public PageResponse<ProfileSimpleResponse> filterCoaches(CoachingContractFilterConnectionsRequest request) {
        return filterConnections(request, CoachingContractCallerRole.CLIENT);
    }

    /**
     * Retrieves a paginated list of profiles - the current user's own profile (page 0 only) plus all
     * active "other side" profiles of the current user's coaching contracts, where the current user's
     * role in each contract is given by {@code callerRole}.
     */
    private PageResponse<ProfileSimpleResponse> filterConnections(CoachingContractFilterConnectionsRequest request, CoachingContractCallerRole callerRole) {
        User currentUser = userService.getOrCreateCurrentUser();
        Profile currentProfile = currentUser.getProfile();

        Specification<CoachingContract> specification = CoachingContractSpecification.filterConnections(request, currentProfile, callerRole);
        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );
        Page<CoachingContract> page = coachingContractRepository.findAll(specification, pageable);

        List<ProfileSimpleResponse> results = new ArrayList<>();
        if (request.page() == 0) {
            results.add(ProfileMapper.entityToSimpleResponse(currentProfile));
        }
        page.stream()
                .map(callerRole == CoachingContractCallerRole.COACH ? CoachingContract::getClient : CoachingContract::getCoach)
                .map(ProfileMapper::entityToSimpleResponse)
                .forEach(results::add);

        long totalElements = page.getTotalElements() + 1;
        int totalPages = (int) Math.ceil((double) totalElements / request.size());

        return PageResponse.<ProfileSimpleResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(totalElements)
                .totalPages(totalPages)
                .results(results)
                .build();
    }
}
