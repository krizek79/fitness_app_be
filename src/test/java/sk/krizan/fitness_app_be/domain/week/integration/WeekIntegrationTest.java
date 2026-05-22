package sk.krizan.fitness_app_be.domain.week.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import sk.krizan.fitness_app_be.common.BaseIntegrationTest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.FilterAssertionUtils;
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
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekFilterRequest;
import sk.krizan.fitness_app_be.domain.week.rest.dto.response.WeekResponse;

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
    void filterWeeks() throws Exception {
        List<Week> weeks1 = new ArrayList<>(List.of(
                WeekHelper.createWeek(1),
                WeekHelper.createWeek(2)
        ));

        planRepository.save(PlanHelper.createPlan(mockProfile, mockProfile, weeks1, new ArrayList<>()));

        List<Week> weeks2 = new ArrayList<>(List.of(
                WeekHelper.createWeek(1),
                WeekHelper.createWeek(2)
        ));

        Plan plan2 = planRepository.save(PlanHelper.createPlan(mockProfile, mockProfile, weeks2, new ArrayList<>()));

        List<Week> expectedList = new ArrayList<>(plan2.getWeeks());

        WeekFilterRequest request = WeekHelper.createFilterRequest(0, expectedList.size(), Week.Fields.id, Sort.Direction.DESC.name(), plan2.getId());

        PageResponse<WeekResponse> response = performPost(
                BASE_URL + "/filter",
                request,
                new TypeReference<>() {
                },
                HttpStatus.OK);

        FilterAssertionUtils.assertFilterResults(
                expectedList,
                response,
                Week::getId,
                WeekResponse::id,
                WeekHelper::assertWeekResponse);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWeekById() throws Exception {
        List<Week> weeks = new ArrayList<>(List.of(
                WeekHelper.createWeek(1)
        ));

        Plan plan = planRepository.save(PlanHelper.createPlan(mockProfile, mockProfile, weeks, new ArrayList<>()));

        Week week = plan.getWeeks().get(0);

        WeekResponse response = performGet(
                BASE_URL + "/" + week.getId(),
                new TypeReference<>() {
                },
                HttpStatus.OK);

        WeekHelper.assertWeekResponse(week, response);
    }

}