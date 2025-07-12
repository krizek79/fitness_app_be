package sk.krizan.fitness_app_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.model.entity.Profile;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByIdAndDeletedFalse(Long id);

    Boolean existsByNameAndDeletedFalse(String name);

    Page<Profile> findAll(Specification<Profile> specification, Pageable pageable);
}
