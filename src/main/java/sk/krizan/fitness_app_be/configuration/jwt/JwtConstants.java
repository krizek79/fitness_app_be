package sk.krizan.fitness_app_be.configuration.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtConstants {

    ISSUER("self"),
    ROLES_CLAIM("roles"),
    ROLE_PREFIX("ROLE_");

    private final String value;
}
