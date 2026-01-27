package sk.krizan.fitness_app_be.controller.request.common;

import lombok.Builder;

import java.util.List;

@Builder
public record BatchUpdateRequest<T>(
        List<T> updateRequestList
) {
}