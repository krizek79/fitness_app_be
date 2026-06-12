package sk.krizan.fitness_app_be.domain.exercise.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.SetJoin;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.common.util.PredicateUtils;
import sk.krizan.fitness_app_be.domain.equipment.entity.Equipment;
import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseFilterRequest;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity.ExerciseMuscleRole;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity.Muscle;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;

public class ExerciseSpecification {

    public static Specification<Exercise> filter(ExerciseFilterRequest request) {
        return (Root<Exercise> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.isFalse(root.get(Profile.Fields.deleted));

            if (request.title() != null) {
                Predicate titlePredicate = PredicateUtils.sanitizedLike(
                        root,
                        criteriaBuilder,
                        Exercise.Fields.title,
                        request.title());
                predicate = criteriaBuilder.and(predicate, titlePredicate);
            }

            if (request.movementPatterns() != null && !request.movementPatterns().isEmpty()) {
                SetJoin<Exercise, Muscle> movementPatternJoin = root.joinSet(Exercise.Fields.movementPatterns);

                query.groupBy(root.get(Exercise.Fields.id));
                Predicate havingPredicate = criteriaBuilder.equal(
                        criteriaBuilder.countDistinct(movementPatternJoin),
                        request.movementPatterns().size()
                );
                Predicate inPredicate = movementPatternJoin.in(request.movementPatterns());
                query.having(criteriaBuilder.and(havingPredicate));
                predicate = criteriaBuilder.and(predicate, inPredicate);
            }

            if (request.requiredEquipmentIds() != null && !request.requiredEquipmentIds().isEmpty()) {
                SetJoin<Exercise, Equipment> equipmentJoin = root.joinSet(Exercise.Fields.requiredEquipment);

                query.groupBy(root.get(Exercise.Fields.id));
                Predicate havingPredicate = criteriaBuilder.equal(
                        criteriaBuilder.countDistinct(equipmentJoin),
                        request.requiredEquipmentIds().size()
                );
                Predicate inPredicate = equipmentJoin.get(Equipment.Fields.id).in(request.requiredEquipmentIds());
                query.having(criteriaBuilder.and(havingPredicate));
                predicate = criteriaBuilder.and(predicate, inPredicate);
            }

            if (request.muscles() != null && !request.muscles().isEmpty()) {
                SetJoin<Exercise, ExerciseMuscleRole> exerciseMuscleRoleJoin = root.joinSet(Exercise.Fields.muscles);

                query.groupBy(root.get(Exercise.Fields.id));

                Predicate havingPredicate = criteriaBuilder.equal(
                        criteriaBuilder.countDistinct(exerciseMuscleRoleJoin.get(ExerciseMuscleRole.Fields.muscle)),
                        request.muscles().size()
                );

                Predicate inPredicate = exerciseMuscleRoleJoin
                        .get(ExerciseMuscleRole.Fields.muscle)
                        .in(request.muscles());

                query.having(criteriaBuilder.and(havingPredicate));

                predicate = criteriaBuilder.and(predicate, inPredicate);
            }

            if (request.exerciseCategory() != null) {
                Predicate categoryPredicate = criteriaBuilder.equal(root.get(Exercise.Fields.exerciseCategory), request.exerciseCategory());
                predicate = criteriaBuilder.and(predicate, categoryPredicate);
            }

            return predicate;
        };
    }
}
