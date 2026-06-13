package sk.krizan.fitness_app_be.domain.draft.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import sk.krizan.fitness_app_be.common.BaseIntegrationTest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.FilterAssertionUtils;
import sk.krizan.fitness_app_be.domain.draft.entity.Draft;
import sk.krizan.fitness_app_be.domain.draft.entity.DraftEntityType;
import sk.krizan.fitness_app_be.domain.draft.helper.DraftHelper;
import sk.krizan.fitness_app_be.domain.draft.repository.DraftRepository;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.request.DraftCreateRequest;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.request.DraftFilterRequest;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.request.DraftUpdateRequest;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.response.DraftResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.helper.ProfileHelper;
import sk.krizan.fitness_app_be.domain.profile.repository.ProfileRepository;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.helper.UserHelper;
import sk.krizan.fitness_app_be.domain.user.repository.UserRepository;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class DraftIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DraftRepository draftRepository;

    @MockBean
    private UserService userService;

    private Profile mockProfile;

    private final static String BASE_URL = "/drafts";

    @BeforeEach
    void setUp() {
        User user = userRepository.save(UserHelper.createUser());
        mockProfile = profileRepository.save(ProfileHelper.createProfile(user));

        when(userService.getOrCreateCurrentUser()).thenReturn(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterDrafts() throws Exception {
        User user1 = userRepository.save(UserHelper.createUser());
        Profile profile1 = profileRepository.save(ProfileHelper.createProfile(user1));

        List<Draft> originalList = DraftHelper.createMockDraftListForFilter(profile1, mockProfile);
        originalList = draftRepository.saveAll(originalList);

        List<Draft> expectedList = new ArrayList<>(List.of(originalList.get(originalList.size() - 2)));

        DraftFilterRequest request = DraftHelper.createFilterRequest(0, expectedList.size(), Draft.Fields.id, Sort.Direction.ASC.name(), expectedList.get(0).getEntityType(), expectedList.get(0).getTitle());

        PageResponse<DraftResponse> response = performPost(
                BASE_URL + "/filter",
                request,
                new TypeReference<>() {
                },
                HttpStatus.OK);

        FilterAssertionUtils.assertFilterResults(
                expectedList,
                response,
                Draft::getId,
                DraftResponse::id,
                DraftHelper::assertResponse);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getDraftById() throws Exception {
        Draft draft = draftRepository.save(DraftHelper.createDraft(mockProfile, DraftEntityType.PLAN, "Plan Draft", DraftHelper.createMockContent("Plan Draft", "{\"someAttribute\":\"someValue\"}")));

        DraftResponse response = performGet(
                BASE_URL + "/" + draft.getId(),
                new TypeReference<>() {
                },
                HttpStatus.OK);

        DraftHelper.assertResponse(draft, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createDraft() throws Exception {
        DraftCreateRequest request = DraftHelper.createCreateRequest(
                DraftEntityType.PLAN,
                DraftHelper.createMockContent("Plan A", "{\"someAttribute\":\"someValue1\"}")
        );
        String expectedTitle = request.content().get("title").toString();

        DraftResponse response = performPost(BASE_URL, request, new TypeReference<>() {
        }, HttpStatus.CREATED);

        DraftHelper.assertCreateRequest(request, response, mockProfile, expectedTitle);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateDraft() throws Exception {
        Draft draft = draftRepository.save(DraftHelper.createDraft(mockProfile, DraftEntityType.PLAN, "Plan To Update", DraftHelper.createMockContent("Plan To Update", "{\"someAttribute\":\"someValueToUpdate\"}")));

        DraftUpdateRequest request = DraftHelper.createUpdateRequest(DraftHelper.createMockContent("Updated Plan", "{\"someAttribute\":\"updatedValue\"}"));
        String expectedTitle = request.content().get("title").toString();

        DraftResponse response = performPut(
                BASE_URL + "/" + draft.getId(),
                request, new TypeReference<>() {
                },
                HttpStatus.OK);

        Draft updatedDraft = draftRepository.findById(draft.getId()).orElseThrow();

        DraftHelper.assertUpdateRequest(request, response, updatedDraft, expectedTitle);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteDraft() throws Exception {
        Draft draft = draftRepository.save(DraftHelper.createDraft(mockProfile, DraftEntityType.PLAN, "Plan To Delete", DraftHelper.createMockContent("Plan To Delete", "{\"someAttribute\":\"someValueToDelete\"}")));

        performDeleteNoResponse(BASE_URL + "/" + draft.getId(), HttpStatus.NO_CONTENT);

        boolean exists = draftRepository.existsById(draft.getId());

        Assertions.assertFalse(exists);
    }
}
