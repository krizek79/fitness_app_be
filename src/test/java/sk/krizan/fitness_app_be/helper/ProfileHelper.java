package sk.krizan.fitness_app_be.helper;

import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;

import static sk.krizan.fitness_app_be.helper.DefaultValues.DEFAULT_VALUE;

public class ProfileHelper {

    public static Profile createMockProfile(String name, User user) {
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setName(name);
        profile.setBio(DEFAULT_VALUE);
        user.setProfile(profile);
        return profile;
    }
}
