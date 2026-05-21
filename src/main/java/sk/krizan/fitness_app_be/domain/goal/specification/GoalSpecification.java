package sk.krizan.fitness_app_be.domain.goal.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalFilterRequest;
import sk.krizan.fitness_app_be.domain.cycle.entity.Cycle;
import sk.krizan.fitness_app_be.domain.goal.entity.Goal;

public class GoalSpecification {

    //  TODO: Add access control to ensure users can only see goals they are authorized to view
    public static Specification<Goal> filter(GoalFilterRequest request) {
        return (Root<Goal> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (request.cycleId() != null) {
                Join<Goal, Cycle> cycleJoin = root.join(Goal.Fields.cycle);
                Predicate cycleIdPredicate = criteriaBuilder.equal(cycleJoin.get(Cycle.Fields.id), request.cycleId());
                predicate = criteriaBuilder.and(predicate, cycleIdPredicate);
            }

            return predicate;
        };
    }
}
