package sk.krizan.fitness_app_be.controller.response;

import java.util.List;

import lombok.Builder;

@Builder
public record PageResponse<T>(
        Integer pageNumber,
        Integer pageSize,
        Integer totalPages,
        Long totalElements,
        List<T> results
) {
}
