package sk.krizan.fitness_app_be.configuration.web;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${keycloak.authorization.url}")
    private String keycloakAuthorizationUrl;

    @Value("${keycloak.token.url}")
    private String keycloakTokenUrl;

    @Bean
    public OpenAPI customOpenAPI() {

        final String bearerSchemeName = "bearerAuth";
        final String keycloakSchemeName = "keycloakAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Fitness App API")
                        .description("Comprehensive API for fitness application with support for workout plans, exercises, and progress tracking. " +
                                "API provides OAuth2 authentication via Keycloak or JWT tokens."))
                .components(new Components()
                        .addSecuritySchemes(bearerSchemeName, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Bearer token - used for direct authentication without OAuth2"))
                        .addSecuritySchemes(keycloakSchemeName, new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .description("OAuth2 authentication via Keycloak (supports Google and other external providers)")
                                .flows(new OAuthFlows()
                                        .authorizationCode(new OAuthFlow()
                                                .authorizationUrl(keycloakAuthorizationUrl)
                                                .tokenUrl(keycloakTokenUrl)
                                                .scopes(new Scopes()
                                                        .addString("openid", "Basic OIDC scope - user identification")
                                                        .addString("profile", "Access to user profile data and photo")
                                                        .addString("email", "Access to user email address"))))))
                .addSecurityItem(new SecurityRequirement().addList(bearerSchemeName))
                .addSecurityItem(new SecurityRequirement().addList(keycloakSchemeName));
    }
}