package sk.krizan.fitness_app_be.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import sk.krizan.fitness_app_be.controller.exception.ApplicationException;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private final String KEYCLOAK_ID = "test-kc-id";

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setKeycloakId(KEYCLOAK_ID);
        testUser.setEmail("test@example.com");
        testUser.addToRoleSet(new HashSet<>(Set.of(Role.USER)));
    }

    // --- Tests for syncUser ---

    @Test
    void syncUser_ShouldCreateNewUser_WhenUserDoesNotExist() {
        when(jwt.getSubject()).thenReturn(KEYCLOAK_ID);

        when(jwt.getClaimAsString("picture")).thenReturn("http://image.com/avatar.png");
        when(jwt.getClaimAsString("email")).thenReturn("new@example.com");
        when(jwt.getClaimAsString("name")).thenReturn("John Doe");

        when(userRepository.findByKeycloakId(KEYCLOAK_ID)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = userService.syncUser(jwt, Set.of(Role.USER));

        assertNotNull(result);
        assertEquals(KEYCLOAK_ID, result.getKeycloakId());
        assertEquals("new@example.com", result.getEmail());
        assertNotNull(result.getProfile());
        assertEquals("John Doe", result.getProfile().getName());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void syncUser_ShouldUpdateRoles_WhenRolesHaveChanged() {
        Set<Role> newRoles = Set.of(Role.USER, Role.ADMIN);
        when(jwt.getSubject()).thenReturn(KEYCLOAK_ID);
        when(userRepository.findByKeycloakId(KEYCLOAK_ID)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.syncUser(jwt, newRoles);

        assertTrue(result.getRoleSet().contains(Role.ADMIN));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void syncUser_ShouldNotSave_WhenRolesAreSame() {
        Set<Role> sameRoles = Set.of(Role.USER);
        when(jwt.getSubject()).thenReturn(KEYCLOAK_ID);
        when(userRepository.findByKeycloakId(KEYCLOAK_ID)).thenReturn(Optional.of(testUser));

        User result = userService.syncUser(jwt, sameRoles);

        verify(userRepository, never()).save(any(User.class));
        assertEquals(testUser, result);
    }

    // --- Tests for getCurrentUser ---

    @Test
    void getCurrentUser_ShouldReturnUser_WhenAuthenticated() {
        JwtAuthenticationToken auth = mock(JwtAuthenticationToken.class);
        when(auth.getToken()).thenReturn(jwt);
        when(jwt.getSubject()).thenReturn(KEYCLOAK_ID);
        when(userRepository.findByKeycloakId(KEYCLOAK_ID)).thenReturn(Optional.of(testUser));

        SecurityContextHolder.getContext().setAuthentication(auth);

        User result = userService.getCurrentUser();

        assertEquals(testUser, result);
    }

    @Test
    void getCurrentUser_ShouldThrowException_WhenUserNotFoundInDb() {
        JwtAuthenticationToken auth = mock(JwtAuthenticationToken.class);
        when(auth.getToken()).thenReturn(jwt);
        when(jwt.getSubject()).thenReturn(KEYCLOAK_ID);
        when(userRepository.findByKeycloakId(KEYCLOAK_ID)).thenReturn(Optional.empty());

        SecurityContextHolder.getContext().setAuthentication(auth);

        ApplicationException ex = assertThrows(ApplicationException.class, () -> userService.getCurrentUser());
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
    }

    // --- Tests for getUserById ---

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User result = userService.getUserById(1L);

        assertEquals(testUser, result);
    }

    @Test
    void getUserById_ShouldThrowNotFound_WhenDoesNotExist() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ApplicationException.class, () -> userService.getUserById(99L));
    }
}