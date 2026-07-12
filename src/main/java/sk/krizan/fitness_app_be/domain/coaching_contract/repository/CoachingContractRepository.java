package sk.krizan.fitness_app_be.domain.coaching_contract.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContract;
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContractStatus;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CoachingContractRepository extends JpaRepository<CoachingContract, Long> {

    Page<CoachingContract> findAll(Specification<CoachingContract> specification, Pageable pageable);

    Optional<CoachingContract> findByCoachIdAndClientIdAndStatus(Long coachId, Long clientId, CoachingContractStatus status);

    boolean existsByCoachIdAndClientIdAndStatusIn(Long coachId, Long clientId, Collection<CoachingContractStatus> statuses);

    default CoachingContract getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, CoachingContract.class.getSimpleName() + " with id { %s } does not exist.".formatted(id)));
    }

}
