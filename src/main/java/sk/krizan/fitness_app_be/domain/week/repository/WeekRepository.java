package sk.krizan.fitness_app_be.domain.week.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.week.entity.Week;

@Repository
public interface WeekRepository extends JpaRepository<Week, Long> {

    Page<Week> findAll(Specification<Week> specification, Pageable pageable);

    default Week getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, Week.class.getSimpleName() + " with id { %s } does not exist.".formatted(id)));
    }

}
