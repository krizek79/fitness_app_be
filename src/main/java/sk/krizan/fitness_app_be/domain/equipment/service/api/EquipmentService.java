package sk.krizan.fitness_app_be.domain.equipment.service.api;

import org.springframework.web.multipart.MultipartFile;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.request.EquipmentFilterRequest;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.request.EquipmentInputRequest;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.response.EquipmentResponse;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.equipment.entity.Equipment;

public interface EquipmentService {

    PageResponse<EquipmentResponse> filterEquipment(EquipmentFilterRequest request);

    Equipment getEquipmentById(Long id);

    Equipment createUpdateEquipment(Long id, EquipmentInputRequest request, MultipartFile thumbnail);

    void deleteEquipment(Long id);

}

