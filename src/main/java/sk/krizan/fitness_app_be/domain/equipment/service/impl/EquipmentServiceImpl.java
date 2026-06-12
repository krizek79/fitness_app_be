package sk.krizan.fitness_app_be.domain.equipment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.PageUtils;
import sk.krizan.fitness_app_be.domain.equipment.entity.Equipment;
import sk.krizan.fitness_app_be.domain.equipment.mapper.EquipmentMapper;
import sk.krizan.fitness_app_be.domain.equipment.repository.EquipmentRepository;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.request.EquipmentFilterRequest;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.request.EquipmentInputRequest;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.response.EquipmentResponse;
import sk.krizan.fitness_app_be.domain.equipment.service.api.EquipmentService;
import sk.krizan.fitness_app_be.domain.equipment.specification.EquipmentSpecification;
import sk.krizan.fitness_app_be.domain.media.MediaService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final MediaService mediaService;

    private static final List<String> supportedSortFields = List.of(
            Equipment.Fields.id,
            Equipment.Fields.title
    );

    @Override
    @Transactional(readOnly = true)
    public PageResponse<EquipmentResponse> filterEquipment(EquipmentFilterRequest request) {
        Specification<Equipment> specification = EquipmentSpecification.filter(request);
        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );
        Page<Equipment> page = equipmentRepository.findAll(specification, pageable);
        List<EquipmentResponse> responseList = page.stream()
                .map(EquipmentMapper::entityToResponse).collect(Collectors.toList());

        return PageResponse.<EquipmentResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .results(responseList)
                .build();
    }

    @Override
    public Equipment getEquipmentById(Long id) {
        return equipmentRepository.getByIdOrThrow(id);
    }

    @Override
    public Equipment createUpdateEquipment(Long id, EquipmentInputRequest request, MultipartFile thumbnail) {
        Equipment equipment = id == null ? null : getEquipmentById(id);
        equipment = EquipmentMapper.inputRequestToEntity(equipment, request);

        equipment = equipmentRepository.save(equipment);

        if (thumbnail != null && !thumbnail.isEmpty()) {
            String thumbnailUrl = mediaService.uploadFile(thumbnail, "requiredEquipment-" + equipment.getId());
            equipment.setThumbnailUrl(thumbnailUrl);
            equipment = equipmentRepository.save(equipment);
        }

        return equipment;
    }

    @Override
    public void deleteEquipment(Long id) {
        Equipment equipment = getEquipmentById(id);
        equipment.setDeleted(true);
        equipmentRepository.save(equipment);
    }

}




