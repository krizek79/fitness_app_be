package sk.krizan.fitness_app_be.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.model.entity.Tag;
import sk.krizan.fitness_app_be.util.PredicateUtils;

@RequiredArgsConstructor
public class TagSpecification {

    public static Specification<Tag> filter(String name) {
        return (Root<Tag> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            Predicate namePredicate =
                PredicateUtils.sanitizedLike(root, criteriaBuilder, Tag.Fields.name, name);
            predicate = criteriaBuilder.and(predicate, namePredicate);

            return predicate;
        };
    }
}
