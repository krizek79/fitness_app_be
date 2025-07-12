package sk.krizan.fitness_app_be.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.controller.request.ProfileFilterRequest;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.util.PredicateUtils;

public class ProfileSpecification {

    public static Specification<Profile> filter(ProfileFilterRequest request) {
        return (Root<Profile> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.isFalse(root.get(Profile.Fields.deleted));

            if (request.name() != null) {
                Predicate namePredicate = PredicateUtils.sanitizedLike(
                        root,
                        criteriaBuilder,
                        Profile.Fields.name,
                        request.name());
                predicate = criteriaBuilder.and(predicate, namePredicate);
            }

            return predicate;
        };
    }
}
