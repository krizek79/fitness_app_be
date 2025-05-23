package sk.krizan.fitness_app_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.model.entity.Week;

import java.util.List;

@Repository
public interface WeekRepository extends JpaRepository<Week, Long> {

    Page<Week> findAll(Specification<Week> specification, Pageable pageable);

    List<Week> findAllByCycleIdAndIdNotOrderByOrder(Long cycleId, Long excludedWeekId);

    List<Week> findAllByCycleIdOrderByOrder(Long cycleId);
}
