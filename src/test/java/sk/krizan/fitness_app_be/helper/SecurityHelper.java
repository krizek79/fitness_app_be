package sk.krizan.fitness_app_be.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import sk.krizan.fitness_app_be.model.CustomUserDetails;
import sk.krizan.fitness_app_be.model.entity.User;

import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityHelper {

    public static void setAuthentication(User user) {
        CustomUserDetails userDetails = new CustomUserDetails(user);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
