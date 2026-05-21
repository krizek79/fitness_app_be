package sk.krizan.fitness_app_be.domain.goal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.goal.entity.Goal;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    Page<Goal> findAll(Specification<Goal> specification, Pageable pageable);

    default Goal getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, Goal.class.getSimpleName() + " with id { %s } does not exist.".formatted(id)));
    }

}
