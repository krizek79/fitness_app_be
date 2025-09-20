package sk.krizan.fitness_app_be.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.request.BatchUpdateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WeekUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.SimpleListResponse;
import sk.krizan.fitness_app_be.controller.response.WeekResponse;
import sk.krizan.fitness_app_be.helper.CycleHelper;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.helper.WeekHelper;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Week;
import sk.krizan.fitness_app_be.model.enums.Level;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.CycleRepository;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;
import sk.krizan.fitness_app_be.repository.WeekRepository;
import sk.krizan.fitness_app_be.service.api.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class WeekControllerTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeekRepository weekRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private Profile mockProfile;
    private Cycle mockCycle;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(UserHelper.createMockUser(Set.of(Role.ADMIN)));
        when(userService.getCurrentUser()).thenReturn(user);

        mockProfile = profileRepository.save(ProfileHelper.createMockProfile(user));

        mockCycle = cycleRepository.save(CycleHelper.createMockCycle(mockProfile, mockProfile, Level.BEGINNER));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterWeeks() throws Exception {
        Cycle cycle1 = CycleHelper.createMockCycle(mockProfile, mockProfile, Level.BEGINNER);
        cycle1 = cycleRepository.save(cycle1);

        Week week1 = WeekHelper.createMockWeek(mockCycle, 1);
        week1 = weekRepository.save(week1);
        Week week2 = WeekHelper.createMockWeek(cycle1, 1);
        week2 = weekRepository.save(week2);
        List<Week> originalList = new ArrayList<>(List.of(week1, week2));
        List<Week> expectedList = new ArrayList<>(List.of(originalList.get(1)));

        WeekFilterRequest request = WeekHelper.createFilterRequest(0, originalList.size(), Week.Fields.id, Sort.Direction.DESC.name(), cycle1.getId());

        MvcResult mvcResult = mockMvc.perform(
                        post("/weeks/filter")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        PageResponse<WeekResponse> response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        WeekHelper.assertFilter(expectedList, request, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWeekById() throws Exception {
        Week week = WeekHelper.createMockWeek(mockCycle, 1);
        week = weekRepository.save(week);

        MvcResult mvcResult = mockMvc.perform(
                        get("/weeks/" + week.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        WeekResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        WeekHelper.assertGet(week, response);
    }

    private static Stream<Arguments> createWeekMethodSource() {
        return Stream.of(
                // Case 1: Create with position 2 -> should insert into position 2, shift week2 and week3 to 3 and 4
                Arguments.of(2, 2, List.of(1, 2, 3, 4)),
                // Case 2: Create with position 5 -> beyond current size -> goes to last (4th place)
                Arguments.of(5, 4, List.of(1, 2, 3, 4))
        );
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @MethodSource("createWeekMethodSource")
    void createWeek(
            Integer requestedOrder,
            Integer expectedInsertedOrder,
            List<Integer> expectedFinalOrder
    ) throws Exception {
        List<Week> originalList = List.of(
                WeekHelper.createMockWeek(mockCycle, 1),
                WeekHelper.createMockWeek(mockCycle, 2),
                WeekHelper.createMockWeek(mockCycle, 3)
        );
        weekRepository.saveAll(originalList);

        WeekCreateRequest request = WeekHelper.createCreateRequest(mockCycle.getId(), requestedOrder);

        MvcResult mvcResult = mockMvc.perform(
                        post("/weeks")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        WeekResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        List<Week> finalWeekList = weekRepository.findAllByCycleIdOrderByOrder(mockCycle.getId());
        WeekHelper.assertCreateWeek(request, response, expectedInsertedOrder, finalWeekList, expectedFinalOrder);
    }

    public static Stream<Arguments> updateWeekMethodSource() {
        return Stream.of(
                // Move week at position 3 to position 1
                Arguments.of(List.of(1, 2, 3, 4), 2, 1, List.of(2, 0, 1, 3)),
                // Move week at position 1 to position 3
                Arguments.of(List.of(1, 2, 3, 4), 0, 3, List.of(1, 2, 0, 3)),
                // Move week at position 4 to position 2
                Arguments.of(List.of(1, 2, 3, 4), 3, 2, List.of(0, 3, 1, 2)),
                // Move week at position 2 to position 4
                Arguments.of(List.of(1, 2, 3, 4), 1, 4, List.of(0, 2, 3, 1)),
                // Move week at position 2 to the same position (should result in no reordering)
                Arguments.of(List.of(1, 2, 3), 1, 2, List.of(0, 1, 2))
        );
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @MethodSource("updateWeekMethodSource")
    void updateWeek(
            List<Integer> originalOrders,
            Integer idOfElementToUpdate,
            Integer newRequestedOrder,
            List<Integer> listIdsOfExpectedElementsInOrder
    ) throws Exception {
        List<Week> originalList = new ArrayList<>();
        for (Integer order : originalOrders) {
            Week week = WeekHelper.createMockWeek(mockCycle, order);
            originalList.add(weekRepository.save(week));
        }
        Assertions.assertEquals(originalList.size(), listIdsOfExpectedElementsInOrder.size());
        List<Long> idsOfExpectedElementsInOrder = listIdsOfExpectedElementsInOrder.stream()
                .map(index -> originalList.get(index).getId())
                .collect(Collectors.toList());

        Week weekToUpdate = originalList.get(idOfElementToUpdate);
        WeekUpdateRequest request = WeekHelper.createUpdateRequest(weekToUpdate.getId(), newRequestedOrder);

        MvcResult mvcResult = mockMvc.perform(
                        put("/weeks")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        WeekResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        List<Week> finalWeekList = weekRepository.findAllByCycleIdOrderByOrder(mockCycle.getId());
        WeekHelper.assertUpdateWeek(request, response, idsOfExpectedElementsInOrder, finalWeekList);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void batchUpdateWeeks() throws Exception {
        List<Week> originalList = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            Week week = WeekHelper.createMockWeek(mockCycle, i);
            originalList.add(weekRepository.save(week));
        }

        List<WeekUpdateRequest> requestList = new ArrayList<>();
        for (int i = originalList.size(); i > 0; i--) {
            Long id = originalList.get(originalList.size() - i).getId();
            requestList.add(WeekHelper.createUpdateRequest(id, i));
        }
        BatchUpdateRequest<WeekUpdateRequest> batchRequest = BatchUpdateRequest.<WeekUpdateRequest>builder()
                .updateRequestList(requestList)
                .build();

        MvcResult mvcResult = mockMvc.perform(
                        put("/weeks/batch-update")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(batchRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        SimpleListResponse<WeekResponse> listResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        WeekHelper.assertBatchUpdate(requestList, listResponse);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteWeek() throws Exception {
        Week weekToUpdate = weekRepository.save(WeekHelper.createMockWeek(mockCycle, 2));
        Week weekToDelete = weekRepository.save(WeekHelper.createMockWeek(mockCycle, 1));

        MvcResult mvcResult = mockMvc.perform(
                        delete("/weeks/" + weekToDelete.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Long deletedWeekId = Long.parseLong(jsonResponse);

        boolean exists = weekRepository.existsById(deletedWeekId);
        weekToUpdate = weekRepository.findById(weekToUpdate.getId()).orElseThrow();

        WeekHelper.assertDelete(exists, weekToDelete, deletedWeekId, weekToUpdate);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void triggerCompleted() throws Exception {
        Week week = weekRepository.save(WeekHelper.createMockWeek(mockCycle, 1));
        Boolean originalState = week.getCompleted();

        MvcResult mvcResult = mockMvc.perform(
                        patch("/weeks/" + week.getId() + "/trigger-completed")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        WeekResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        WeekHelper.assertTriggerCompleted(originalState, response);
    }
}