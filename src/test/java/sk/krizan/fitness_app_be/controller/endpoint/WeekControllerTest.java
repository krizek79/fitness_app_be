package sk.krizan.fitness_app_be.controller.endpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.request.WeekFilterRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WeekResponse;
import sk.krizan.fitness_app_be.helper.CycleHelper;
import sk.krizan.fitness_app_be.helper.WeekHelper;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.SecurityHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Week;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Level;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.CycleRepository;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;
import sk.krizan.fitness_app_be.repository.WeekRepository;
import sk.krizan.fitness_app_be.service.api.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
class WeekControllerTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private WeekController weekController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeekRepository weekRepository;

    @MockBean
    private UserService userService;

    private Profile mockProfile;
    private Cycle mockCycle;

    @BeforeEach
    void setUp() {
        User mockUser = UserHelper.createMockUser("admin@test.com", Set.of(Role.ADMIN));
        mockUser = userRepository.save(mockUser);

        mockProfile = ProfileHelper.createMockProfile("admin", mockUser);
        mockProfile = profileRepository.save(mockProfile);

        SecurityHelper.setAuthentication(mockUser);
        when(userService.getCurrentUser()).thenReturn(mockUser);

        mockCycle = CycleHelper.createMockCycle(mockProfile, mockProfile, Level.BEGINNER);
        mockCycle = cycleRepository.save(mockCycle);
    }

    @Test
    void filterWeeks() {
        Cycle cycle1 = CycleHelper.createMockCycle(mockProfile, mockProfile, Level.BEGINNER);
        cycle1 = cycleRepository.save(cycle1);

        Week week1 = WeekHelper.createMockWeek(mockCycle, 1);
        week1 = weekRepository.save(week1);
        Week week2 = WeekHelper.createMockWeek(cycle1, 1);
        week2 = weekRepository.save(week2);
        List<Week> originalList = new ArrayList<>(List.of(week1, week2));
        List<Week> expectedList = new ArrayList<>(List.of(originalList.get(1)));

        WeekFilterRequest request = WeekHelper.createFilterRequest(0, originalList.size(), Week.Fields.id, Sort.Direction.DESC.name(), cycle1.getId());
        PageResponse<WeekResponse> response = weekController.filterWeeks(request);

        WeekHelper.assertFilter(expectedList, request, response);
    }

    @Test
    void getWeekById() {
        Week week = WeekHelper.createMockWeek(mockCycle, 1);
        week = weekRepository.save(week);

        WeekResponse response = weekController.getWeekById(week.getId());
        WeekHelper.assertWeekResponse_get(week, response);
    }

    //  TODO: https://github.com/krizek79/fitness_app_be/issues/4
    @Test
    @Disabled
    void createWeek() {
    }

    //  TODO: https://github.com/krizek79/fitness_app_be/issues/4
    @Test
    @Disabled
    void updateWeek() {
    }

    @Test
    void deleteWeek() {
        Week week = WeekHelper.createMockWeek(mockCycle, 0);
        week = weekRepository.save(week);

        Long deletedWeekId = weekController.deleteWeek(week.getId());
        boolean exists = weekRepository.existsById(deletedWeekId);

        WeekHelper.assertDelete(exists, week, deletedWeekId);
    }

    @Test
    void triggerCompleted() {
        Week week = WeekHelper.createMockWeek(mockCycle, 0);
        week = weekRepository.save(week);
        Boolean originalState = week.getCompleted();

        WeekResponse response = weekController.triggerCompleted(week.getId());
        WeekHelper.assertTriggerCompleted(originalState, response);
    }
}