package sk.krizan.fitness_app_be.controller.endpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.response.EnumResponse;
import sk.krizan.fitness_app_be.helper.EnumHelper;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.SecurityHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Level;
import sk.krizan.fitness_app_be.model.enums.MuscleGroup;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;

import java.util.List;
import java.util.Set;

@Transactional
@SpringBootTest
class EnumControllerTest {

    @Autowired
    private EnumController enumController;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User mockUser = UserHelper.createMockUser("admin@test.com", Set.of(Role.ADMIN));
        mockUser = userRepository.save(mockUser);

        Profile mockProfile = ProfileHelper.createMockProfile("admin", mockUser);
        profileRepository.save(mockProfile);

        SecurityHelper.setAuthentication(mockUser);
    }

    @Test
    void getWorkoutLevels() {
        List<EnumResponse> levels = enumController.getWorkoutLevels();
        EnumHelper.assertEnumResponsesMatch(Level.class, levels);
    }

    @Test
    void getMuscleGroups() {
        List<EnumResponse> muscleGroups = enumController.getMuscleGroups();
        EnumHelper.assertEnumResponsesMatch(MuscleGroup.class, muscleGroups);
    }
}
