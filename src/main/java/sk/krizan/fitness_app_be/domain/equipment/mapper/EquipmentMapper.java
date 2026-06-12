package sk.krizan.fitness_app_be.domain.equipment.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.equipment.entity.Equipment;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.request.EquipmentInputRequest;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.response.EquipmentResponse;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EquipmentMapper {

    public static EquipmentResponse entityToResponse(Equipment entity) {
        return EquipmentResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .thumbnailUrl(entity.getThumbnailUrl())
                .build();
    }

    public static Equipment inputRequestToEntity(Equipment equipment, EquipmentInputRequest request) {
        if (equipment == null) {
            equipment = new Equipment();
        }

        equipment.setTitle(request.title());

        return equipment;
    }

}
