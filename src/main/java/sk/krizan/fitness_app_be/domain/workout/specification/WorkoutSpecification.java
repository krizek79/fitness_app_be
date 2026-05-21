package sk.krizan.fitness_app_be.domain.workout.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.tag.entity.Tag;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.common.util.PredicateUtils;

public class WorkoutSpecification {

    public static Specification<Workout> filter(WorkoutFilterRequest request) {
        return (Root<Workout> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (request.title() != null) {
                Predicate namePredicate = PredicateUtils.sanitizedLike(
                        root,
                        criteriaBuilder,
                        Workout.Fields.title,
                        request.title());
                predicate = criteriaBuilder.and(predicate, namePredicate);
            }

            if (request.tagIdList() != null && !request.tagIdList().isEmpty()) {
                Join<Workout, Tag> tagJoin = root.join(Workout.Fields.tags);
                Predicate tagPredicate = tagJoin.get(Tag.Fields.id).in(request.tagIdList());
                predicate = criteriaBuilder.and(predicate, tagPredicate);
            }

            if (request.isTemplate() != null) {
                Predicate isTemplatePredicate = criteriaBuilder.equal(root.get(Workout.Fields.isTemplate), request.isTemplate());
                predicate = criteriaBuilder.and(predicate, isTemplatePredicate);
            }

            if (request.authorId() != null) {
                Join<Workout, Profile> profileJoin = root.join(Workout.Fields.author);
                Predicate authorPredicate = criteriaBuilder.equal(profileJoin.get(Profile.Fields.id), request.authorId());
                predicate = criteriaBuilder.and(predicate, authorPredicate);
            }

            return predicate;
        };
    }
}
