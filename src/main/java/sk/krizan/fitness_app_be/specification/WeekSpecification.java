package sk.krizan.fitness_app_be.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.controller.request.WeekFilterRequest;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Week;

public class WeekSpecification {

    public static Specification<Week> filter(WeekFilterRequest request) {
        return (Root<Week> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (request.cycleId() != null) {
                Join<Week, Cycle> cycleJoin = root.join(Week.Fields.cycle);
                Predicate cycleIdPredicate = criteriaBuilder.equal(cycleJoin.get(Cycle.Fields.id), request.cycleId());
                predicate = criteriaBuilder.and(predicate, cycleIdPredicate);
            }

            return predicate;
        };
    }
}
