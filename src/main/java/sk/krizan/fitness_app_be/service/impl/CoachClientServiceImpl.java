package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.controller.exception.ForbiddenException;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.request.CoachClientCreateRequest;
import sk.krizan.fitness_app_be.controller.request.CoachClientFilterRequest;
import sk.krizan.fitness_app_be.controller.response.CoachClientResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.model.entity.CoachClient;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.model.mapper.CoachClientMapper;
import sk.krizan.fitness_app_be.repository.CoachClientRepository;
import sk.krizan.fitness_app_be.service.api.CoachClientService;
import sk.krizan.fitness_app_be.service.api.ProfileService;
import sk.krizan.fitness_app_be.service.api.UserService;
import sk.krizan.fitness_app_be.specification.CoachClientSpecification;
import sk.krizan.fitness_app_be.util.PageUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoachClientServiceImpl implements CoachClientService {

    private final UserService userService;
    private final ProfileService profileService;
    private final CoachClientRepository coachClientRepository;

    private final static String ERROR_COACH_CLIENT_NOT_FOUND = "CoachClient with id { %s } does not exist.";

    private static final List<String> supportedSortFields = List.of(
            CoachClient.Fields.id
    );

    @Override
    public PageResponse<CoachClientResponse> filterCoachClients(CoachClientFilterRequest request) {
        checkAuthorization_filter(request);

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

    @Override
    public CoachClient getCoachClientById(Long id) {
        CoachClient coachClient = coachClientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ERROR_COACH_CLIENT_NOT_FOUND.formatted(id)));

        checkAuthorization_get(coachClient);

        return coachClient;
    }

    @Override
    public CoachClient createCoachClient(CoachClientCreateRequest request) {
        Profile coach  = profileService.getProfileById(request.coachId());
        Profile client  = profileService.getProfileById(request.clientId());

        checkAuthorization_create(coach);

        return CoachClientMapper.createRequestToEntity(coach, client);
    }

    @Override
    public Profile resolveTrainee(Long requestTraineeId, Profile author, Profile defaultTrainee) {
        if (requestTraineeId != null) {
            if (requestTraineeId.equals(author.getId())) {
                return author;
            }
            CoachClient coachClient = coachClientRepository.findByCoachIdAndClientId(author.getId(), requestTraineeId)
                    .orElseThrow(ForbiddenException::new);
            return coachClient.getClient();
        }
        return defaultTrainee;
    }

    private void checkAuthorization_filter(CoachClientFilterRequest request) {
        User currentUser = userService.getCurrentUser();
        Long currentProfileId = currentUser.getProfile().getId();

        boolean isAdmin = currentUser.getRoleSet().contains(Role.ADMIN);
        boolean isCoachFilterUsed = request.coachId() != null;
        boolean isClientFilterUsed = request.clientId() != null;

        boolean isFilteringAsCoach = isCoachFilterUsed && currentProfileId.equals(request.coachId());
        boolean isFilteringAsClient = isClientFilterUsed && currentProfileId.equals(request.clientId());
        boolean isUnfiltered = !isCoachFilterUsed && !isClientFilterUsed;

        boolean isAuthorized = isAdmin || isFilteringAsCoach || isFilteringAsClient;

        if (isUnfiltered && !isAdmin) {
            throw new ForbiddenException();
        }

        if (!isAuthorized) {
            throw new ForbiddenException();
        }
    }

    private void checkAuthorization_get(CoachClient coachClient) {
        User currentUser = userService.getCurrentUser();
        User coach = coachClient.getCoach().getUser();
        User client = coachClient.getClient().getUser();

        boolean isCurrentUserAdmin = currentUser.getRoleSet().contains(Role.ADMIN);
        boolean isCurrentUserCoachOfTheClient = currentUser == coach;
        boolean isCurrentUserClient = currentUser == client;

        if (!isCurrentUserCoachOfTheClient && !isCurrentUserClient && !isCurrentUserAdmin) {
            throw new ForbiddenException();
        }
    }

    //  For now, only coach will be able to create coach-client relationship
    private void checkAuthorization_create(Profile coach) {
        User currentUser = userService.getCurrentUser();

        boolean isCurrentUserAdmin = currentUser.getRoleSet().contains(Role.ADMIN);

        if (currentUser != coach.getUser() && !isCurrentUserAdmin) {
            throw new ForbiddenException();
        }
    }
}
