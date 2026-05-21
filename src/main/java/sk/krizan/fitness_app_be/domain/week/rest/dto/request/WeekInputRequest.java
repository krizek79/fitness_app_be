package sk.krizan.fitness_app_be.domain.week.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.Length;
import sk.krizan.fitness_app_be.common.validation.group.CreateGroup;

@Builder
@FieldNameConstants
public record WeekInputRequest(

        @Schema(description = "Should be set only for update. For new object, this field should be null.")
        Long id,

        @Min(1)
        @NotNull
        Integer order,

        @Length(max = 1024)
        String note,

        @NotNull
        @Null(groups = CreateGroup.class)
        Boolean completed
) {
}
