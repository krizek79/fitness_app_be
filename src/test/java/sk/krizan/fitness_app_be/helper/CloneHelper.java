package sk.krizan.fitness_app_be.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.response.CycleResponse;
import sk.krizan.fitness_app_be.controller.response.WeekWorkoutResponse;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.Week;
import sk.krizan.fitness_app_be.model.entity.WeekWorkout;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.model.entity.WorkoutExerciseSet;

import java.util.Comparator;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CloneHelper {

    public static void assertCycleResponse(Cycle cycle, CycleResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertNotEquals(cycle.getId(), response.id());
        Assertions.assertEquals(cycle.getName(), response.name());
        Assertions.assertEquals(cycle.getDescription(), response.description());
        EnumHelper.assertEnumResponse(cycle.getLevel().getKey(), response.levelResponse());
        Assertions.assertEquals(cycle.getAuthor().getId(), response.authorId());
        Assertions.assertEquals(cycle.getAuthor().getName(), response.authorName());
        Assertions.assertEquals(cycle.getTrainee().getId(), response.traineeId());
        Assertions.assertEquals(cycle.getTrainee().getName(), response.traineeName());
    }

    public static void assertCycle(Cycle originalCycle, Cycle clonedCycle, Profile profile) {
        Assertions.assertNotEquals(originalCycle.getId(), clonedCycle.getId());
        Assertions.assertEquals(originalCycle.getName(), clonedCycle.getName());
        Assertions.assertEquals(originalCycle.getDescription(), clonedCycle.getDescription());
        Assertions.assertEquals(originalCycle.getLevel(), clonedCycle.getLevel());
        Assertions.assertEquals(originalCycle.getAuthor(), clonedCycle.getAuthor());
        Assertions.assertEquals(profile, clonedCycle.getTrainee());
        Assertions.assertNotNull(clonedCycle.getGoalList());
        Assertions.assertTrue(clonedCycle.getGoalList().isEmpty());
        Assertions.assertNotNull(clonedCycle.getWeekList());
        Assertions.assertFalse(clonedCycle.getWeekList().isEmpty());
        Assertions.assertEquals(originalCycle.getWeekList().size(), clonedCycle.getWeekList().size());
        List<Week> originalWeekList = originalCycle.getWeekList().stream().sorted(Comparator.comparing(Week::getOrder)).toList();
        List<Week> clonedWeekList = clonedCycle.getWeekList().stream().sorted(Comparator.comparing(Week::getOrder)).toList();
        for (int i = 0; i < clonedWeekList.size(); i++) {
            Week originalWeek = originalWeekList.get(i);
            Week clonedWeek = clonedWeekList.get(i);
            assertWeek(originalWeek, clonedWeek);
        }
    }

    private static void assertWeek(Week originalWeek, Week clonedWeek) {
        Assertions.assertNotNull(clonedWeek.getId());
        Assertions.assertNotEquals(originalWeek.getId(), clonedWeek.getId());
        Assertions.assertEquals(originalWeek.getOrder(), clonedWeek.getOrder());
        Assertions.assertFalse(clonedWeek.getCompleted());
        Assertions.assertNotNull(clonedWeek.getWeekWorkoutList());
        Assertions.assertFalse(clonedWeek.getWeekWorkoutList().isEmpty());
        Assertions.assertEquals(originalWeek.getWeekWorkoutList().size(), clonedWeek.getWeekWorkoutList().size());
        List<WeekWorkout> originalWeekWorkoutList = originalWeek.getWeekWorkoutList().stream().sorted(Comparator.comparing(WeekWorkout::getId)).toList();
        List<WeekWorkout> clonedWeekWorkoutList = clonedWeek.getWeekWorkoutList().stream().sorted(Comparator.comparing(WeekWorkout::getDayOfTheWeek)).toList();
        for (int i = 0; i < clonedWeekWorkoutList.size(); i++) {
            WeekWorkout originalWeekWorkout = originalWeekWorkoutList.get(i);
            WeekWorkout clonedWeekWorkout = clonedWeekWorkoutList.get(i);
            assertWeekWorkout(originalWeekWorkout, clonedWeekWorkout);
        }
    }

    private static void assertWeekWorkout(WeekWorkout originalWeekWorkout, WeekWorkout clonedWeekWorkout) {
        Assertions.assertNotNull(clonedWeekWorkout.getId());
        Assertions.assertNotEquals(originalWeekWorkout.getId(), clonedWeekWorkout.getId());
        Assertions.assertNotNull(clonedWeekWorkout.getWeek());
        Assertions.assertNotEquals(originalWeekWorkout.getId(), clonedWeekWorkout.getId());
        Assertions.assertEquals(originalWeekWorkout.getDayOfTheWeek(), clonedWeekWorkout.getDayOfTheWeek());
        Assertions.assertFalse(clonedWeekWorkout.getCompleted());
        Workout originalWorkout = originalWeekWorkout.getWorkout();
        Workout clonedWorkout = clonedWeekWorkout.getWorkout();
        assertWorkout(originalWorkout, clonedWorkout);
    }

    public static void assertWorkout(Workout originalWorkout, Workout clonedWorkout) {
        Assertions.assertNotNull(clonedWorkout.getId());
        Assertions.assertNotEquals(originalWorkout.getId(), clonedWorkout.getId());
        Assertions.assertEquals(originalWorkout.getDescription(), clonedWorkout.getDescription());
        Assertions.assertEquals(originalWorkout.getName(), clonedWorkout.getName());
        Assertions.assertEquals(originalWorkout.getAuthor(), clonedWorkout.getAuthor());
        Assertions.assertEquals(originalWorkout.getTagSet(), clonedWorkout.getTagSet());
        Assertions.assertNotNull(clonedWorkout.getWorkoutExerciseList());
        Assertions.assertEquals(originalWorkout.getWorkoutExerciseList().size(), clonedWorkout.getWorkoutExerciseList().size());
        List<WorkoutExercise> originalWorkoutExerciseList = originalWorkout.getWorkoutExerciseList().stream().sorted(Comparator.comparing(WorkoutExercise::getId)).toList();
        List<WorkoutExercise> clonedWorkoutExerciseList = clonedWorkout.getWorkoutExerciseList().stream().sorted(Comparator.comparing(WorkoutExercise::getId)).toList();
        for (int i = 0; i < clonedWorkoutExerciseList.size(); i++) {
            WorkoutExercise originalWorkoutExercise = originalWorkoutExerciseList.get(i);
            WorkoutExercise clonedWorkoutExercise = clonedWorkoutExerciseList.get(i);
            assertWorkoutExercise(originalWorkoutExercise, clonedWorkoutExercise);
        }
    }

    private static void assertWorkoutExercise(WorkoutExercise originalWorkoutExercise, WorkoutExercise clonedWorkoutExercise) {
        Assertions.assertNotNull(clonedWorkoutExercise.getId());
        Assertions.assertNotEquals(originalWorkoutExercise.getId(), clonedWorkoutExercise.getId());
        Assertions.assertEquals(originalWorkoutExercise.getExercise(), clonedWorkoutExercise.getExercise());
        Assertions.assertEquals(originalWorkoutExercise.getWorkoutExerciseType(), clonedWorkoutExercise.getWorkoutExerciseType());
        Assertions.assertNotNull(clonedWorkoutExercise.getWorkoutExerciseSetList());
        Assertions.assertEquals(originalWorkoutExercise.getWorkoutExerciseSetList().size(), clonedWorkoutExercise.getWorkoutExerciseSetList().size());
        List<WorkoutExerciseSet> originalWorkoutExerciseSetList = originalWorkoutExercise.getWorkoutExerciseSetList().stream().sorted(Comparator.comparing(WorkoutExerciseSet::getId)).toList();
        List<WorkoutExerciseSet> clonedWorkoutExerciseSetList = clonedWorkoutExercise.getWorkoutExerciseSetList().stream().sorted(Comparator.comparing(WorkoutExerciseSet::getId)).toList();
        for (int i = 0; i < clonedWorkoutExerciseSetList.size(); i++) {
            WorkoutExerciseSet originalWorkoutExerciseSet = originalWorkoutExerciseSetList.get(i);
            WorkoutExerciseSet clonedWorkoutExerciseSet = clonedWorkoutExerciseSetList.get(i);
            assertWorkoutExerciseSet(originalWorkoutExerciseSet, clonedWorkoutExerciseSet);
        }
    }

    private static void assertWorkoutExerciseSet(WorkoutExerciseSet originalWorkoutExerciseSet, WorkoutExerciseSet clonedWorkoutExerciseSet) {
        Assertions.assertNotNull(clonedWorkoutExerciseSet.getId());
        Assertions.assertNotEquals(originalWorkoutExerciseSet.getId(), clonedWorkoutExerciseSet.getId());
        Assertions.assertEquals(originalWorkoutExerciseSet.getWorkoutExerciseSetType(), clonedWorkoutExerciseSet.getWorkoutExerciseSetType());
        Assertions.assertEquals(originalWorkoutExerciseSet.getOrder(), clonedWorkoutExerciseSet.getOrder());
        Assertions.assertEquals(originalWorkoutExerciseSet.getNote(), clonedWorkoutExerciseSet.getNote());
        Assertions.assertEquals(originalWorkoutExerciseSet.getRestDuration(), clonedWorkoutExerciseSet.getRestDuration());
        Assertions.assertEquals(originalWorkoutExerciseSet.getGoalTime(), clonedWorkoutExerciseSet.getGoalTime());
        Assertions.assertEquals(originalWorkoutExerciseSet.getGoalWeight(), clonedWorkoutExerciseSet.getGoalWeight());
        Assertions.assertEquals(originalWorkoutExerciseSet.getGoalRepetitions(), clonedWorkoutExerciseSet.getGoalRepetitions());
        Assertions.assertNull(clonedWorkoutExerciseSet.getActualTime());
        Assertions.assertNull(clonedWorkoutExerciseSet.getActualWeight());
        Assertions.assertNull(clonedWorkoutExerciseSet.getActualRepetitions());
        Assertions.assertFalse(clonedWorkoutExerciseSet.getCompleted());
    }

    public static void assertWorkoutResponse(WeekWorkoutCreateRequest request, WeekWorkoutResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertNotEquals(request.workoutId(), response.workoutId());
        Assertions.assertEquals(request.weekId(), response.weekId());
        Assertions.assertEquals(request.dayOfTheWeek(), response.dayOfTheWeek());
        Assertions.assertFalse(response.completed());
        Assertions.assertNotNull(response.workoutTagResponseList());
        Assertions.assertNotNull(response.workoutName());
    }

    public static void assertCycleWorkoutRelation(Cycle originalCycle, Workout clonedWorkout) {
        Assertions.assertNotNull(originalCycle.getWeekList().get(0));
        Assertions.assertNotNull(originalCycle.getWeekList().get(0).getWeekWorkoutList().get(0));
        Assertions.assertNotNull(originalCycle.getWeekList().get(0).getWeekWorkoutList().get(0).getWorkout());
        Assertions.assertEquals(originalCycle.getWeekList().get(0).getWeekWorkoutList().get(0).getWorkout(), clonedWorkout);
    }
}
