package sk.krizan.fitness_app_be.controller.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ExceptionResponse(
    LocalDateTime timestamp,
    String message
) {
}