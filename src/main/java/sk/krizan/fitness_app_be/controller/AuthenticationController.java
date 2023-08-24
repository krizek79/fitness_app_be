package sk.krizan.fitness_app_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.service.api.AuthenticationService;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
}
