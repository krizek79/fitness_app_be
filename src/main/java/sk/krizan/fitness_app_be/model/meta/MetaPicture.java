package sk.krizan.fitness_app_be.model.meta;

import lombok.Builder;

@Builder
public record MetaPicture(
    PictureData data
) {
}