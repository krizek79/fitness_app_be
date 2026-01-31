package sk.krizan.fitness_app_be.domain.tag.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.request.TagFilterRequest;
import sk.krizan.fitness_app_be.domain.tag.entity.Tag;
import sk.krizan.fitness_app_be.common.util.PredicateUtils;

public class TagSpecification {

    public static Specification<Tag> filter(TagFilterRequest request) {
        return (Root<Tag> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            Predicate namePredicate = PredicateUtils.sanitizedLike(
                root,
                criteriaBuilder,
                Tag.Fields.name,
                request.name());
            predicate = criteriaBuilder.and(predicate, namePredicate);

            return predicate;
        };
    }
}
