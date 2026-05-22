package sk.krizan.fitness_app_be.domain.plan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.plan.entity.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    Page<Plan> findAll(Specification<Plan> specification, Pageable pageable);

    default Plan getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, Plan.class.getSimpleName() + " with id { %s } does not exist.".formatted(id)));
    }
}
