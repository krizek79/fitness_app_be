package sk.krizan.fitness_app_be.domain.week.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.domain.week.entity.Week;

import java.util.List;

@Repository
public interface WeekRepository extends JpaRepository<Week, Long> {

    Page<Week> findAll(Specification<Week> specification, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Week> findAllByCycleIdOrderByOrder(Long cycleId);
}
