package sk.krizan.fitness_app_be.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.controller.response.EnumResponse;
import sk.krizan.fitness_app_be.model.enums.BaseEnum;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumHelper {

    public static <E extends Enum<E> & BaseEnum> void assertEnumResponsesMatch(Class<E> enumClass, List<EnumResponse> responseList) {
        Assertions.assertNotNull(responseList, "Response list is null");
        Assertions.assertEquals(enumClass.getEnumConstants().length, responseList.size(), "Enum size mismatch");

        for (E enumConstant : enumClass.getEnumConstants()) {
            boolean found = responseList.stream().anyMatch(response ->
                    response.key().equals(enumConstant.getKey()) &&
                            response.value().equals(enumConstant.getValue())
            );
            Assertions.assertTrue(found, "Missing or incorrect enum response: " + enumConstant.name());
        }
    }

    public static void assertEnumResponse(BaseEnum baseEnum, EnumResponse enumResponse) {
        Assertions.assertEquals(baseEnum.getKey(), enumResponse.key());
        Assertions.assertEquals(baseEnum.getValue(), enumResponse.value());
    }
}
