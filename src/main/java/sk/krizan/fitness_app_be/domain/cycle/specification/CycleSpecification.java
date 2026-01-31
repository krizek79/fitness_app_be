package sk.krizan.fitness_app_be.domain.cycle.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.domain.cycle.rest.dto.request.CycleFilterRequest;
import sk.krizan.fitness_app_be.domain.cycle.entity.Cycle;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.common.util.PredicateUtils;

public class CycleSpecification {

    public static Specification<Cycle> filter(CycleFilterRequest request, Profile currentProfile) {
        return (Root<Cycle> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate predicate = cb.conjunction();

            if (request.authorId() != null) {
                Join<Cycle, Profile> authorJoin = root.join(Cycle.Fields.author);
                predicate = cb.and(predicate, cb.equal(authorJoin.get(Profile.Fields.id), request.authorId()));
            }

            if (request.traineeId() != null) {
                Join<Cycle, Profile> traineeJoin = root.join(Cycle.Fields.trainee);
                predicate = cb.and(predicate, cb.equal(traineeJoin.get(Profile.Fields.id), request.traineeId()));
            }

            if (request.title() != null) {
                Predicate namePredicate = PredicateUtils.sanitizedLike(
                        root,
                        cb,
                        Cycle.Fields.title,
                        request.title());
                predicate = cb.and(predicate, namePredicate);
            }

            if (request.level() != null) {
                predicate = cb.and(predicate, cb.equal(root.get(Cycle.Fields.level), request.level()));
            }

            if (currentProfile != null) {
                Predicate authorPredicate = cb.equal(root.get(Cycle.Fields.author), currentProfile);
                Predicate traineePredicate = cb.equal(root.get(Cycle.Fields.trainee), currentProfile);
                Predicate accessPredicate = cb.or(authorPredicate, traineePredicate);
                predicate = cb.and(predicate, accessPredicate);
            }

            return predicate;
        };
    }
}
