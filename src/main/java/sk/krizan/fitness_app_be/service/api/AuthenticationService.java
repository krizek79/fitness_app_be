package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.controller.request.ClientAuthenticationRequest;
import sk.krizan.fitness_app_be.controller.request.LocalAuthenticationRequest;
import sk.krizan.fitness_app_be.controller.request.SignUpRequest;
import sk.krizan.fitness_app_be.controller.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse signInLocal(LocalAuthenticationRequest request);
    AuthenticationResponse signInMeta(ClientAuthenticationRequest request);
    AuthenticationResponse signInGoogle(ClientAuthenticationRequest request);
    String signUp(SignUpRequest request);
}
