package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.controller.exception.IllegalOperationException;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.request.CreateTagRequest;
import sk.krizan.fitness_app_be.model.entity.Tag;
import sk.krizan.fitness_app_be.model.mapper.TagMapper;
import sk.krizan.fitness_app_be.repository.TagRepository;
import sk.krizan.fitness_app_be.service.api.TagService;
import sk.krizan.fitness_app_be.specification.TagSpecification;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public Page<Tag> filterTags(Pageable pageable, String name) {
        Specification<Tag> specification = TagSpecification.filter(name);
        return tagRepository.findAll(specification, pageable);
    }

    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Tag with id { " + id + " } does not exist."));
    }

    @Override
    public Tag createTag(CreateTagRequest request) {
        if (tagRepository.existsByName(request.name().toLowerCase())) {
            throw new IllegalOperationException(
                "Tag with name { " + request.name().toLowerCase() + " } already exists.");
        }

        return tagRepository.save(TagMapper.createRequestToTag(request));
    }

    @Override
    public Long deleteTag(Long id) {
        Tag tag = getTagById(id);
        tagRepository.delete(tag);
        return tag.getId();
    }
}
