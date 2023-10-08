package sk.krizan.fitness_app_be.util;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import sk.krizan.fitness_app_be.controller.exception.IllegalOperationException;

public class PageUtils {

    private static final String DIR_ASC = "ASC";

    private static final String ERROR_UNSOPPORTED_SORT_FIELD = "Unsupported sort field { %s }.";

    public static Pageable createPageable(
        Integer pageNumber,
        Integer pageSize,
        String sortBy,
        String sortDirection,
        List<String> supportedSortFields
    ) {
        if (!supportedSortFields.contains(sortBy)) {
            throw new IllegalOperationException(ERROR_UNSOPPORTED_SORT_FIELD.formatted(sortBy));
        }

        return PageRequest.of(
            pageNumber,
            pageSize,
            Sort.by(
                sortDirection.equalsIgnoreCase(DIR_ASC) ? Sort.Direction.ASC : Sort.Direction.DESC,
                sortBy));
    }
}
