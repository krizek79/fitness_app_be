package sk.krizan.fitness_app_be.service.impl;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.exception.ForbiddenException;
import sk.krizan.fitness_app_be.controller.exception.IllegalOperationException;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.request.ProfileFilterRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.ProfileResponse;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.model.mapper.ProfileMapper;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.service.api.ProfileService;
import sk.krizan.fitness_app_be.service.api.UserService;
import sk.krizan.fitness_app_be.specification.ProfileSpecification;
import sk.krizan.fitness_app_be.util.PageUtils;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserService userService;

    private final ProfileRepository profileRepository;

    private final Faker faker = new Faker(Locale.getDefault());

    private static final String ERROR_WITH_ID_NOT_FOUND = "Profile with id { %s } does not exist.";
    private static final String ERROR_ALREADY_HAS_PROFILE = "User { %s } already has an assigned profile.";

    private static final List<String> supportedSortFields = List.of(
            Profile.Fields.id,
            Profile.Fields.name
    );

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProfileResponse> filterProfiles(ProfileFilterRequest request) {
        Specification<Profile> specification = ProfileSpecification.filter(request);
        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );
        Page<Profile> page = profileRepository.findAll(specification, pageable);
        List<ProfileResponse> responseList = page.stream()
                .map(ProfileMapper::entityToResponse).collect(Collectors.toList());

        return PageResponse.<ProfileResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .results(responseList)
                .build();
    }

    @Override
    public Profile getProfileById(Long id) {
        return profileRepository.findByIdAndDeletedFalse(id).orElseThrow(
            () -> new NotFoundException(ERROR_WITH_ID_NOT_FOUND.formatted(id)));
    }

    @Override
    @Transactional
    public void createProfile(Long userId) {
        User user = userService.getUserById(userId);
        if (user.getProfile() != null) {
            throw new IllegalOperationException(ERROR_ALREADY_HAS_PROFILE.formatted(user.getEmail()));
        }

        String name = getRandomName();
        String profilePictureUrl = getRandomProfilePictureUrl();
        Profile profile = ProfileMapper.createInitialProfile(name, profilePictureUrl, user);
        user.setProfile(profile);

        profileRepository.save(profile);
    }

    @Override
    public Long deleteProfile(Long id) {
        User currentUser = userService.getCurrentUser();
        Profile profile = getProfileById(id);

        if (profile.getUser() != currentUser && !currentUser.getRoleSet().contains(Role.ADMIN)) {
            throw new ForbiddenException();
        }

        profile.setDeleted(true);
        profileRepository.save(profile);
        return profile.getId();
    }

    private String getRandomName() {
        String name;
        do {
            name = faker.name().username();
        } while (profileRepository.existsByNameAndDeletedFalse(name));

        return name;
    }

    private String getRandomProfilePictureUrl() {
        return "";
    }
}
