package sk.krizan.fitness_app_be.domain.equipment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.equipment.entity.Equipment;

import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    Page<Equipment> findAll(Specification<Equipment> specification, Pageable pageable);

    Optional<Equipment> findByIdAndDeletedFalse(Long id);

    default Equipment getByIdOrThrow(Long id) {
        return findByIdAndDeletedFalse(id).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, Equipment.class.getSimpleName() + " with id { %s } does not exist.".formatted(id)));
    }

}
