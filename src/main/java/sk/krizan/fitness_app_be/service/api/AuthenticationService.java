package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.controller.request.ClientAuthenticationRequest;
import sk.krizan.fitness_app_be.controller.request.LocalAuthenticationRequest;
import sk.krizan.fitness_app_be.controller.request.SignUpRequest;
import sk.krizan.fitness_app_be.controller.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse authenticateLocal(LocalAuthenticationRequest request);
    AuthenticationResponse authenticateMeta(ClientAuthenticationRequest request);
    AuthenticationResponse authenticateGoogle(ClientAuthenticationRequest request);
    String signup(SignUpRequest request);
}
