package sk.krizan.fitness_app_be.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.controller.request.CycleFilterRequest;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.util.PredicateUtils;

public class CycleSpecification {

    public static Specification<Cycle> filter(CycleFilterRequest request) {
        return (Root<Cycle> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (request.authorId() != null) {
                Join<Cycle, Profile> profileJoin = root.join(Cycle.Fields.author);
                Predicate authorIdPredicate = criteriaBuilder.equal(profileJoin.get(Profile.Fields.id), request.authorId());
                predicate = criteriaBuilder.and(predicate, authorIdPredicate);
            }

            if (request.traineeId() != null) {
                Join<Cycle, Profile> profileJoin = root.join(Cycle.Fields.trainee);
                Predicate traineeIdPredicate = criteriaBuilder.equal(profileJoin.get(Profile.Fields.id), request.traineeId());
                predicate = criteriaBuilder.and(predicate, traineeIdPredicate);
            }

            if (request.name() != null) {
                Predicate namePredicate = PredicateUtils.sanitizedLike(
                        root,
                        criteriaBuilder,
                        Cycle.Fields.name,
                        request.name());
                predicate = criteriaBuilder.and(predicate, namePredicate);
            }

            return predicate;
        };
    }
}
