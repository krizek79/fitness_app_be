package sk.krizan.fitness_app_be.model.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;


@Builder
public record MetaUser(
    String id,
    @JsonProperty("first_name")
    String firstName,
    @JsonProperty("last_name")
    String lastName,
    String email,
    MetaPicture picture
) {
}
