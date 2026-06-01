package sk.krizan.fitness_app_be.domain.goal.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.domain.goal.entity.Goal;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalFilterRequest;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.specification.UserSpecification;

public class GoalSpecification {

    public static Specification<Goal> filter(GoalFilterRequest request, User currentUser) {
        return (Root<Goal> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (request.profileId() != null) {
                Join<Goal, Profile> profileJoin = root.join(Goal.Fields.profile);
                Predicate profileIdPredicate = criteriaBuilder.equal(profileJoin.get(Profile.Fields.id), request.profileId());
                predicate = criteriaBuilder.and(predicate, profileIdPredicate);
            }

            if (currentUser != null && currentUser.getProfile() != null) {
                Profile currentProfile = currentUser.getProfile();

                Predicate authorPredicate = criteriaBuilder.equal(root.get(Goal.Fields.profile), currentProfile);
                Predicate isAdminPredicate = UserSpecification.getIsAdminPredicate(currentUser, query, criteriaBuilder);

                Predicate accessPredicate = criteriaBuilder.or(authorPredicate, isAdminPredicate);
                predicate = criteriaBuilder.and(predicate, accessPredicate);
            }

            return predicate;
        };
    }
}
