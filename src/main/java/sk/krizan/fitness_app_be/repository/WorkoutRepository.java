package sk.krizan.fitness_app_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.model.entity.Workout;

import java.util.Optional;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    Page<Workout> findAll(Specification<Workout> specification, Pageable pageable);
    Optional<Workout> findByIdAndDeletedFalse(Long id);
}
