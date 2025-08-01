package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.exception.ApplicationException;
import sk.krizan.fitness_app_be.controller.request.TagCreateRequest;
import sk.krizan.fitness_app_be.controller.request.TagFilterRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.TagResponse;
import sk.krizan.fitness_app_be.model.entity.Tag;
import sk.krizan.fitness_app_be.model.mapper.TagMapper;
import sk.krizan.fitness_app_be.repository.TagRepository;
import sk.krizan.fitness_app_be.service.api.TagService;
import sk.krizan.fitness_app_be.specification.TagSpecification;
import sk.krizan.fitness_app_be.util.PageUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private static final String ERROR_NOT_FOUND = "Tag with id { %s } does not exist.";
    private static final String ERROR_ALREADY_EXISTS_BY_NAME = "Tag with name { %s } already exists.";

    private static final List<String> supportedSortFields = List.of(
        Tag.Fields.id,
        Tag.Fields.name
    );

    @Override
    @Transactional
    public PageResponse<TagResponse> filterTags(TagFilterRequest request) {
        Specification<Tag> specification = TagSpecification.filter(request);
        Pageable pageable = PageUtils.createPageable(
            request.page(),
            request.size(),
            request.sortBy(),
            request.sortDirection(),
            supportedSortFields
        );

        Page<Tag> page = tagRepository.findAll(specification, pageable);
        List<TagResponse> responseList = page.stream()
            .map(TagMapper::entityToResponse).collect(Collectors.toList());

        return PageResponse.<TagResponse>builder()
            .pageNumber(page.getNumber())
            .pageSize(page.getSize())
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .results(responseList)
            .build();
    }

    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, ERROR_NOT_FOUND.formatted(id)));
    }

    @Override
    public Optional<Tag> findTagByName(String name) {
        return tagRepository.findTagByName(name.toLowerCase());
    }

    @Override
    @Transactional
    public Tag createTag(TagCreateRequest request) {
        if (tagRepository.existsByName(request.name().toLowerCase())) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, ERROR_ALREADY_EXISTS_BY_NAME.formatted(request.name().toLowerCase()));
        }

        return tagRepository.save(TagMapper.createRequestToEntity(request));
    }

    @Override
    public Long deleteTag(Long id) {
        Tag tag = getTagById(id);
        tagRepository.delete(tag);
        return tag.getId();
    }
}
