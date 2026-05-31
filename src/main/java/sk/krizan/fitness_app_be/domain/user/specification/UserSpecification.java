package sk.krizan.fitness_app_be.domain.user.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import sk.krizan.fitness_app_be.domain.user.entity.Role;
import sk.krizan.fitness_app_be.domain.user.entity.User;

public class UserSpecification {

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
    public static Predicate getIsAdminPredicate(User currentUser, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Subquery<Long> adminSubquery = query.subquery(Long.class);
        Root<User> userRoot = adminSubquery.from(User.class);
        adminSubquery.select(userRoot.get(User.Fields.id))
                .where(cb.and(
                        cb.equal(userRoot.get(User.Fields.id), currentUser.getId()),
                        cb.isMember(Role.ADMIN, userRoot.get(User.Fields.roles))
                ));
        return cb.exists(adminSubquery);
    }

}
