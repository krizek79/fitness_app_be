package sk.krizan.fitness_app_be.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import sk.krizan.fitness_app_be.controller.exception.IllegalOperationException;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageUtils {

    private static final String DIR_ASC = "ASC";

    private static final String ERROR_UNSUPPORTED_SORT_FIELD = "Unsupported sort field { %s }.";

    public static Pageable createPageable(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDirection,
            List<String> supportedSortFields
    ) {
        if (!supportedSortFields.contains(sortBy)) {
            throw new IllegalOperationException(ERROR_UNSUPPORTED_SORT_FIELD.formatted(sortBy));
        }

        Sort sort = Sort.by(new Sort.Order(sortDirection.equalsIgnoreCase(DIR_ASC) ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy).with(Sort.NullHandling.NULLS_LAST));
        return PageRequest.of(pageNumber, pageSize, sort);
    }
}
