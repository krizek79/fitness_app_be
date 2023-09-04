package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.controller.exception.IllegalOperationException;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.request.CreateProfileRequest;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.mapper.ProfileMapper;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.service.api.ProfileService;
import sk.krizan.fitness_app_be.service.api.UserService;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserService userService;

    private final ProfileRepository profileRepository;

    @Override
    public Profile getProfileById(Long id) {
        return profileRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Profile with id { " + id + " } does not exist."));
    }

    @Override
    public Profile getProfileByDisplayName(String displayName) {
        return profileRepository.findByDisplayName(displayName).orElseThrow(
            () -> new NotFoundException(
                "Profile with displayName { " + displayName + " } does not exist."));
    }

    @Override
    public Profile createProfile(CreateProfileRequest request) {
        User user = userService.getUserById(request.userId());
        if (user.getProfile() != null) {
            throw new IllegalOperationException(
                "User { " + user.getEmail() + " } already has an assigned profile.");
        }

        return ProfileMapper.createProfileRequestToProfile(request, user);
    }

    @Override
    public Long deleteProfile(Long id) {
        Profile profile = getProfileById(id);
        profileRepository.delete(profile);
        return profile.getId();
    }
}
