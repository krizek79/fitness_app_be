package sk.krizan.fitness_app_be.domain.goal.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalFilterRequest;
import sk.krizan.fitness_app_be.domain.plan.entity.Plan;
import sk.krizan.fitness_app_be.domain.goal.entity.Goal;

public class GoalSpecification {

    //  TODO: Add access control to ensure users can only see goals they are authorized to view
    public static Specification<Goal> filter(GoalFilterRequest request) {
        return (Root<Goal> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (request.planId() != null) {
                Join<Goal, Plan> planJoin = root.join(Goal.Fields.plan);
                Predicate planIdPredicate = criteriaBuilder.equal(planJoin.get(Plan.Fields.id), request.planId());
                predicate = criteriaBuilder.and(predicate, planIdPredicate);
            }

            return predicate;
        };
    }
}
