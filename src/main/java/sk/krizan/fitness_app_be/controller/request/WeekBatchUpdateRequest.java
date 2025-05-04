package sk.krizan.fitness_app_be.controller.request;

import lombok.Builder;

import java.util.List;

@Builder
public record WeekBatchUpdateRequest(
        List<WeekUpdateRequest> updateRequestList
) {
}