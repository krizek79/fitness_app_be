package sk.krizan.fitness_app_be.domain.draft.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.draft.entity.Draft;

@Repository
public interface DraftRepository extends JpaRepository<Draft, Long> {

    Page<Draft> findAll(Specification<Draft> specification, Pageable pageable);

    default Draft getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, Draft.class.getSimpleName() + " with id { %s } does not exist.".formatted(id)));
    }
}
