package sk.krizan.fitness_app_be.domain.equipment.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.domain.equipment.entity.Equipment;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.request.EquipmentFilterRequest;
import sk.krizan.fitness_app_be.common.util.PredicateUtils;

public class EquipmentSpecification {

    public static Specification<Equipment> filter(EquipmentFilterRequest request) {
        return (Root<Equipment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.isFalse(root.get(Equipment.Fields.deleted));

            if (request.title() != null) {
                Predicate titlePredicate = PredicateUtils.sanitizedLike(
                        root,
                        criteriaBuilder,
                        Equipment.Fields.title,
                        request.title());
                predicate = criteriaBuilder.and(predicate, titlePredicate);
            }

            return predicate;
        };
    }
}

