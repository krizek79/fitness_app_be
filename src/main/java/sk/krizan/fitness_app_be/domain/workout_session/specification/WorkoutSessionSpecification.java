package sk.krizan.fitness_app_be.domain.workout_session.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.domain.coaching_contract.specification.CoachingContractSpecification;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout_session.entity.WorkoutSession;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.request.WorkoutSessionFilterRequest;

public class WorkoutSessionSpecification {

    public static Specification<WorkoutSession> filter(WorkoutSessionFilterRequest request, User currentUser, boolean isUserAdmin) {
        return (Root<WorkoutSession> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (request.workoutId() != null) {
                Join<WorkoutSession, Workout> workoutJoin = root.join(WorkoutSession.Fields.workout);
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(workoutJoin.get(Workout.Fields.id), request.workoutId()));
            }

            if (request.weekWorkoutId() != null) {
                Join<WorkoutSession, WeekWorkout> weekWorkoutJoin = root.join(WorkoutSession.Fields.weekWorkout);
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(weekWorkoutJoin.get(WeekWorkout.Fields.id), request.weekWorkoutId()));
            }

            if (request.status() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get(WorkoutSession.Fields.status), request.status()));
            }

            if (request.profileId() != null) {
                Join<WorkoutSession, Workout> workoutJoin = root.join(WorkoutSession.Fields.workout);
                Predicate authorPredicate = criteriaBuilder.equal(workoutJoin.get(Workout.Fields.author).get(Profile.Fields.id), request.profileId());
                Predicate traineePredicate = criteriaBuilder.equal(workoutJoin.get(Workout.Fields.trainee).get(Profile.Fields.id), request.profileId());
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(authorPredicate, traineePredicate));
            }

            if (!isUserAdmin) {
                Profile currentProfile = currentUser.getProfile();
                Join<WorkoutSession, Workout> workoutJoin = root.join(WorkoutSession.Fields.workout);
                Predicate authorPredicate = criteriaBuilder.equal(workoutJoin.get(Workout.Fields.author), currentProfile);
                Predicate traineePredicate = criteriaBuilder.equal(workoutJoin.get(Workout.Fields.trainee), currentProfile);
                Predicate isCoachPredicate = CoachingContractSpecification.getIsCoachPredicate(
                        currentProfile, workoutJoin.get(Workout.Fields.author), query, criteriaBuilder);
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.or(authorPredicate, traineePredicate, isCoachPredicate));
            }

            return predicate;
        };
    }
}
