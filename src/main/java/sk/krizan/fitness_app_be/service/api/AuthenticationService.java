package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.controller.request.LocalAuthenticationRequest;
import sk.krizan.fitness_app_be.controller.request.SignUpRequest;
import sk.krizan.fitness_app_be.controller.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse signInLocal(LocalAuthenticationRequest request);
    String signUp(SignUpRequest request);
}
