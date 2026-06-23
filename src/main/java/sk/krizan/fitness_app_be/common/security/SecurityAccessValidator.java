package sk.krizan.fitness_app_be.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContract;
import sk.krizan.fitness_app_be.domain.coaching_contract.repository.CoachingContractRepository;
import sk.krizan.fitness_app_be.domain.draft.entity.Draft;
import sk.krizan.fitness_app_be.domain.draft.repository.DraftRepository;
import sk.krizan.fitness_app_be.domain.goal.entity.Goal;
import sk.krizan.fitness_app_be.domain.goal.repository.GoalRepository;
import sk.krizan.fitness_app_be.domain.plan.entity.Plan;
import sk.krizan.fitness_app_be.domain.plan.repository.PlanRepository;
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week.repository.WeekRepository;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;
import sk.krizan.fitness_app_be.domain.week_workout.repository.WeekWorkoutRepository;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout.repository.WorkoutRepository;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.domain.workout_exercise.repository.WorkoutExerciseRepository;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.entity.WorkoutExerciseSession;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.repository.WorkoutExerciseSessionRepository;
import sk.krizan.fitness_app_be.domain.workout_session.entity.WorkoutSession;
import sk.krizan.fitness_app_be.domain.workout_session.repository.WorkoutSessionRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityAccessValidator {

    private final GoalRepository goalRepository;
    private final PlanRepository planRepository;
    private final WeekRepository weekRepository;
    private final DraftRepository draftRepository;
    private final WorkoutRepository workoutRepository;
    private final WeekWorkoutRepository weekWorkoutRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final WorkoutExerciseSessionRepository workoutExerciseSessionRepository;
    private final CoachingContractRepository coachingContractRepository;

    @Transactional(readOnly = true)
    public boolean canAccessPlan(Long planId, Long profileId, Permission permission) {
        Plan plan = planRepository.getByIdOrThrow(planId);
        Long traineeId = plan.getTrainee() != null ? plan.getTrainee().getId() : null;
        return checkResourceAccess(plan.getAuthor().getId(), traineeId, profileId, permission);
    }

    @Transactional(readOnly = true)
    public boolean canAccessWeek(Long weekId, Long profileId, Permission permission) {
        Week week = weekRepository.getByIdOrThrow(weekId);
        Plan plan = week.getPlan();
        Long traineeId = plan.getTrainee() != null ? plan.getTrainee().getId() : null;
        return checkResourceAccess(plan.getAuthor().getId(), traineeId, profileId, permission);
    }

    @Transactional(readOnly = true)
    public boolean canAccessWeekWorkout(Long weekWorkoutId, Long profileId, Permission permission) {
        WeekWorkout weekWorkout = weekWorkoutRepository.getByIdOrThrow(weekWorkoutId);
        Plan plan = weekWorkout.getWeek().getPlan();
        Long traineeId = plan.getTrainee() != null ? plan.getTrainee().getId() : null;
        return checkResourceAccess(plan.getAuthor().getId(), traineeId, profileId, permission);
    }

    @Transactional(readOnly = true)
    public boolean canAccessWorkout(Long workoutId, Long profileId, Permission permission) {
        Workout workout = workoutRepository.getByIdOrThrow(workoutId);
        Long traineeId = workout.getTrainee() != null ? workout.getTrainee().getId() : null;
        return checkResourceAccess(workout.getAuthor().getId(), traineeId, profileId, permission);
    }

    @Transactional(readOnly = true)
    public boolean canAccessWorkoutExercise(Long workoutExerciseId, Long profileId, Permission permission) {
        WorkoutExercise workoutExercise = workoutExerciseRepository.getByIdOrThrow(workoutExerciseId);
        return canAccessWorkout(workoutExercise.getWorkout().getId(), profileId, permission);
    }

    @Transactional(readOnly = true)
    public boolean canAccessWorkoutSession(Long workoutSessionId, Long profileId, Permission permission) {
        WorkoutSession workoutSession = workoutSessionRepository.getByIdOrThrow(workoutSessionId);
        return canAccessWorkout(workoutSession.getWorkout().getId(), profileId, permission);
    }

    @Transactional(readOnly = true)
    public boolean canAccessWorkoutExerciseSession(Long workoutExerciseSessionId, Long profileId, Permission permission) {
        WorkoutExerciseSession workoutExerciseSession = workoutExerciseSessionRepository.getByIdOrThrow(workoutExerciseSessionId);
        return canAccessWorkoutSession(workoutExerciseSession.getWorkoutSession().getId(), profileId, permission);
    }

    @Transactional(readOnly = true)
    public boolean canAccessGoal(Long goalId, Long profileId, Permission permission) {
        Goal goal = goalRepository.getByIdOrThrow(goalId);
        return checkResourceAccess(goal.getProfile().getId(), goal.getProfile().getId(), profileId, permission);
    }

    @Transactional(readOnly = true)
    public boolean canAccessDraft(Long draftId, Long profileId, Permission mappedPermission) {
        Draft draft = draftRepository.getByIdOrThrow(draftId);
        return checkResourceAccess(draft.getProfile().getId(), draft.getProfile().getId(), profileId, mappedPermission);
    }

    /**
     * Checks if the current user has access to a resource.
     *
     * @param authorId The ID of the resource author (coach)
     * @param traineeId The ID of the resource trainee (client), can be null
     * @param profileId The ID of the current user
     * @param permission The required permission
     * @return true if the user has the required permission, false otherwise
     */
    private boolean checkResourceAccess(Long authorId, Long traineeId, Long profileId, Permission permission) {
        // User is the author - has full access
        if (authorId.equals(profileId)) {
            return true;
        }

        // OWNER permission is only for the author
        if (permission == Permission.OWNER) {
            return false;
        }

        // If there's no trainee assigned, only the author has access
        if (traineeId == null) {
            return false;
        }

        // User is the trainee - check coaching contract
        if (traineeId.equals(profileId)) {
            return checkCoachingContractAccess(authorId, traineeId, permission);
        }

        // User is neither author nor trainee
        return false;
    }

    /**
     * Checks if the trainee has access based on the coaching contract status.
     *
     * @param coachId The ID of the coach (author)
     * @param clientId The ID of the client (trainee)
     * @param permission The required permission
     * @return true if the coaching contract allows the permission, false otherwise
     */
    private boolean checkCoachingContractAccess(Long coachId, Long clientId, Permission permission) {
        Optional<CoachingContract> contract = coachingContractRepository.findByCoachIdAndClientIdAndActiveTrue(coachId, clientId);

        if (contract.isPresent()) {
            // Active contract - client has full access
            return true;
        }

        // No active contract - check if there's an inactive one
        if (coachingContractRepository.existsByCoachIdAndClientId(coachId, clientId)) {
            // Inactive contract - client has read-only access
            return permission == Permission.READ;
        }

        // No contract at all - no access
        return false;
    }
}