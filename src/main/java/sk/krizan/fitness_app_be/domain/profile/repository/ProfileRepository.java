package sk.krizan.fitness_app_be.domain.profile.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByIdAndDeletedFalse(Long id);

    Optional<Profile> findByPublicIdAndDeletedFalse(String publicId);

    boolean existsByPublicId(String publicId);

    Page<Profile> findAll(Specification<Profile> specification, Pageable pageable);

    default Profile getByIdOrThrow(Long id) {
        return findByIdAndDeletedFalse(id).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, Profile.class.getSimpleName() + " with id { %s } does not exist.".formatted(id)));
    }

    default Profile getByPublicIdOrThrow(String publicId) {
        return findByPublicIdAndDeletedFalse(publicId).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, Profile.class.getSimpleName() + " with publicId { %s } does not exist.".formatted(publicId)));
    }

}
