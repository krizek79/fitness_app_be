package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.request.CreateTagRequest;
import sk.krizan.fitness_app_be.model.entity.Tag;
import sk.krizan.fitness_app_be.repository.TagRepository;
import sk.krizan.fitness_app_be.service.api.TagService;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Tag with id { " + id + " } does not exist."));
    }

    @Override
    public Tag createTag(CreateTagRequest request) {
        return null;
    }

    @Override
    public Long deleteTag(Long id) {
        Tag tag = getTagById(id);
        tagRepository.delete(tag);
        return tag.getId();
    }
}
