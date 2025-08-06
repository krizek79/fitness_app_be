package sk.krizan.fitness_app_be.controller.endpoint;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.endpoint.impl.UserController;
import sk.krizan.fitness_app_be.controller.response.UserResponse;
import sk.krizan.fitness_app_be.helper.SecurityHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.UserRepository;

import java.util.Set;

@Transactional
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserController userController;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = userRepository.save(UserHelper.createMockUser(Set.of(Role.ADMIN)));
        SecurityHelper.setAuthentication(mockUser);
    }

    @Test
    void getUserById() {
        UserResponse userResponse = userController.getUserById(mockUser.getId());
        UserHelper.assertUserResponse(mockUser, userResponse);
    }

    @Test
    void getUserById_shouldThrowForbidden() {
        mockUser.getRoleSet().clear();
        mockUser.getRoleSet().add(Role.USER);
        mockUser = userRepository.save(mockUser);
        SecurityHelper.setAuthentication(mockUser);

        Assertions.assertThrows(AccessDeniedException.class, () -> userController.getUserById(mockUser.getId()), "");
    }
}
