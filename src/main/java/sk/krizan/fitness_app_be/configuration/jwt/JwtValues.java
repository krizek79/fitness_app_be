package sk.krizan.fitness_app_be.configuration.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@NoArgsConstructor
@Component
public class JwtValues {

    @Value("${security.jwt.expiration}")
    private Long expiration;
}
