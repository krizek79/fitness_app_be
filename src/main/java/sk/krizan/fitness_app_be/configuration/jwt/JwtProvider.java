package sk.krizan.fitness_app_be.configuration.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtValues jwtValues;
    private final JwtEncoder jwtEncoder;

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        long expiresIn = jwtValues.getExpiration();

        String authorities = authentication.getAuthorities().stream()
            .map(grantedAuthority -> JwtConstants.ROLE_PREFIX.getValue() + grantedAuthority.getAuthority())
            .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer(JwtConstants.ISSUER.getValue())
            .issuedAt(now)
            .expiresAt(now.plus(expiresIn, ChronoUnit.HOURS))
            .subject(authentication.getName())
            .claim(JwtConstants.ROLES_CLAIM.getValue(), authorities)
            .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
