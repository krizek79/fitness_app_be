package sk.krizan.fitness_app_be.domain.user.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import sk.krizan.fitness_app_be.common.BaseIntegrationTest;
import sk.krizan.fitness_app_be.domain.user.entity.Role;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.helper.UserHelper;
import sk.krizan.fitness_app_be.domain.user.repository.UserRepository;
import sk.krizan.fitness_app_be.domain.user.rest.dto.response.UserResponse;

import java.util.Set;

public class UserIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    private User mockUser;

    private static final String BASE_URL = "/users";

    @BeforeEach
    void setUp() {
        mockUser = userRepository.save(UserHelper.createUser(Set.of(Role.ADMIN)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserById() throws Exception {
        UserResponse response = performGet(
                BASE_URL + "/" + mockUser.getId(),
                new TypeReference<>() {
                },
                HttpStatus.OK);

        UserHelper.assertUserResponse(mockUser, response);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getUserById_shouldThrowForbidden() throws Exception {
        performGet(
                BASE_URL + "/" + mockUser.getId(),
                new TypeReference<>() {
                },
                HttpStatus.FORBIDDEN);
    }

}
