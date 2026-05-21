package sk.krizan.fitness_app_be.common.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.function.Function;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Utility class for common MockMvc HTTP testing patterns.
 * Eliminates boilerplate code from integration tests.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MockMvcHelper {

    // ===== GET =====

    /**
     * Perform a GET request and parse response as JSON
     *
     * @param mockMvc MockMvc instance
     * @param objectMapper ObjectMapper for JSON parsing
     * @param endpoint request endpoint
     * @param responseType response type for deserialization
     * @param expectedStatus expected HTTP status
     * @return parsed response object
     * @throws Exception if request fails
     */
    public static <RESPONSE> RESPONSE performGet(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            String endpoint,
            TypeReference<RESPONSE> responseType,
            HttpStatus expectedStatus
    ) throws Exception {
        MvcResult result = mockMvc.perform(
                get(endpoint)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(expectedStatus.value()))
         .andReturn();

        return parseResponse(objectMapper, result, responseType);
    }

    /**
     * Perform a GET request without expecting a response body (e.g., 401, 403, 204)
     *
     * @param mockMvc MockMvc instance
     * @param endpoint request endpoint
     * @param expectedStatus expected HTTP status
     * @throws Exception if request fails
     */
    public static void performGetNoResponse(
            MockMvc mockMvc,
            String endpoint,
            HttpStatus expectedStatus
    ) throws Exception {
        mockMvc.perform(
                get(endpoint)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(expectedStatus.value()))
         .andReturn();
    }

    // ===== POST =====

    /**
     * Perform a POST request and parse response as JSON
     *
     * @param mockMvc MockMvc instance
     * @param objectMapper ObjectMapper for JSON parsing
     * @param endpoint request endpoint
     * @param requestBody request body object (will be serialized to JSON)
     * @param responseType response type for deserialization
     * @param expectedStatus expected HTTP status (e.g., 200, 201, 400)
     * @return parsed response object
     * @throws Exception if request fails
     */
    public static <REQUEST, RESPONSE> RESPONSE performPost(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            String endpoint,
            REQUEST requestBody,
            TypeReference<RESPONSE> responseType,
            HttpStatus expectedStatus
    ) throws Exception {
        MvcResult result = mockMvc.perform(
                post(endpoint)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
        ).andExpect(status().is(expectedStatus.value()))
         .andReturn();

        return parseResponse(objectMapper, result, responseType);
    }

    /**
     * Perform a POST request that returns plain text (e.g., Long ID)
     *
     * @param mockMvc MockMvc instance
     * @param endpoint request endpoint
     * @param requestBody request body object (will be serialized to JSON)
     * @param responseParser function to parse response string
     * @param expectedStatus expected HTTP status
     * @return parsed response
     * @throws Exception if request fails
     */
    public static <REQUEST, RESPONSE> RESPONSE performPostPlainText(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            String endpoint,
            REQUEST requestBody,
            Function<String, RESPONSE> responseParser,
            HttpStatus expectedStatus
    ) throws Exception {
        MvcResult result = mockMvc.perform(
                post(endpoint)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
        ).andExpect(status().is(expectedStatus.value()))
         .andReturn();

        String responseText = result.getResponse().getContentAsString();
        return responseParser.apply(responseText);
    }

    /**
     * Perform a POST request without expecting a response body (e.g., 401, 403, 204)
     *
     * @param mockMvc MockMvc instance
     * @param objectMapper ObjectMapper for JSON parsing
     * @param endpoint request endpoint
     * @param requestBody request body object (will be serialized to JSON)
     * @param expectedStatus expected HTTP status
     * @throws Exception if request fails
     */
    public static <REQUEST> void performPostNoResponse(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            String endpoint,
            REQUEST requestBody,
            HttpStatus expectedStatus
    ) throws Exception {
        mockMvc.perform(
                post(endpoint)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
        ).andExpect(status().is(expectedStatus.value()))
         .andReturn();
    }

    // ===== PUT =====

    /**
     * Perform a PUT request and parse response as JSON
     *
     * @param mockMvc MockMvc instance
     * @param objectMapper ObjectMapper for JSON parsing
     * @param endpoint request endpoint
     * @param requestBody request body object (will be serialized to JSON)
     * @param responseType response type for deserialization
     * @param expectedStatus expected HTTP status
     * @return parsed response object
     * @throws Exception if request fails
     */
    public static <REQUEST, RESPONSE> RESPONSE performPut(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            String endpoint,
            REQUEST requestBody,
            TypeReference<RESPONSE> responseType,
            HttpStatus expectedStatus
    ) throws Exception {
        MvcResult result = mockMvc.perform(
                put(endpoint)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
        ).andExpect(status().is(expectedStatus.value()))
         .andReturn();

        return parseResponse(objectMapper, result, responseType);
    }

    /**
     * Perform a PUT request without expecting a response body (e.g., 401, 403, 204)
     *
     * @param mockMvc MockMvc instance
     * @param objectMapper ObjectMapper for JSON parsing
     * @param endpoint request endpoint
     * @param requestBody request body object (will be serialized to JSON)
     * @param expectedStatus expected HTTP status
     * @throws Exception if request fails
     */
    public static <REQUEST> void performPutNoResponse(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            String endpoint,
            REQUEST requestBody,
            HttpStatus expectedStatus
    ) throws Exception {
        mockMvc.perform(
                put(endpoint)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
        ).andExpect(status().is(expectedStatus.value()))
         .andReturn();
    }

    // ===== DELETE =====

    /**
     * Perform a DELETE request and parse response as JSON
     *
     * @param mockMvc MockMvc instance
     * @param objectMapper ObjectMapper for JSON parsing
     * @param endpoint request endpoint
     * @param responseType response type for deserialization
     * @param expectedStatus expected HTTP status
     * @return parsed response object
     * @throws Exception if request fails
     */
    public static <RESPONSE> RESPONSE performDelete(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            String endpoint,
            TypeReference<RESPONSE> responseType,
            HttpStatus expectedStatus
    ) throws Exception {
        MvcResult result = mockMvc.perform(
                delete(endpoint)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(expectedStatus.value()))
         .andReturn();

        return parseResponse(objectMapper, result, responseType);
    }

    /**
     * Perform a DELETE request without expecting a response body (e.g., 204 No Content, 401, 403)
     *
     * @param mockMvc MockMvc instance
     * @param endpoint request endpoint
     * @param expectedStatus expected HTTP status
     * @throws Exception if request fails
     */
    public static void performDeleteNoResponse(
            MockMvc mockMvc,
            String endpoint,
            HttpStatus expectedStatus
    ) throws Exception {
        mockMvc.perform(
                delete(endpoint)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(expectedStatus.value()))
         .andReturn();
    }

    /**
     * Parse response MvcResult to typed object
     *
     * @param objectMapper ObjectMapper for deserialization
     * @param result MvcResult from MockMvc perform
     * @param responseType TypeReference for response object type
     * @return parsed response
     * @throws Exception if parsing fails
     */
    private static <RESPONSE> RESPONSE parseResponse(
            ObjectMapper objectMapper,
            MvcResult result,
            TypeReference<RESPONSE> responseType
    ) throws Exception {
        String jsonResponse = result.getResponse().getContentAsString();
        return objectMapper.readValue(jsonResponse, responseType);
    }

}

