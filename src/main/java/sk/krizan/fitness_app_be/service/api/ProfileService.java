package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.model.entity.Profile;

public interface ProfileService {

    Profile getProfileById(Long id);

    Profile getProfileByName(String name);

    Profile createProfile(Long userId);

    Long deleteProfile(Long id);
}
