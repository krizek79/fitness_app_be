package sk.krizan.fitness_app_be.controller.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("sign-in")
    public AuthenticationResponse signInLocal(@Valid @RequestBody LocalAuthenticationRequest request) {
        return authenticationService.signInLocal(request);
    }

    @PostMapping("sign-up")
    public String signUp(@Valid @RequestBody SignUpRequest request) {
        return authenticationService.signUp(request);
    }
}
