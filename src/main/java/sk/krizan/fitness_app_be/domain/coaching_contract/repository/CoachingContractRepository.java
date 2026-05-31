package sk.krizan.fitness_app_be.domain.coaching_contract.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContract;

import java.util.Optional;

@Repository
public interface CoachingContractRepository extends JpaRepository<CoachingContract, Long> {

    Page<CoachingContract> findAll(Specification<CoachingContract> specification, Pageable pageable);

    Optional<CoachingContract> findByCoachIdAndClientIdAndActiveTrue(Long coachId, Long clientId);

    Boolean existsByCoachIdAndClientId(Long coachId, Long clientId);

    default CoachingContract getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, CoachingContract.class.getSimpleName() + " with id { %s } does not exist.".formatted(id)));
    }

}
