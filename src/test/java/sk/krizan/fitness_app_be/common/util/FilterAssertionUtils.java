package sk.krizan.fitness_app_be.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Utility class for common pagination/filter response assertions.
 * Eliminates boilerplate code from domain-specific helper classes.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilterAssertionUtils {

    /**
     * Assert that a PageResponse has valid structure:
     * - Not null
     * - All pagination fields are present
     * - Results list is not empty
     * - Page number matches requested page
     *
     * @param response PageResponse to validate
     * @param expectedPageNumber expected page number from request
     * @param expectedSize expected size of current page
     * @param <T> type of response objects
     */
    public static <T> void assertPageResponse(
            PageResponse<T> response,
            Integer expectedPageNumber,
            Integer expectedSize
    ) {
        Assertions.assertNotNull(response, "PageResponse is null");
        Assertions.assertNotNull(response.getPageNumber(), "Page number is null");
        Assertions.assertNotNull(response.getPageSize(), "Page size is null");
        Assertions.assertNotNull(response.getTotalElements(), "Total elements is null");
        Assertions.assertNotNull(response.getTotalPages(), "Total pages is null");
        Assertions.assertNotNull(response.getResults(), "Results list is null");
        Assertions.assertFalse(response.getResults().isEmpty(), "Results list is empty");
        Assertions.assertEquals(expectedPageNumber, response.getPageNumber(),
                "Page number mismatch");
        Assertions.assertEquals(expectedSize, response.getResults().size(),
                "Page size mismatch");
    }

    /**
     * Generic assertion for filter results with custom entity-to-response mapper.
     * <p>
     * Handles:
     * - PageResponse structure validation
     * - Entity and response list sorting
     * - Size matching
     * - Individual entity comparison via provided consumer
     *
     * @param expectedEntities sorted list of expected entities
     * @param response PageResponse from API
     * @param entityIdExtractor function to get ID from entity object (for sorting)
     * @param responseIdExtractor function to get ID from response object (for sorting)
     * @param entityResponseAsserter BiConsumer that asserts entity vs response equality
     * @param <E> entity type
     * @param <R> response type
     */
    public static <E, R> void assertFilterResults(
            List<E> expectedEntities,
            PageResponse<R> response,
            FilterComparator<E> entityIdExtractor,
            FilterComparator<R> responseIdExtractor,
            BiConsumer<E, R> entityResponseAsserter
    ) {
        assertPageResponse(response, 0, expectedEntities.size());

        // Sort both lists for consistent comparison
        List<E> sortedEntities = expectedEntities.stream()
                .sorted(Comparator.comparingLong(entityIdExtractor::getId))
                .toList();

        List<R> sortedResponses = response.getResults().stream()
                .sorted(Comparator.comparingLong(responseIdExtractor::getId))
                .toList();

        // Assert each entity matches corresponding response
        for (int i = 0; i < sortedEntities.size(); i++) {
            E entity = sortedEntities.get(i);
            R responseItem = sortedResponses.get(i);
            entityResponseAsserter.accept(entity, responseItem);
        }
    }

    /**
     * Functional interface for extracting comparable ID from entities or responses.
     * Used for sorting during filter assertions.
     */
    @FunctionalInterface
    public interface FilterComparator<T> {
        long getId(T item);
    }

}


