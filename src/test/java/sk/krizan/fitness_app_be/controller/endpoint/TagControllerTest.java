package sk.krizan.fitness_app_be.controller.endpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.request.TagCreateRequest;
import sk.krizan.fitness_app_be.controller.request.TagFilterRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.TagResponse;
import sk.krizan.fitness_app_be.helper.SecurityHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.helper.TagHelper;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Tag;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.TagRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;
import sk.krizan.fitness_app_be.service.api.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
class TagControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagController tagController;

    @Autowired
    private TagRepository tagRepository;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        User mockUser = UserHelper.createMockUser(Set.of(Role.ADMIN));
        mockUser = userRepository.save(mockUser);

        SecurityHelper.setAuthentication(mockUser);
        when(userService.getCurrentUser()).thenReturn(mockUser);
    }

    @Test
    void filterTags() {
        List<Tag> originalList = new ArrayList<>(List.of(
                TagHelper.createMockTag(),
                TagHelper.createMockTag(),
                TagHelper.createMockTag()
        ));
        originalList = tagRepository.saveAll(originalList);
        List<Tag> expectedList = new ArrayList<>(List.of(originalList.get(2)));

        String name = expectedList.get(0).getName().substring(0, 5);
        TagFilterRequest request = TagHelper.createFilterRequest(0, originalList.size(), Tag.Fields.id, Sort.Direction.DESC.name(), name);
        PageResponse<TagResponse> response = tagController.filterTags(request);

        TagHelper.assertFilter(expectedList, request, response);
    }

    @Test
    void createTag() {
        TagCreateRequest request = TagHelper.createCreateRequest();
        TagResponse response = tagController.createTag(request);
        Tag createdTag = tagRepository.findById(response.id()).orElseThrow();
        TagHelper.assertTag_create(request, response, createdTag);
    }

    @Test
    void deleteTag() {
        Tag Tag = TagHelper.createMockTag();
        Tag = tagRepository.save(Tag);

        Long deletedTagId = tagController.deleteTag(Tag.getId());
        boolean exists = tagRepository.existsById(deletedTagId);

        TagHelper.assertDelete(exists, Tag, deletedTagId);
    }
}