package sk.krizan.fitness_app_be.common.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
     * @param mockMvc        MockMvc instance
     * @param objectMapper   ObjectMapper for JSON parsing
     * @param endpoint       request endpoint
     * @param responseType   response type for deserialization
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

    // ===== POST =====

    /**
     * Perform a POST request and parse response as JSON
     *
     * @param mockMvc        MockMvc instance
     * @param objectMapper   ObjectMapper for JSON parsing
     * @param endpoint       request endpoint
     * @param requestBody    request body object (will be serialized to JSON)
     * @param responseType   response type for deserialization
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
     * Performs a multipart POST request and returns the parsed JSON response.
     *
     * @param mockMvc        MockMvc instance used to execute the request
     * @param objectMapper   ObjectMapper used for JSON serialization/deserialization
     * @param endpoint       target endpoint path
     * @param requestBody    request body object serialized as JSON part
     * @param file           optional multipart file (can be null)
     * @param responseType   expected response type for JSON deserialization
     * @param expectedStatus expected HTTP status of the response
     * @return deserialized response object
     * @throws Exception if request execution or parsing fails
     */
    public static <REQUEST, RESPONSE> RESPONSE performMultipartPost(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            String endpoint,
            REQUEST requestBody,
            MockMultipartFile file,
            TypeReference<RESPONSE> responseType,
            HttpStatus expectedStatus
    ) throws Exception {
        return performMultipart(
                mockMvc,
                objectMapper,
                "POST",
                endpoint,
                requestBody,
                file,
                responseType,
                expectedStatus
        );
    }

    // ===== PUT =====

    /**
     * Perform a PUT request and parse response as JSON
     *
     * @param mockMvc        MockMvc instance
     * @param objectMapper   ObjectMapper for JSON parsing
     * @param endpoint       request endpoint
     * @param requestBody    request body object (will be serialized to JSON)
     * @param responseType   response type for deserialization
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
     * Performs a multipart PUT request and returns the parsed JSON response.
     *
     * @param mockMvc        MockMvc instance used to execute the request
     * @param objectMapper   ObjectMapper used for JSON serialization/deserialization
     * @param endpoint       target endpoint path
     * @param requestBody    request body object serialized as JSON part
     * @param file           optional multipart file (can be null)
     * @param responseType   expected response type for JSON deserialization
     * @param expectedStatus expected HTTP status of the response
     * @return deserialized response object
     * @throws Exception if request execution or parsing fails
     */
    public static <REQUEST, RESPONSE> RESPONSE performMultipartPut(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            String endpoint,
            REQUEST requestBody,
            MockMultipartFile file,
            TypeReference<RESPONSE> responseType,
            HttpStatus expectedStatus
    ) throws Exception {
        return performMultipart(
                mockMvc,
                objectMapper,
                "PUT",
                endpoint,
                requestBody,
                file,
                responseType,
                expectedStatus
        );
    }

    // ===== DELETE =====

    /**
     * Perform a DELETE request without expecting a response body (e.g., 204 No Content, 401, 403)
     *
     * @param mockMvc        MockMvc instance
     * @param endpoint       request endpoint
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
     * @param result       MvcResult from MockMvc perform
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

    /**
     * Performs a multipart HTTP request (POST, PUT, etc.) and returns the parsed JSON response.
     *
     * @param mockMvc        MockMvc instance used to execute the request
     * @param objectMapper   ObjectMapper used for JSON serialization/deserialization
     * @param method         HTTP method to use (POST, PUT, etc.)
     * @param endpoint       target endpoint path
     * @param requestBody    request body object serialized as JSON part
     * @param file           optional multipart file (can be null)
     * @param responseType   expected response type for JSON deserialization
     * @param expectedStatus expected HTTP status of the response
     * @return deserialized response object
     * @throws Exception if request execution or parsing fails
     */
    private static <REQUEST, RESPONSE> RESPONSE performMultipart(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            String method,
            String endpoint,
            REQUEST requestBody,
            MockMultipartFile file,
            TypeReference<RESPONSE> responseType,
            HttpStatus expectedStatus
    ) throws Exception {

        MockMultipartFile requestPart = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(requestBody)
        );

        MockMultipartHttpServletRequestBuilder builder = multipart(endpoint)
                .file(requestPart);

        if (file != null) {
            builder.file(file);
        }

        builder.with(csrf());

        builder.with(request -> {
            request.setMethod(method);
            return request;
        });

        MvcResult result = mockMvc.perform(builder)
                .andExpect(status().is(expectedStatus.value()))
                .andReturn();

        return parseResponse(objectMapper, result, responseType);
    }

}

