package sk.krizan.fitness_app_be.service.impl;

import com.github.javafaker.Faker;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.exception.ForbiddenException;
import sk.krizan.fitness_app_be.controller.exception.IllegalOperationException;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.model.mapper.ProfileMapper;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.service.api.ProfileService;
import sk.krizan.fitness_app_be.service.api.UserService;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {


    private final UserService userService;

    private final ProfileRepository profileRepository;

    private final Faker faker = new Faker(Locale.getDefault());

    private static final String ERROR_WITH_ID_NOT_FOUND = "Profile with id { %s } does not exist.";
    private static final String ERROR_WITH_NAME_NOT_FOUND = "Profile with name { %s } does not exist.";
    private static final String ERROR_ALREADY_HAS_PROFILE = "User { %s } already has an assigned profile.";

    @Override
    public Profile getProfileById(Long id) {
        return profileRepository.findByIdAndDeletedFalse(id).orElseThrow(
            () -> new NotFoundException(ERROR_WITH_ID_NOT_FOUND.formatted(id)));
    }

    @Override
    public Profile getProfileByName(String name) {
        return profileRepository.findByNameAndDeletedFalse(name).orElseThrow(
            () -> new NotFoundException(ERROR_WITH_NAME_NOT_FOUND.formatted(name)));
    }

    @Override
    @Transactional
    public Profile createProfile(Long userId) {
        User user = userService.getUserById(userId);
        if (user.getProfile() != null) {
            throw new IllegalOperationException(ERROR_ALREADY_HAS_PROFILE.formatted(user.getEmail()));
        }

        String name = getRandomName();
        String profilePictureUrl = getRandomProfilePictureUrl();
        Profile profile = ProfileMapper.createInitialProfile(name, profilePictureUrl, user);
        user.setProfile(profile);

        return profileRepository.save(profile);
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
