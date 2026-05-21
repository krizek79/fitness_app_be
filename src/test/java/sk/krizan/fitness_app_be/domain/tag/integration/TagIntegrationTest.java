package sk.krizan.fitness_app_be.domain.tag.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import sk.krizan.fitness_app_be.common.BaseIntegrationTest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.FilterAssertionUtils;
import sk.krizan.fitness_app_be.domain.tag.entity.Tag;
import sk.krizan.fitness_app_be.domain.tag.helper.TagHelper;
import sk.krizan.fitness_app_be.domain.tag.repository.TagRepository;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.request.TagCreateRequest;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.request.TagFilterRequest;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.response.TagResponse;

import java.util.ArrayList;
import java.util.List;

class TagIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TagRepository tagRepository;

    private static final String BASE_URL = "/tags";

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterTags() throws Exception {
        List<Tag> originalList = new ArrayList<>(List.of(
                TagHelper.createTag(),
                TagHelper.createTag(),
                TagHelper.createTag()
        ));
        originalList = tagRepository.saveAll(originalList);
        List<Tag> expectedList = new ArrayList<>(List.of(originalList.get(2)));

        String name = expectedList.get(0).getName().substring(0, 5);
        TagFilterRequest request = TagHelper.createFilterRequest(0, originalList.size(), Tag.Fields.id, Sort.Direction.DESC.name(), name);

        PageResponse<TagResponse> response = performPost(
                BASE_URL + "/filter",
                request,
                new TypeReference<>() {
                },
                HttpStatus.OK);

        FilterAssertionUtils.assertFilterResults(
                expectedList,
                response,
                Tag::getId,
                TagResponse::id,
                TagHelper::assertResponse);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createTag() throws Exception {
        TagCreateRequest request = TagHelper.createCreateRequest();

        TagResponse response = performPost(
                BASE_URL,
                request,
                new TypeReference<>() {
                },
                HttpStatus.CREATED);

        Tag tag = tagRepository.findById(response.id()).orElseThrow();

        TagHelper.assertCreateRequestToEntity(tag, request);
        TagHelper.assertResponse(tag, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteTag() throws Exception {
        Tag tag = tagRepository.save(TagHelper.createTag());

        performDeleteNoResponse(BASE_URL + "/" + tag.getId(), HttpStatus.NO_CONTENT);

        boolean exists = tagRepository.existsById(tag.getId());

        Assertions.assertFalse(exists);
    }

}