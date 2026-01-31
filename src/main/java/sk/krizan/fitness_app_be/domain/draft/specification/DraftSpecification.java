package sk.krizan.fitness_app_be.domain.draft.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.request.DraftFilterRequest;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.draft.entity.Draft;
import sk.krizan.fitness_app_be.common.util.PredicateUtils;

public class DraftSpecification {

    public static Specification<Draft> filter(DraftFilterRequest request, Profile currentProfile) {
        return (Root<Draft> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate predicate = cb.conjunction();

            if (request.title() != null) {
                Predicate titlePredicate = PredicateUtils.sanitizedLike(
                        root,
                        cb,
                        Draft.Fields.title,
                        request.title());
                predicate = cb.and(predicate, titlePredicate);
            }

            predicate = cb.and(predicate, cb.equal(root.get(Draft.Fields.entityType), request.entityType()));

            if (currentProfile != null && currentProfile.getId() != null) {
                Join<Draft, Profile> profileJoin = root.join(Draft.Fields.profile);
                predicate = cb.and(predicate, cb.equal(profileJoin.get(Profile.Fields.id), currentProfile.getId()));
            }

            return predicate;
        };
    }
}
