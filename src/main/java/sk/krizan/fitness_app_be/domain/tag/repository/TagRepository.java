package sk.krizan.fitness_app_be.domain.tag.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.domain.tag.entity.Tag;

import java.util.List;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Boolean existsByName(String name);

    Page<Tag> findAll(Specification<Tag> specification, Pageable pageable);

    List<Tag> findAllByNameIn(Set<String> uniqueNames);
}
