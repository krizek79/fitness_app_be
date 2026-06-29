package sk.krizan.fitness_app_be.domain.coaching_contract.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.common.util.PredicateUtils;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractFilterRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractFilterClientsRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContract;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.user.entity.User;

public class CoachingContractSpecification {

    public static Specification<CoachingContract> filter(CoachingContractFilterRequest request, User currentUser, boolean isUserAdmin) {
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

            if (!isUserAdmin) {
                Profile currentProfile = currentUser.getProfile();
                Predicate isCoach = criteriaBuilder.equal(root.get(CoachingContract.Fields.coach), currentProfile);
                Predicate isClient = criteriaBuilder.equal(root.get(CoachingContract.Fields.client), currentProfile);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(isCoach, isClient));
            }

            return predicate;
        };
    }

    public static Specification<CoachingContract> filterClients(CoachingContractFilterClientsRequest request, Profile currentProfile) {
        return (Root<CoachingContract> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get(CoachingContract.Fields.coach), currentProfile));
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.isTrue(root.get(CoachingContract.Fields.active)));

            if (request.name() != null) {
                Join<CoachingContract, Profile> clientJoin = root.join(CoachingContract.Fields.client);
                predicate = criteriaBuilder.and(predicate, PredicateUtils.sanitizedLike(criteriaBuilder, clientJoin.get(Profile.Fields.name), request.name()));
            }

            return predicate;
        };
    }

    /**
     * Creates a predicate checking if the current user has access to a resource through an active coaching contract.
     * <p>
     * It builds an EXISTS subquery evaluating to true if there is an active coaching contract
     * where the current user is either the coach or the client of the resource's author.
     *
     * @param currentProfile   the profile of the logged-in user
     * @param authorExpression the path or expression leading to the resource author's Profile (e.g., root.get(Plan.Fields.author))
     * @param query            the criteria query for building the subquery
     * @param criteriaBuilder  the criteria builder for constructing predicates
     * @return a predicate checking the existence of an active coaching contract with the resource author
     */
    public static Predicate getIsCoachPredicate(
            Profile currentProfile,
            Expression<Profile> authorExpression,
            CriteriaQuery<?> query,
            CriteriaBuilder criteriaBuilder
    ) {
        Subquery<Long> contractSubquery = query.subquery(Long.class);
        Root<CoachingContract> contractRoot = contractSubquery.from(CoachingContract.class);

        Predicate currentProfileIsCoach = criteriaBuilder.and(
                criteriaBuilder.equal(contractRoot.get(CoachingContract.Fields.coach), currentProfile),
                criteriaBuilder.equal(contractRoot.get(CoachingContract.Fields.client), authorExpression)
        );

        Predicate currentProfileIsClient = criteriaBuilder.and(
                criteriaBuilder.equal(contractRoot.get(CoachingContract.Fields.client), currentProfile),
                criteriaBuilder.equal(contractRoot.get(CoachingContract.Fields.coach), authorExpression)
        );

        contractSubquery.select(criteriaBuilder.literal(1L))
                .where(criteriaBuilder.and(
                        criteriaBuilder.isTrue(contractRoot.get(CoachingContract.Fields.active)),
                        criteriaBuilder.or(currentProfileIsCoach, currentProfileIsClient)
                ));

        return criteriaBuilder.exists(contractSubquery);
    }

}
