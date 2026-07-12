package sk.krizan.fitness_app_be.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.common.helper.MockMvcHelper;

/**
 * Base class for integration tests using MockMvc.
 * Provides common setup and helper methods for REST API testing.
 * <p>
 * Usage:
 * {@code
 * class MyControllerTest extends BaseIntegrationTest {
 * // Test methods
 * }
 * }
 */
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Perform a POST request with custom status and return parsed response
     *
     * @param endpoint       request endpoint path
     * @param requestBody    request payload
     * @param responseType   expected response type
     * @param expectedStatus expected HTTP status code
     * @return parsed response object
     * @throws Exception if request fails
     */
    protected <REQUEST, RESPONSE> RESPONSE performPost(
            String endpoint,
            REQUEST requestBody,
            TypeReference<RESPONSE> responseType,
            HttpStatus expectedStatus
    ) throws Exception {
        return MockMvcHelper.performPost(
                mockMvc,
                objectMapper,
                endpoint,
                requestBody,
                responseType,
                expectedStatus);
    }

    /**
     * Performs a multipart POST request using MockMvc and returns the parsed response.
     *
     * @param endpoint       target endpoint path
     * @param requestBody    request body object sent as JSON multipart part
     * @param file           optional multipart file (can be null)
     * @param responseType   expected response type for JSON deserialization
     * @param expectedStatus expected HTTP status of the response
     * @return deserialized response object
     * @throws Exception if request execution or parsing fails
     */
    protected <REQUEST, RESPONSE> RESPONSE performMultipartPost(
            String endpoint,
            REQUEST requestBody,
            MockMultipartFile file,
            TypeReference<RESPONSE> responseType,
            HttpStatus expectedStatus
    ) throws Exception {
        return MockMvcHelper.performMultipartPost(
                mockMvc,
                objectMapper,
                endpoint,
                requestBody,
                file,
                responseType,
                expectedStatus
        );
    }

    /**
     * Perform a PUT request with custom status and return parsed response
     *
     * @param endpoint       request endpoint path
     * @param requestBody    request payload
     * @param responseType   expected response type
     * @param expectedStatus expected HTTP status code
     * @return parsed response object
     * @throws Exception if request fails
     */
    protected <REQUEST, RESPONSE> RESPONSE performPut(
            String endpoint,
            REQUEST requestBody,
            TypeReference<RESPONSE> responseType,
            HttpStatus expectedStatus
    ) throws Exception {
        return MockMvcHelper.performPut(
                mockMvc,
                objectMapper,
                endpoint,
                requestBody,
                responseType,
                expectedStatus);
    }

    /**
     * Performs a multipart PUT request using MockMvc and returns the parsed response.
     *
     * @param endpoint       target endpoint path
     * @param requestBody    request body object sent as JSON multipart part
     * @param file           optional multipart file (can be null)
     * @param responseType   expected response type for JSON deserialization
     * @param expectedStatus expected HTTP status of the response
     * @return deserialized response object
     * @throws Exception if request execution or parsing fails
     */
    protected <REQUEST, RESPONSE> RESPONSE performMultipartPut(
            String endpoint,
            REQUEST requestBody,
            MockMultipartFile file,
            TypeReference<RESPONSE> responseType,
            HttpStatus expectedStatus
    ) throws Exception {
        return MockMvcHelper.performMultipartPut(
                mockMvc,
                objectMapper,
                endpoint,
                requestBody,
                file,
                responseType,
                expectedStatus
        );
    }

    /**
     * Perform a PATCH request with custom status and return parsed response
     *
     * @param endpoint       request endpoint path
     * @param requestBody    request payload
     * @param responseType   expected response type
     * @param expectedStatus expected HTTP status code
     * @return parsed response object
     * @throws Exception if request fails
     */
    protected <REQUEST, RESPONSE> RESPONSE performPatch(
            String endpoint,
            REQUEST requestBody,
            TypeReference<RESPONSE> responseType,
            HttpStatus expectedStatus
    ) throws Exception {
        return MockMvcHelper.performPatch(
                mockMvc,
                objectMapper,
                endpoint,
                requestBody,
                responseType,
                expectedStatus);
    }

    /**
     * Perform a GET request with custom status and return parsed response
     *
     * @param endpoint       request endpoint path
     * @param responseType   expected response type
     * @param expectedStatus expected HTTP status code
     * @return parsed response object
     * @throws Exception if request fails
     */
    protected <RESPONSE> RESPONSE performGet(
            String endpoint,
            TypeReference<RESPONSE> responseType,
            HttpStatus expectedStatus
    ) throws Exception {
        return MockMvcHelper.performGet(
                mockMvc,
                objectMapper,
                endpoint,
                responseType,
                expectedStatus);
    }

    /**
     * Perform a DELETE request without expecting response body (e.g., 204 No Content, 401, 403)
     *
     * @param endpoint       request endpoint path
     * @param expectedStatus expected HTTP status code
     * @throws Exception if request fails
     */
    protected void performDeleteNoResponse(
            String endpoint,
            HttpStatus expectedStatus
    ) throws Exception {
        MockMvcHelper.performDeleteNoResponse(
                mockMvc,
                endpoint,
                expectedStatus);
    }

}

