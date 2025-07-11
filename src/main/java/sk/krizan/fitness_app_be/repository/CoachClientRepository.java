package sk.krizan.fitness_app_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.model.entity.CoachClient;

import java.util.Optional;

@Repository
public interface CoachClientRepository extends JpaRepository<CoachClient, Long> {

    Page<CoachClient> findAll(Specification<CoachClient> specification, Pageable pageable);
    Optional<CoachClient> findByCoachIdAndClientId(Long coachId, Long clientId);
}
