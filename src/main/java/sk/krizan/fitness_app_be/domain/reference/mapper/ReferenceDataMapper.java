package sk.krizan.fitness_app_be.domain.reference.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.reference.entity.BaseEnum;
import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReferenceDataMapper {

    public static ReferenceDataResponse enumToResponse(BaseEnum baseEnum) {
        return ReferenceDataResponse.builder()
                .key(baseEnum.getKey())
                .value(baseEnum.getValue())
                .build();
    }
}
