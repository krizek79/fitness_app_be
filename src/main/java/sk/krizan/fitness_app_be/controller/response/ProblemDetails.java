package sk.krizan.fitness_app_be.controller.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProblemDetails(
        String type,
        String title,
        int status,
        String detail,
        String instance,
        LocalDateTime timestamp,
        String traceId,
        String errorId,
        Object errors
) {
}