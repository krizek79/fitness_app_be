package sk.krizan.fitness_app_be.domain.week.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import sk.krizan.fitness_app_be.common.BaseIntegrationTest;
import sk.krizan.fitness_app_be.domain.plan.entity.Plan;
import sk.krizan.fitness_app_be.domain.plan.helper.PlanHelper;
import sk.krizan.fitness_app_be.domain.plan.repository.PlanRepository;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.helper.ProfileHelper;
import sk.krizan.fitness_app_be.domain.profile.repository.ProfileRepository;
import sk.krizan.fitness_app_be.domain.user.entity.Role;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.helper.UserHelper;
import sk.krizan.fitness_app_be.domain.user.repository.UserRepository;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week.helper.WeekHelper;
import sk.krizan.fitness_app_be.domain.week.rest.dto.response.WeekDetailResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

class WeekIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    private Profile mockProfile;

    private static final String BASE_URL = "/weeks";

    @BeforeEach
    void setUp() {
        User user = userRepository.save(UserHelper.createUser(Set.of(Role.ADMIN)));
        mockProfile = profileRepository.save(ProfileHelper.createProfile(user));

        when(userService.getCurrentUser()).thenReturn(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWeekById() throws Exception {
        List<Week> weeks = new ArrayList<>(List.of(
                WeekHelper.createWeek(1)
        ));

        Plan plan = planRepository.save(PlanHelper.createPlan(mockProfile, mockProfile, weeks));

        Week week = plan.getWeeks().get(0);

        WeekDetailResponse response = performGet(
                BASE_URL + "/" + week.getId(),
                new TypeReference<>() {
                },
                HttpStatus.OK);

        WeekHelper.assertWeekDetailResponse(week, response);
    }

}