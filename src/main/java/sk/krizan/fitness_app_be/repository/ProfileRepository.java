package sk.krizan.fitness_app_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.model.entity.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
