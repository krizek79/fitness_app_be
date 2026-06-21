package sk.krizan.fitness_app_be.domain.equipment.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.javafaker.Faker;
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
import sk.krizan.fitness_app_be.domain.equipment.entity.Equipment;
import sk.krizan.fitness_app_be.domain.equipment.helper.EquipmentHelper;
import sk.krizan.fitness_app_be.domain.equipment.repository.EquipmentRepository;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.request.EquipmentFilterRequest;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.request.EquipmentInputRequest;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.response.EquipmentResponse;
import sk.krizan.fitness_app_be.domain.media.MediaService;
import sk.krizan.fitness_app_be.domain.media.helper.MediaHelper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class EquipmentIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @MockBean
    private MediaService mediaService;

    private static final String BASE_URL = "/equipment";
    private static final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        when(mediaService.uploadFile(any(), any())).thenReturn(faker.internet().image());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterEquipment() throws Exception {
        List<Equipment> equipmentList = equipmentRepository.saveAll(List.of(
                EquipmentHelper.createEquipment(),
                EquipmentHelper.createEquipment(),
                EquipmentHelper.createEquipment()
        ));

        List<Equipment> expected = List.of(equipmentList.get(1));

        EquipmentFilterRequest request = EquipmentHelper.createFilterRequest(0, expected.size(), Equipment.Fields.id, Sort.Direction.DESC.name(), expected.get(0).getTitle());

        PageResponse<EquipmentResponse> response = performPost(
                BASE_URL + "/filter",
                request,
                new TypeReference<>() {
                },
                HttpStatus.OK);

        FilterAssertionUtils.assertFilterResults(
                expected,
                response,
                Equipment::getId,
                EquipmentResponse::id,
                EquipmentHelper::assertEquipmentResponse);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getEquipmentById() throws Exception {
        Equipment equipment = equipmentRepository.save(EquipmentHelper.createEquipment());

        EquipmentResponse response = performGet(
                BASE_URL + "/" + equipment.getId(),
                new TypeReference<>() {
                },
                HttpStatus.OK);

        EquipmentHelper.assertEquipmentResponse(equipment, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createEquipment() throws Exception {
        EquipmentInputRequest request = EquipmentHelper.createInputRequest();

        EquipmentResponse response = performMultipartPost(
                BASE_URL,
                request,
                MediaHelper.createMockImage("image/jpeg", 1024),
                new TypeReference<>() {
                },
                HttpStatus.CREATED);


        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Equipment equipment = equipmentRepository.getByIdOrThrow(response.id());
        EquipmentHelper.assertInputRequestToEntity(request, equipment);
        EquipmentHelper.assertEquipmentResponse(equipment, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateEquipment() throws Exception {
        Equipment equipment = equipmentRepository.save(EquipmentHelper.createEquipment());

        EquipmentInputRequest request = EquipmentHelper.createInputRequest();

        EquipmentResponse response = performMultipartPut(
                BASE_URL + "/" + equipment.getId(),
                request,
                MediaHelper.createMockImage("image/jpeg", 1024),
                new TypeReference<>() {
                },
                HttpStatus.OK);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        equipment = equipmentRepository.getByIdOrThrow(response.id());
        EquipmentHelper.assertInputRequestToEntity(request, equipment);
        EquipmentHelper.assertEquipmentResponse(equipment, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteEquipment() throws Exception {
        Equipment equipment = equipmentRepository.save(EquipmentHelper.createEquipment());

        performDeleteNoResponse(BASE_URL + "/" + equipment.getId(), HttpStatus.NO_CONTENT);

        equipment = equipmentRepository.findById(equipment.getId()).orElseThrow();
        Assertions.assertTrue(equipment.isDeleted());
    }

}
