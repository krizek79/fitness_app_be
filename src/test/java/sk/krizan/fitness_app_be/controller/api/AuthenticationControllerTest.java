package sk.krizan.fitness_app_be.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.request.LocalAuthenticationRequest;
import sk.krizan.fitness_app_be.controller.request.SignUpRequest;
import sk.krizan.fitness_app_be.controller.response.AuthenticationResponse;
import sk.krizan.fitness_app_be.helper.AuthenticationHelper;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sk.krizan.fitness_app_be.service.impl.AuthenticationServiceImpl.REGISTRATION_SUCCESSFUL;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private final Faker faker = new Faker();

    @Test
    void signInLocal() throws Exception {
        User user = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
        profileRepository.save(ProfileHelper.createMockProfile(user));

        LocalAuthenticationRequest request = LocalAuthenticationRequest.builder()
                .email(user.getEmail())
                .password("password")
                .build();

        MvcResult mvcResult = mockMvc.perform(
                        post("/auth/sign-in")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        AuthenticationResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        AuthenticationHelper.assertSignInLocal_success(user, response);
    }

    @Test
    void signUp_success() throws Exception {
        String password = UUID.randomUUID().toString();
        SignUpRequest request = AuthenticationHelper.createSignUpRequest(faker.internet().emailAddress(), password, password);

        MvcResult mvcResult = mockMvc.perform(
                        post("/auth/sign-up")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated()).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();

        Assertions.assertEquals(REGISTRATION_SUCCESSFUL, jsonResponse);

        Optional<User> userOptional = userRepository.findByEmail(request.email());
        userOptional.ifPresentOrElse(user ->
                        AuthenticationHelper.assertSignedUpUserAndProfile(user, request),
                () -> Assertions.fail("User does not exist."));
    }

    @Test
    void signUp_passwordsNotMatching() throws Exception {
        SignUpRequest request = AuthenticationHelper.createSignUpRequest(faker.internet().emailAddress(), UUID.randomUUID().toString(), UUID.randomUUID().toString());

        mockMvc.perform(
                        post("/auth/sign-up")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_userWithEmailAlreadyExists() throws Exception {
        User user = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));

        String password = UUID.randomUUID().toString();
        SignUpRequest request = AuthenticationHelper.createSignUpRequest(user.getEmail(), password, password);

        mockMvc.perform(
                        post("/auth/sign-up")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isConflict());
    }
}