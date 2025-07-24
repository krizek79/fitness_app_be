package sk.krizan.fitness_app_be.controller.endpoint.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.LocalAuthenticationRequest;
import sk.krizan.fitness_app_be.controller.request.SignUpRequest;
import sk.krizan.fitness_app_be.controller.response.AuthenticationResponse;
import sk.krizan.fitness_app_be.service.api.AuthenticationService;

@RestController
@RequiredArgsConstructor
public class AuthenticationController implements sk.krizan.fitness_app_be.controller.endpoint.api.AuthenticationController {

    private final AuthenticationService authenticationService;

    @Override
    public AuthenticationResponse signInLocal(LocalAuthenticationRequest request) {
        return authenticationService.signInLocal(request);
    }

    @Override
    public String signUp(SignUpRequest request) {
        return authenticationService.signUp(request);
    }
}
