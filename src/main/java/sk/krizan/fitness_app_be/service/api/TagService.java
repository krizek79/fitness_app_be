package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.controller.request.CreateTagRequest;
import sk.krizan.fitness_app_be.model.entity.Tag;

public interface TagService {

    Tag getTagById(Long id);
    Tag createTag(CreateTagRequest request);
    Long deleteTag(Long id);
}
