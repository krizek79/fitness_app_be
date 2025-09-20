package sk.krizan.fitness_app_be.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.request.TagCreateRequest;
import sk.krizan.fitness_app_be.controller.request.TagFilterRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.TagResponse;
import sk.krizan.fitness_app_be.helper.TagHelper;
import sk.krizan.fitness_app_be.model.entity.Tag;
import sk.krizan.fitness_app_be.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class TagControllerTest {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterTags() throws Exception {
        List<Tag> originalList = new ArrayList<>(List.of(
                TagHelper.createMockTag(),
                TagHelper.createMockTag(),
                TagHelper.createMockTag()
        ));
        originalList = tagRepository.saveAll(originalList);
        List<Tag> expectedList = new ArrayList<>(List.of(originalList.get(2)));

        String name = expectedList.get(0).getName().substring(0, 5);
        TagFilterRequest request = TagHelper.createFilterRequest(0, originalList.size(), Tag.Fields.id, Sort.Direction.DESC.name(), name);
        MvcResult mvcResult = mockMvc.perform(
                        post("/tags/filter")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        PageResponse<TagResponse> response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        TagHelper.assertFilter(expectedList, request, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createTag() throws Exception {
        TagCreateRequest request = TagHelper.createCreateRequest();

        MvcResult mvcResult = mockMvc.perform(
                        post("/tags")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        TagResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        Tag createdTag = tagRepository.findById(response.id()).orElseThrow();
        TagHelper.assertTag_create(request, response, createdTag);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteTag() throws Exception {
        Tag tag = tagRepository.save(TagHelper.createMockTag());

        MvcResult mvcResult = mockMvc.perform(
                        delete("/tags/" + tag.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Long deletedTagId = Long.parseLong(jsonResponse);

        boolean exists = tagRepository.existsById(deletedTagId);

        TagHelper.assertDelete(exists, tag, deletedTagId);
    }
}