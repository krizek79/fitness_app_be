package sk.krizan.fitness_app_be.domain.coach_client.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.domain.coach_client.entity.CoachClient;

import java.util.Optional;

@Repository
public interface CoachClientRepository extends JpaRepository<CoachClient, Long> {

    Page<CoachClient> findAll(Specification<CoachClient> specification, Pageable pageable);

    Optional<CoachClient> findByCoachIdAndClientIdAndActiveTrue(Long coachId, Long clientId);

    Boolean existsByCoachIdAndClientIdAndActiveTrue(Long coachId, Long clientId);
}
