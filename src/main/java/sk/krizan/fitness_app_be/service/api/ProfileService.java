package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.model.entity.Profile;

public interface ProfileService {

    Profile getProfileById(Long id);
    Profile getProfileByDisplayName(String displayName);
    Long deleteProfile(Long id);
}
