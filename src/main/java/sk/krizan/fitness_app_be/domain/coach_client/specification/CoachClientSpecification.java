package sk.krizan.fitness_app_be.domain.coach_client.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.domain.coach_client.rest.dto.request.CoachClientFilterRequest;
import sk.krizan.fitness_app_be.domain.coach_client.entity.CoachClient;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;

public class CoachClientSpecification {

    public static Specification<CoachClient> filter(CoachClientFilterRequest request) {
        return (Root<CoachClient> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (request.coachId() != null) {
                Join<CoachClient, Profile> profileJoin = root.join(CoachClient.Fields.coach);
                Predicate coachIdPredicate = criteriaBuilder.equal(profileJoin.get(Profile.Fields.id), request.coachId());
                predicate = criteriaBuilder.and(predicate, coachIdPredicate);
            }

            if (request.clientId() != null) {
                Join<CoachClient, Profile> profileJoin = root.join(CoachClient.Fields.client);
                Predicate clientIdPredicate = criteriaBuilder.equal(profileJoin.get(Profile.Fields.id), request.clientId());
                predicate = criteriaBuilder.and(predicate, clientIdPredicate);
            }

            return predicate;
        };
    }
}
