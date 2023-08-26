package sk.krizan.fitness_app_be.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.LocalAuthenticationRequest;
import sk.krizan.fitness_app_be.controller.request.SignUpRequest;
import sk.krizan.fitness_app_be.controller.response.AuthenticationResponse;
import sk.krizan.fitness_app_be.service.api.AuthenticationService;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("sign-in/local")
    public AuthenticationResponse signInLocal(@Valid LocalAuthenticationRequest request) {
        return authenticationService.signInLocal(request);
    }

    @PostMapping("sign-up")
    public String signUp(@Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }
}
