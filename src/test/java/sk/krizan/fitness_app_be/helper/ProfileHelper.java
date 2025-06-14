package sk.krizan.fitness_app_be.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.WeightUnit;

import static sk.krizan.fitness_app_be.helper.DefaultValues.DEFAULT_VALUE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileHelper {

    public static Profile createMockProfile(String name, User user) {
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setName(name);
        profile.setBio(DEFAULT_VALUE);
        profile.setPreferredWeightUnit(WeightUnit.KG);
        user.setProfile(profile);
        return profile;
    }
}
