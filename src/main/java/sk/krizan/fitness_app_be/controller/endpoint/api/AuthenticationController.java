package sk.krizan.fitness_app_be.controller.endpoint.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import sk.krizan.fitness_app_be.controller.request.LocalAuthenticationRequest;
import sk.krizan.fitness_app_be.controller.request.SignUpRequest;
import sk.krizan.fitness_app_be.controller.response.AuthenticationResponse;
import sk.krizan.fitness_app_be.controller.response.ExceptionResponse;

@Tag(
        name = "Authentication",
        description = "Endpoints for user registration and login"
)
@RequestMapping("auth")
public interface AuthenticationController {

    @Operation(
            summary = "Sign in using local credentials",
            description = "Authenticates user using email and password. Returns a JWT token and user info on success.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User successfully authenticated",
                            content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request or credentials",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized – incorrect email or password",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PostMapping("sign-in")
    AuthenticationResponse signInLocal(@Valid @RequestBody LocalAuthenticationRequest request);

    @Operation(
            summary = "Register a new user",
            description = "Registers a new user account.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User successfully registered",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation failed – missing or invalid fields",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Email is already in use",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    String signUp(@Valid @RequestBody SignUpRequest request);
}

