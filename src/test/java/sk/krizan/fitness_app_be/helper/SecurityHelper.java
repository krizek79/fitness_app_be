package sk.krizan.fitness_app_be.helper;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import sk.krizan.fitness_app_be.model.entity.User;

import java.util.stream.Collectors;

public class SecurityHelper {

    public static void setAuthentication(User user) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        "",
                        user.getRoleSet().stream()
                                .map(role -> new SimpleGrantedAuthority(role.name()))
                                .collect(Collectors.toSet())));
    }
}
