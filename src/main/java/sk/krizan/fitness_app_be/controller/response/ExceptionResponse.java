package sk.krizan.fitness_app_be.controller.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ExceptionResponse(
        LocalDateTime timestamp,
        String message,
        Object detail
) {
}