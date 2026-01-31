package sk.krizan.fitness_app_be.common.rest.dto.request;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@Builder
@FieldNameConstants
public record BatchUpdateRequest<T>(
        List<T> updateRequestList
) {
}