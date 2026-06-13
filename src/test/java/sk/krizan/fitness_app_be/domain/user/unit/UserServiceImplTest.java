package sk.krizan.fitness_app_be.domain.user.unit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.reference.entity.WeightUnit;
import sk.krizan.fitness_app_be.domain.user.entity.Role;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.repository.UserRepository;
import sk.krizan.fitness_app_be.domain.user.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private MockedStatic<SecurityContextHolder> mockedSecurityContextHolder;
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class);
        securityContext = mock(SecurityContext.class);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
    }

    @AfterEach
    void tearDown() {
        mockedSecurityContextHolder.close();
    }

    @Nested
    class GetOrCreateCurrentUserTests {

        @Test
        void shouldReturnExistingUser_WhenUserExistsInDb() {
            String keycloakId = "kc-user-123";
            Jwt jwt = mock(Jwt.class);
            when(jwt.getSubject()).thenReturn(keycloakId);

            JwtAuthenticationToken authentication = mock(JwtAuthenticationToken.class);
            when(authentication.getToken()).thenReturn(jwt);
            when(securityContext.getAuthentication()).thenReturn(authentication);

            User expectedUser = new User();
            expectedUser.setKeycloakId(keycloakId);
            expectedUser.setEmail("test@fitness.sk");

            when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(expectedUser));

            User result = userService.getOrCreateCurrentUser();

            assertNotNull(result);
            assertEquals(keycloakId, result.getKeycloakId());
            verify(userRepository, times(1)).findByKeycloakId(keycloakId);
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        void shouldCreateAndReturnNewUser_WhenUserDoesNotExistInDb() {
            String keycloakId = "kc-new-456";
            String email = "newuser@fitness.sk";
            String name = "Ján Novák";
            String picture = "http://photo.com/me.png";

            Jwt jwt = mock(Jwt.class);
            when(jwt.getSubject()).thenReturn(keycloakId);
            when(jwt.getClaimAsString("email")).thenReturn(email);
            when(jwt.getClaimAsString("title")).thenReturn(name);
            when(jwt.getClaimAsString("picture")).thenReturn(picture);

            JwtAuthenticationToken authentication = mock(JwtAuthenticationToken.class);
            when(authentication.getToken()).thenReturn(jwt);
            when(securityContext.getAuthentication()).thenReturn(authentication);

            when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.empty());

            when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            User result = userService.getOrCreateCurrentUser();

            assertNotNull(result);
            assertEquals(keycloakId, result.getKeycloakId());
            assertEquals(email, result.getEmail());

            assertNotNull(result.getProfile());
            assertEquals(name, result.getProfile().getName());
            assertEquals(picture, result.getProfile().getProfilePictureUrl());
            assertEquals(WeightUnit.KG, result.getProfile().getPreferredWeightUnit());
            assertEquals(result, result.getProfile().getUser());

            verify(userRepository, times(1)).findByKeycloakId(keycloakId);
            verify(userRepository, times(1)).save(any(User.class));
        }

        @Test
        void shouldThrowApplicationException_WhenAuthenticationIsNotJwt() {
            Authentication wrongAuthentication = mock(Authentication.class);
            when(securityContext.getAuthentication()).thenReturn(wrongAuthentication);

            ApplicationException exception = assertThrows(ApplicationException.class, userService::getOrCreateCurrentUser);

            assertEquals(HttpStatus.UNAUTHORIZED, exception.getHttpStatus());
            assertEquals("Unrecognized authentication principal", exception.getMessage());
            verifyNoInteractions(userRepository);
        }
    }

    @Nested
    class IsUserAdminTests {

        @Test
        void shouldReturnTrue_WhenUserHasAdminRole() {
            Authentication authentication = mock(Authentication.class);
            GrantedAuthority adminAuthority = new SimpleGrantedAuthority("ROLE_" + Role.ADMIN.name());

            doReturn(List.of(adminAuthority)).when(authentication).getAuthorities();
            when(securityContext.getAuthentication()).thenReturn(authentication);

            User user = new User();

            boolean isAdmin = userService.isUserAdmin(user);

            assertTrue(isAdmin);
        }

        @Test
        void shouldReturnFalse_WhenUserDoesNotHaveAdminRole() {
            Authentication authentication = mock(Authentication.class);
            GrantedAuthority userAuthority = new SimpleGrantedAuthority("ROLE_USER");

            doReturn(List.of(userAuthority)).when(authentication).getAuthorities();
            when(securityContext.getAuthentication()).thenReturn(authentication);

            User user = new User();

            boolean isAdmin = userService.isUserAdmin(user);

            assertFalse(isAdmin);
        }
    }

}
