package sk.krizan.fitness_app_be.domain.coaching_contract.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractFilterRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContract;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;

public class CoachingContractSpecification {

    public static Specification<CoachingContract> filter(CoachingContractFilterRequest request) {
        return (Root<CoachingContract> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (request.coachId() != null) {
                Join<CoachingContract, Profile> profileJoin = root.join(CoachingContract.Fields.coach);
                Predicate coachIdPredicate = criteriaBuilder.equal(profileJoin.get(Profile.Fields.id), request.coachId());
                predicate = criteriaBuilder.and(predicate, coachIdPredicate);
            }

            if (request.clientId() != null) {
                Join<CoachingContract, Profile> profileJoin = root.join(CoachingContract.Fields.client);
                Predicate clientIdPredicate = criteriaBuilder.equal(profileJoin.get(Profile.Fields.id), request.clientId());
                predicate = criteriaBuilder.and(predicate, clientIdPredicate);
            }

            return predicate;
        };
    }
}
