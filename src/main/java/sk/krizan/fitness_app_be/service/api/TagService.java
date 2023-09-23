package sk.krizan.fitness_app_be.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sk.krizan.fitness_app_be.controller.request.CreateTagRequest;
import sk.krizan.fitness_app_be.model.entity.Tag;

public interface TagService {

    Page<Tag> filterTags(Pageable pageable, String name);
    Tag getTagById(Long id);
    Tag createTag(CreateTagRequest request);

    Long deleteTag(Long id);
}
