package sk.krizan.fitness_app_be.domain.week_workout.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;

@Repository
public interface WeekWorkoutRepository extends JpaRepository<WeekWorkout, Long> {

    Page<WeekWorkout> findAll(Specification<WeekWorkout> specification, Pageable pageable);

    default WeekWorkout getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, WeekWorkout.class.getSimpleName() + " with id { %s } does not exist.".formatted(id)));
    }
}
