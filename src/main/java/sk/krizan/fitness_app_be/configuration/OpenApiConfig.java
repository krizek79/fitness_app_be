package sk.krizan.fitness_app_be.configuration;

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
                        .description("API for fitness app"))
                .components(new Components()
                        .addSecuritySchemes(bearerSchemeName, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token"))
                        .addSecuritySchemes(keycloakSchemeName, new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .description("Prihlásenie cez Google / Keycloak")
                                .flows(new OAuthFlows()
                                        .authorizationCode(new OAuthFlow()
                                                .authorizationUrl(keycloakAuthorizationUrl)
                                                .tokenUrl(keycloakTokenUrl)
                                                .scopes(new Scopes()
                                                        .addString("openid", "Základný OIDC scope")
                                                        .addString("profile", "Prístup k menu a fotke") // Tu je schovaná fotka
                                                        .addString("email", "Prístup k emailu"))))))
                .addSecurityItem(new SecurityRequirement().addList(bearerSchemeName))
                .addSecurityItem(new SecurityRequirement().addList(keycloakSchemeName));
    }
}