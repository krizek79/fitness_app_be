package sk.krizan.fitness_app_be.controller.endpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.request.CycleCreateRequest;
import sk.krizan.fitness_app_be.controller.request.CycleFilterRequest;
import sk.krizan.fitness_app_be.controller.request.CycleUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.CycleResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.SecurityHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.helper.CycleHelper;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.enums.Level;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.CycleRepository;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;
import sk.krizan.fitness_app_be.service.api.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
class CycleControllerTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private CycleController cycleController;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    private Profile mockProfile;

    @BeforeEach
    void setUp() {
        User mockUser = UserHelper.createMockUser("admin@test.com", Set.of(Role.ADMIN));
        mockUser = userRepository.save(mockUser);

        mockProfile = ProfileHelper.createMockProfile("admin", mockUser);
        mockProfile = profileRepository.save(mockProfile);

        SecurityHelper.setAuthentication(mockUser);
        when(userService.getCurrentUser()).thenReturn(mockUser);
    }

    @Test
    void filterCycles() {
        User user1 = UserHelper.createMockUser("user1@test.com", Set.of(Role.USER));
        user1 = userRepository.save(user1);
        Profile profile1 = ProfileHelper.createMockProfile("profile1", user1);
        profile1 = profileRepository.save(profile1);
        User user2 = UserHelper.createMockUser("user2@test.com", Set.of(Role.USER));
        user2 = userRepository.save(user2);
        Profile profile2 = ProfileHelper.createMockProfile("profile2", user2);
        profile2 = profileRepository.save(profile2);

        List<Cycle> originalList = CycleHelper.createMockCycleListForFilter(profile1, profile2);
        originalList = cycleRepository.saveAll(originalList);

        filterCycles_byAuthorId(originalList, profile1.getId());
        filterCycles_byTraineeId(originalList, profile2.getId());
        filterCycles_byName(originalList);
        filterCycles_byLevelKey(originalList);
    }

    private void filterCycles_byAuthorId(List<Cycle> originalList, Long profileId) {
        List<Cycle> expectedList = new ArrayList<>(List.of(originalList.get(0), originalList.get(1)));
        CycleFilterRequest request = CycleHelper.createFilterRequest(0, originalList.size(), Cycle.Fields.id, Sort.Direction.DESC.name(), profileId, null, null, null);
        PageResponse<CycleResponse> response = cycleController.filterCycles(request);
        CycleHelper.assertFilter(expectedList, request, response);
    }

    private void filterCycles_byTraineeId(List<Cycle> originalList, Long profileId) {
        List<Cycle> expectedList = new ArrayList<>(List.of(originalList.get(1), originalList.get(3)));
        CycleFilterRequest request = CycleHelper.createFilterRequest(0, originalList.size(), Cycle.Fields.id, Sort.Direction.DESC.name(), null, profileId, null, null);
        PageResponse<CycleResponse> response = cycleController.filterCycles(request);
        CycleHelper.assertFilter(expectedList, request, response);
    }

    private void filterCycles_byName(List<Cycle> originalList) {
        List<Cycle> expectedList = new ArrayList<>(List.of(originalList.get(2)));
        String name = expectedList.get(0).getName().substring(0, 3);
        CycleFilterRequest request = CycleHelper.createFilterRequest(0, originalList.size(), Cycle.Fields.id, Sort.Direction.DESC.name(), null, null, name, null);
        PageResponse<CycleResponse> response = cycleController.filterCycles(request);
        CycleHelper.assertFilter(expectedList, request, response);
    }

    private void filterCycles_byLevelKey(List<Cycle> originalList) {
        List<Cycle> expectedList = new ArrayList<>(List.of(originalList.get(3)));
        String levelKey = expectedList.get(0).getLevel().getKey();
        CycleFilterRequest request = CycleHelper.createFilterRequest(0, originalList.size(), Cycle.Fields.id, Sort.Direction.DESC.name(), null, null, null, levelKey);
        PageResponse<CycleResponse> response = cycleController.filterCycles(request);
        CycleHelper.assertFilter(expectedList, request, response);
    }

    @Test
    void getCycleById() {
        Cycle cycle = CycleHelper.createMockCycle(mockProfile, mockProfile, Level.BEGINNER);
        cycle = cycleRepository.save(cycle);

        CycleResponse response = cycleController.getCycleById(cycle.getId());
        CycleHelper.assertCycleResponse_get(cycle, response);
    }

    @Test
    void createCycle() {
        CycleCreateRequest createRequest = CycleHelper.createCreateRequest();
        CycleResponse response = cycleController.createCycle(createRequest);
        CycleHelper.assertCycleResponse_create(createRequest, mockProfile, response);
    }

    @Test
    void updateCycle() {
        Cycle mockCycle = CycleHelper.createMockCycle(mockProfile, mockProfile, Level.ADVANCED);
        Cycle savedMockCycle = cycleRepository.save(mockCycle);
        CycleUpdateRequest updateRequest = CycleHelper.createUpdateRequest(Level.INTERMEDIATE);

        CycleResponse response = cycleController.updateCycle(savedMockCycle.getId(), updateRequest);

        CycleHelper.assertCycleResponse_update(updateRequest, mockProfile, response);
    }

    @Test
    void deleteCycle() {
        Cycle cycle = CycleHelper.createMockCycle(mockProfile, mockProfile, Level.INTERMEDIATE);
        cycle = cycleRepository.save(cycle);

        Long deletedCycleId = cycleController.deleteCycle(cycle.getId());
        boolean exists = cycleRepository.existsById(deletedCycleId);

        CycleHelper.assertDelete(exists, cycle, deletedCycleId);
    }
}