package sk.krizan.fitness_app_be.domain.equipment.helper;

import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.domain.equipment.entity.Equipment;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.request.EquipmentFilterRequest;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.request.EquipmentInputRequest;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.response.EquipmentResponse;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EquipmentHelper {

    private static final Faker faker = new Faker();

    public static Equipment createEquipment() {
        Equipment equipment = new Equipment();
        equipment.setTitle(UUID.randomUUID().toString());
        equipment.setThumbnailUrl(faker.internet().image());

        return equipment;
    }

    public static EquipmentFilterRequest createFilterRequest(
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection,
            String title
    ) {
        return EquipmentFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .title(title)
                .build();
    }

    public static EquipmentInputRequest createInputRequest() {
        return EquipmentInputRequest.builder()
                .title(UUID.randomUUID().toString())
                .build();
    }

    public static void assertEquipmentResponse(Equipment equipment, EquipmentResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertEquals(equipment.getId(), response.id());
        Assertions.assertNotNull(response.title());
        Assertions.assertEquals(equipment.getTitle(), response.title());
        Assertions.assertEquals(equipment.getThumbnailUrl(), response.thumbnailUrl());
    }

    public static void assertInputRequestToEntity(EquipmentInputRequest request, Equipment equipment) {
        Assertions.assertNotNull(equipment);
        Assertions.assertEquals(request.title(), equipment.getTitle());
        Assertions.assertFalse(equipment.isDeleted());
    }

}
