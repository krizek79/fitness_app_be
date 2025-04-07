package sk.krizan.fitness_app_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.model.entity.Goal;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    Page<Goal> findAll(Specification<Goal> specification, Pageable pageable);
}
