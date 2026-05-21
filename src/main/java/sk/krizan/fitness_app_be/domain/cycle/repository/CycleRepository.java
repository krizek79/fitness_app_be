package sk.krizan.fitness_app_be.domain.cycle.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.cycle.entity.Cycle;

@Repository
public interface CycleRepository extends JpaRepository<Cycle, Long> {

    Page<Cycle> findAll(Specification<Cycle> specification, Pageable pageable);

    default Cycle getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, Cycle.class.getSimpleName() + " with id { %s } does not exist.".formatted(id)));
    }
}
