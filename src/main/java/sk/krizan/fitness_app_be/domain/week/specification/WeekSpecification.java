package sk.krizan.fitness_app_be.domain.week.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.domain.plan.entity.Plan;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekFilterRequest;
import sk.krizan.fitness_app_be.domain.week.entity.Week;

public class WeekSpecification {

    public static Specification<Week> filter(WeekFilterRequest request) {
        return (Root<Week> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (request.planId() != null) {
                Join<Week, Plan> planJoin = root.join(Week.Fields.plan);
                Predicate planIdPredicate = criteriaBuilder.equal(planJoin.get(Plan.Fields.id), request.planId());
                predicate = criteriaBuilder.and(predicate, planIdPredicate);
            }

            return predicate;
        };
    }

}
