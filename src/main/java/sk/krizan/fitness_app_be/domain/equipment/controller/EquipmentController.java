package sk.krizan.fitness_app_be.domain.equipment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.request.EquipmentFilterRequest;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.request.EquipmentInputRequest;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.response.EquipmentResponse;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.equipment.mapper.EquipmentMapper;
import sk.krizan.fitness_app_be.domain.equipment.service.api.EquipmentService;

@RestController
@RequiredArgsConstructor
public class EquipmentController implements sk.krizan.fitness_app_be.domain.equipment.rest.api.EquipmentController {

    private final EquipmentService equipmentService;

    @Override
    public PageResponse<EquipmentResponse> filterEquipment(EquipmentFilterRequest request) {
        return equipmentService.filterEquipment(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Override
    public EquipmentResponse createEquipment(EquipmentInputRequest request, MultipartFile thumbnail) {
        return EquipmentMapper.entityToResponse(equipmentService.createUpdateEquipment(null, request, thumbnail));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Override
    public EquipmentResponse updateEquipment(Long id, EquipmentInputRequest request, MultipartFile thumbnail) {
        return EquipmentMapper.entityToResponse(equipmentService.createUpdateEquipment(id, request, thumbnail));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Override
    public void deleteEquipment(Long id) {
        equipmentService.deleteEquipment(id);
    }

}

