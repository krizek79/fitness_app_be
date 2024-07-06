package sk.krizan.fitness_app_be.helper;

import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;

import java.util.ArrayList;

import static sk.krizan.fitness_app_be.helper.DefaultValues.DEFAULT_VALUE;

public class ProfileHelper {

    public static Profile createMockProfile(String name, User user) {
        return Profile.builder()
                .id(1L)
                .user(user)
                .name(name)
                .bio(DEFAULT_VALUE)
                .authoredWorkouts(new ArrayList<>())
                .build();
    }
}
