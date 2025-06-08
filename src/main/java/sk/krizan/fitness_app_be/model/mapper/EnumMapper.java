package sk.krizan.fitness_app_be.model.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.response.EnumResponse;
import sk.krizan.fitness_app_be.model.enums.BaseEnum;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumMapper {

    public static EnumResponse enumToResponse(BaseEnum baseEnum) {
        return EnumResponse.builder()
                .key(baseEnum.getKey())
                .value(baseEnum.getValue())
                .build();
    }
}
