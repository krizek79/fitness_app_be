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
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContractStatus;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractCallerRole;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractFilterRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractFilterConnectionsRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContract;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.user.entity.User;

import java.util.List;

public class CoachingContractSpecification {

    public static Specification<CoachingContract> filter(CoachingContractFilterRequest request, User currentUser, boolean isUserAdmin) {
        return (Root<CoachingContract> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            Profile currentProfile = currentUser.getProfile();

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

            if (request.statuses() != null && !request.statuses().isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get(CoachingContract.Fields.status).in(request.statuses()));
            }

            if (request.callerRole() == CoachingContractCallerRole.COACH) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(CoachingContract.Fields.coach), currentProfile));
            } else if (request.callerRole() == CoachingContractCallerRole.CLIENT) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(CoachingContract.Fields.client), currentProfile));
            }

            if (!isUserAdmin) {
                Predicate isCoach = criteriaBuilder.equal(root.get(CoachingContract.Fields.coach), currentProfile);
                Predicate isClient = criteriaBuilder.equal(root.get(CoachingContract.Fields.client), currentProfile);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(isCoach, isClient));
            }

            return predicate;
        };
    }

    /**
     * Builds a predicate for listing the "other side" profiles (clients of a coach, or coaches of a client)
     * of the current user's ACTIVE coaching contracts, optionally filtered by that other profile's name.
     *
     * @param callerRole whether the current user is acting as COACH (returns their clients) or CLIENT (returns their coaches)
     */
    public static Specification<CoachingContract> filterConnections(CoachingContractFilterConnectionsRequest request, Profile currentProfile, CoachingContractCallerRole callerRole) {
        String selfField = callerRole == CoachingContractCallerRole.COACH ? CoachingContract.Fields.coach : CoachingContract.Fields.client;
        String otherField = callerRole == CoachingContractCallerRole.COACH ? CoachingContract.Fields.client : CoachingContract.Fields.coach;

        return (Root<CoachingContract> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get(selfField), currentProfile));
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get(CoachingContract.Fields.status), CoachingContractStatus.ACTIVE));

            if (request.name() != null) {
                Join<CoachingContract, Profile> otherJoin = root.join(otherField);
                predicate = criteriaBuilder.and(predicate, PredicateUtils.sanitizedLike(criteriaBuilder, otherJoin.get(Profile.Fields.name), request.name()));
            }

            return predicate;
        };
    }

    /**
     * Creates a predicate checking if the current user has trainee-side access to a resource through a
     * coaching contract with the resource's author, mirroring {@code SecurityAccessValidator.checkCoachingContractAccess}.
     * <p>
     * Must be combined (AND) with a predicate asserting the resource's trainee is the current user -
     * this only checks that a qualifying contract exists between the current user (as client) and the
     * resource author (as coach). ACTIVE grants access; EXPIRED/TERMINATED grant read-only history access,
     * which is safe here since filter/list endpoints are read-only.
     *
     * @param currentProfile   the profile of the logged-in user (the client side of the contract)
     * @param authorExpression the path or expression leading to the resource author's Profile (e.g., root.get(Plan.Fields.author))
     * @param query            the criteria query for building the subquery
     * @param criteriaBuilder  the criteria builder for constructing predicates
     * @return a predicate checking the existence of a qualifying coaching contract with the resource author
     */
    public static Predicate getTraineeContractAccessPredicate(
            Profile currentProfile,
            Expression<Profile> authorExpression,
            CriteriaQuery<?> query,
            CriteriaBuilder criteriaBuilder
    ) {
        Subquery<Long> contractSubquery = query.subquery(Long.class);
        Root<CoachingContract> contractRoot = contractSubquery.from(CoachingContract.class);

        contractSubquery.select(criteriaBuilder.literal(1L))
                .where(criteriaBuilder.and(
                        criteriaBuilder.equal(contractRoot.get(CoachingContract.Fields.coach), authorExpression),
                        criteriaBuilder.equal(contractRoot.get(CoachingContract.Fields.client), currentProfile),
                        contractRoot.get(CoachingContract.Fields.status).in(List.of(
                                CoachingContractStatus.ACTIVE,
                                CoachingContractStatus.EXPIRED,
                                CoachingContractStatus.TERMINATED
                        ))
                ));

        return criteriaBuilder.exists(contractSubquery);
    }

}
