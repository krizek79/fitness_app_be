package sk.krizan.fitness_app_be.domain.exercise.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;

import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    Page<Exercise> findAll(Specification<Exercise> specification, Pageable pageable);

    Optional<Exercise> findByIdAndDeletedFalse(Long id);

    default Exercise getByIdOrThrow(Long id) {
        return findByIdAndDeletedFalse(id).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, Exercise.class.getSimpleName() + " with id { %s } does not exist.".formatted(id)));
    }

}
