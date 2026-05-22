package sk.krizan.fitness_app_be.domain.plan.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.domain.plan.entity.Plan;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.request.PlanFilterRequest;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.user.entity.Role;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.common.util.PredicateUtils;

public class PlanSpecification {

    /**
     * Creates a filter for searching plans with access control.
     * <p>
     * The filter applies search criteria from the request and restricts results
     * based on the current user's permissions. Users can see plans they created,
     * plans assigned to them, or all plans if they are admins.
     *
     * @param request the search criteria
     * @param currentUser the logged-in user; null means no access restrictions
     * @return a JPA Specification for filtering plans
     */
    public static Specification<Plan> filter(PlanFilterRequest request, User currentUser) {
        return (Root<Plan> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate predicate = cb.conjunction();

            if (request.authorId() != null) {
                Join<Plan, Profile> authorJoin = root.join(Plan.Fields.author);
                predicate = cb.and(predicate, cb.equal(authorJoin.get(Profile.Fields.id), request.authorId()));
            }

            if (request.traineeId() != null) {
                Join<Plan, Profile> traineeJoin = root.join(Plan.Fields.trainee);
                predicate = cb.and(predicate, cb.equal(traineeJoin.get(Profile.Fields.id), request.traineeId()));
            }

            if (request.title() != null) {
                Predicate namePredicate = PredicateUtils.sanitizedLike(
                        root,
                        cb,
                        Plan.Fields.title,
                        request.title());
                predicate = cb.and(predicate, namePredicate);
            }

            if (currentUser != null && currentUser.getProfile() != null) {
                Profile currentProfile = currentUser.getProfile();
                Predicate authorPredicate = cb.equal(root.get(Plan.Fields.author), currentProfile);
                Predicate traineePredicate = cb.equal(root.get(Plan.Fields.trainee), currentProfile);

                Predicate isAdminPredicate = getIsAdminPredicate(currentUser, query, cb);

                Predicate accessPredicate = cb.or(authorPredicate, traineePredicate, isAdminPredicate);
                predicate = cb.and(predicate, accessPredicate);
            }

            return predicate;
        };
    }

    /**
     * Checks if the current user has admin privileges.
     * <p>
     * Returns a database-level check that evaluates to true if the user has
     * the ADMIN role. This predicate is used to grant unrestricted access to plans.
     *
     * @param currentUser the user to check
     * @param query the criteria query for building subqueries
     * @param cb the criteria builder for constructing predicates
     * @return a predicate that evaluates to true if user is admin
     */
    private static Predicate getIsAdminPredicate(User currentUser, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Subquery<Long> adminSubquery = query.subquery(Long.class);
        Root<User> userRoot = adminSubquery.from(User.class);
        adminSubquery.select(userRoot.get(User.Fields.id))
                .where(cb.and(
                        cb.equal(userRoot.get(User.Fields.id), currentUser.getId()),
                        cb.isMember(Role.ADMIN, userRoot.get(User.Fields.roleSet))
                ));
        return cb.exists(adminSubquery);
    }
}
