package sk.krizan.fitness_app_be.configuration.jwt;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.cert.Certificate;

@Configuration
public class JwtConfig {

    @Value("${jwt.keystore.location}")
    private Resource keystoreResource;

    @Value("${jwt.keystore.password}")
    private String keystorePassword;

    @Value("${jwt.key.alias}")
    private String keyAlias;

    @Value("${jwt.key.password}")
    private String keyPassword;

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    @Bean
    public JwtEncoder jwtEncoder() throws Exception {
        loadKeys();
        JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        loadKeys();
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("roles");
        authoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return authenticationConverter;
    }

    private void loadKeys() throws Exception {
        if (publicKey != null && privateKey != null) {
            return;
        }

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (InputStream is = keystoreResource.getInputStream()) {
            keyStore.load(is, keystorePassword.toCharArray());
        }

        privateKey = (RSAPrivateKey) keyStore.getKey(keyAlias, keyPassword.toCharArray());

        Certificate cert = keyStore.getCertificate(keyAlias);
        publicKey = (RSAPublicKey) cert.getPublicKey();
    }
}