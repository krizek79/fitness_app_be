package sk.krizan.fitness_app_be.common.util;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DefaultValues {

    public static final String DEFAULT_VALUE = "default";
    public static final String DEFAULT_UPDATE_VALUE = "update";
}
