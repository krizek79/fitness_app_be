package sk.krizan.fitness_app_be.controller.endpoint;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.request.WorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutResponse;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.SecurityHelper;
import sk.krizan.fitness_app_be.helper.TagHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.helper.WorkoutHelper;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.Tag;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.repository.TagRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;
import sk.krizan.fitness_app_be.repository.WorkoutRepository;
import sk.krizan.fitness_app_be.service.api.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static sk.krizan.fitness_app_be.helper.DefaultValues.DEFAULT_VALUE;

@Transactional
@SpringBootTest
class WorkoutControllerTest {

    @Autowired
    private WorkoutController workoutController;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @MockBean
    private UserService userService;

    private Profile mockProfile;

    @BeforeEach
    void setUp() {
        User mockUser = UserHelper.createMockUser("admin@test.com", Set.of(Role.ADMIN));
        mockUser = userRepository.save(mockUser);

        mockProfile = ProfileHelper.createMockProfile("admin", mockUser);
        mockProfile = profileRepository.save(mockProfile);
        mockUser.setProfile(mockProfile);

        SecurityHelper.setAuthentication(mockUser);
        when(userService.getCurrentUser()).thenReturn(mockUser);
    }

    @Test
    void filterWorkouts() {
        List<Profile> profileList = new ArrayList<>();
        List<List<WorkoutExercise>> workoutExerciseList = new ArrayList<>();
        List<Set<Tag>> tagSetList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            User user = UserHelper.createMockUser("user" + (i + 1) + "@test.com", Set.of(Role.USER));
            user = userRepository.save(user);
            Profile profile = ProfileHelper.createMockProfile("profile" + (i + 1), user);
            profile = profileRepository.save(profile);
            user.setProfile(profile);
            profileList.add(profile);

            workoutExerciseList.add(new ArrayList<>());

            Tag tag1 = tagRepository.save(TagHelper.createMockTag());
            Tag tag2 = tagRepository.save(TagHelper.createMockTag());
            tagSetList.add(Set.of(tag1, tag2));
        }

        List<Workout> originalWorkoutList = new ArrayList<>();
        try {
            originalWorkoutList = WorkoutHelper.createMockWorkoutList(profileList, workoutExerciseList, tagSetList);
        } catch (Exception e) {
            Assertions.fail("An error occurred while creating sample data.");
        }

        filterWorkouts_ByName(originalWorkoutList);
        filterWorkouts_ByTagNameList(originalWorkoutList);
        filterWorkouts_ByAuthorId(originalWorkoutList);
    }

    private void filterWorkouts_ByAuthorId(List<Workout> originalWorkoutList) {
        Workout expectedWorkout = originalWorkoutList.get(1);

        WorkoutFilterRequest request = WorkoutHelper.createFilterRequest(0, originalWorkoutList.size(), Workout.Fields.id, Sort.Direction.DESC.name(), expectedWorkout.getAuthor().getId(), null, null);
        PageResponse<WorkoutResponse> response = workoutController.filterWorkouts(request);

        WorkoutHelper.assertFilter(response, expectedWorkout);
    }

    private void filterWorkouts_ByTagNameList(List<Workout> originalWorkoutList) {
        Workout expectedWorkout = originalWorkoutList.get(2);

        WorkoutFilterRequest request = WorkoutHelper.createFilterRequest(0, originalWorkoutList.size(), Workout.Fields.id, Sort.Direction.DESC.name(), null, expectedWorkout.getTagSet().stream().map(Tag::getId).collect(Collectors.toList()), null);
        PageResponse<WorkoutResponse> response = workoutController.filterWorkouts(request);

        WorkoutHelper.assertFilter(response, expectedWorkout);
    }

    private void filterWorkouts_ByName(List<Workout> originalWorkoutList) {
        Workout expectedWorkout = originalWorkoutList.get(0);

        WorkoutFilterRequest request = WorkoutHelper.createFilterRequest(0, originalWorkoutList.size(), Workout.Fields.id, Sort.Direction.DESC.name(), null, null, expectedWorkout.getName().substring(0, 5));
        PageResponse<WorkoutResponse> response = workoutController.filterWorkouts(request);

        WorkoutHelper.assertFilter(response, expectedWorkout);
    }

    @Test
    void getWorkoutById() {
        Workout workout = WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE);
        workout = workoutRepository.save(workout);

        WorkoutResponse response = workoutController.getWorkoutById(workout.getId());
        WorkoutHelper.assertWorkoutResponse_get(workout, response);
    }

    @Test
    void createWorkout() {
        WorkoutCreateRequest createRequest = WorkoutHelper.createCreateRequest();
        WorkoutResponse response = workoutController.createWorkout(createRequest);
        WorkoutHelper.assertWorkoutResponse_create(createRequest, mockProfile, response);
    }

    @Test
    void updateWorkout() {
        Workout mockWorkout = WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE);
        Workout savedMockWorkout = workoutRepository.save(mockWorkout);
        WorkoutUpdateRequest updateRequest = WorkoutHelper.createUpdateRequest();

        WorkoutResponse response = workoutController.updateWorkout(savedMockWorkout.getId(), updateRequest);

        WorkoutHelper.assertWorkoutResponse_update(updateRequest, mockProfile, response);
    }

    @Test
    void deleteWorkout() {
        Workout mockWorkout = WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE);
        Workout savedMockWorkout = workoutRepository.save(mockWorkout);

        Long deletedWorkoutId = workoutController.deleteWorkout(savedMockWorkout.getId());
        boolean exists = workoutRepository.findById(deletedWorkoutId).isPresent();

        WorkoutHelper.assertDelete(exists, savedMockWorkout, deletedWorkoutId);
    }
}