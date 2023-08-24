package sk.krizan.fitness_app_be.service.impl;

import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.controller.request.ClientAuthenticationRequest;
import sk.krizan.fitness_app_be.controller.request.LocalAuthenticationRequest;
import sk.krizan.fitness_app_be.controller.request.SignUpRequest;
import sk.krizan.fitness_app_be.controller.response.AuthenticationResponse;
import sk.krizan.fitness_app_be.service.api.AuthenticationService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public AuthenticationResponse authenticateLocal(LocalAuthenticationRequest request) {
        return null;
    }

    @Override
    public AuthenticationResponse authenticateMeta(ClientAuthenticationRequest request) {
        return null;
    }

    @Override
    public AuthenticationResponse authenticateGoogle(ClientAuthenticationRequest request) {
        return null;
    }

    @Override
    public String signup(SignUpRequest request) {
        return null;
    }
}
