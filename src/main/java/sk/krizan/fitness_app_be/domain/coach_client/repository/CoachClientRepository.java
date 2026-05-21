package sk.krizan.fitness_app_be.domain.coach_client.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.coach_client.entity.CoachClient;

import java.util.Optional;

@Repository
public interface CoachClientRepository extends JpaRepository<CoachClient, Long> {

    Page<CoachClient> findAll(Specification<CoachClient> specification, Pageable pageable);

    Optional<CoachClient> findByCoachIdAndClientIdAndActiveTrue(Long coachId, Long clientId);

    default CoachClient getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, CoachClient.class.getSimpleName() + " with id { %s } does not exist.".formatted(id)));
    }

}
