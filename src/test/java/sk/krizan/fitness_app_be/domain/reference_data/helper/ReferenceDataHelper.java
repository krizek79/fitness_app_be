package sk.krizan.fitness_app_be.domain.reference_data.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;
import sk.krizan.fitness_app_be.domain.reference.entity.BaseEnum;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReferenceDataHelper {

    public static <E extends Enum<E> & BaseEnum> void assertReferenceDataResponsesMatch(Class<E> enumClass, List<ReferenceDataResponse> responseList) {
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

    public static void assertReferenceDataResponse(BaseEnum baseEnum, ReferenceDataResponse referenceDataResponse) {
        Assertions.assertEquals(baseEnum.getKey(), referenceDataResponse.key());
        Assertions.assertEquals(baseEnum.getValue(), referenceDataResponse.value());
    }
}
