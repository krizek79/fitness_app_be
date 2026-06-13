package sk.krizan.fitness_app_be.domain.plan.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.common.util.PredicateUtils;
import sk.krizan.fitness_app_be.domain.coaching_contract.specification.CoachingContractSpecification;
import sk.krizan.fitness_app_be.domain.plan.entity.Plan;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.request.PlanFilterRequest;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.user.entity.User;

public class PlanSpecification {

    /**
     * Creates a filter for searching plans with access control.
     * <p>
     * The filter applies search criteria from the request and restricts results
     * based on the current user's permissions. Users can see plans they created,
     * plans assigned to them, or all plans if they are admins.
     *
     * @param request     the search criteria
     * @param currentUser the logged-in user; null means no access restrictions
     * @param isUserAdmin is logged-in user admin
     * @return a JPA Specification for filtering plans
     */
    public static Specification<Plan> filter(PlanFilterRequest request, User currentUser, boolean isUserAdmin) {
        return (Root<Plan> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (request.authorId() != null) {
                Join<Plan, Profile> authorJoin = root.join(Plan.Fields.author);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(authorJoin.get(Profile.Fields.id), request.authorId()));
            }

            if (request.traineeId() != null) {
                Join<Plan, Profile> traineeJoin = root.join(Plan.Fields.trainee);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(traineeJoin.get(Profile.Fields.id), request.traineeId()));
            }

            if (request.title() != null) {
                Predicate namePredicate = PredicateUtils.sanitizedLike(
                        root,
                        criteriaBuilder,
                        Plan.Fields.title,
                        request.title());
                predicate = criteriaBuilder.and(predicate, namePredicate);
            }

            if (!isUserAdmin) {
                Profile currentProfile = currentUser.getProfile();
                Predicate authorPredicate = criteriaBuilder.equal(root.get(Plan.Fields.author), currentProfile);
                Predicate traineePredicate = criteriaBuilder.equal(root.get(Plan.Fields.trainee), currentProfile);

                Predicate isCoachPredicate = CoachingContractSpecification.getIsCoachPredicate(currentProfile, root.get(Plan.Fields.author), query, criteriaBuilder);

                Predicate accessPredicate = criteriaBuilder.or(authorPredicate, traineePredicate, isCoachPredicate);
                predicate = criteriaBuilder.and(predicate, accessPredicate);
            }

            return predicate;
        };
    }

}
