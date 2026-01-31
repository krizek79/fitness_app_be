package sk.krizan.fitness_app_be.domain.week_workout.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.request.WeekWorkoutFilterRequest;
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;

public class WeekWorkoutSpecification {

    public static Specification<WeekWorkout> filter(WeekWorkoutFilterRequest request) {
        return (Root<WeekWorkout> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (request.weekId() != null) {
                Join<WeekWorkout, Week> weekJoin = root.join(WeekWorkout.Fields.week);
                Predicate weekIdPredicate = criteriaBuilder.equal(weekJoin.get(Week.Fields.id), request.weekId());
                predicate = criteriaBuilder.and(predicate, weekIdPredicate);
            }

            return predicate;
        };
    }
}
