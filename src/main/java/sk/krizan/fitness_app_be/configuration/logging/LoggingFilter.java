package sk.krizan.fitness_app_be.configuration.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sk.krizan.fitness_app_be.configuration.jwt.JwtConstants;
import sk.krizan.fitness_app_be.model.CustomUserDetails;

import java.io.IOException;
import java.util.UUID;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    public static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String incomingTraceId = request.getHeader(TRACE_ID_HEADER);
            String traceId = incomingTraceId != null ? incomingTraceId : UUID.randomUUID().toString();

            MDC.put(MdcConstants.TRACE_ID.getValue(), traceId);
            MDC.put(MdcConstants.CLIENT_IP.getValue(), request.getRemoteAddr());
            MDC.put(MdcConstants.HTTP_METHOD.getValue(), request.getMethod());
            MDC.put(MdcConstants.PATH.getValue(), request.getRequestURI());
            MDC.put(MdcConstants.START_TIME.getValue(), String.valueOf(System.currentTimeMillis()));

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof Jwt jwt) {
                    MDC.put(MdcConstants.USER_ID.getValue(), jwt.getSubject());

                    Object rolesClaim = jwt.getClaim(JwtConstants.ROLES_CLAIM.getValue());
                    if (rolesClaim instanceof String roleStr) {
                        MDC.put(MdcConstants.USER_ROLES.getValue(), roleStr.replace(" ", ", "));
                    }
                } else if (principal instanceof CustomUserDetails userDetails) {
                    MDC.put(MdcConstants.USER_ID.getValue(), userDetails.getUsername());
                    MDC.put(
                            MdcConstants.USER_ROLES.getValue(),
                            String.join(", ", userDetails.getAuthorities().stream()
                                    .map(Object::toString)
                                    .toList()));
                }
            }

            response.setHeader(TRACE_ID_HEADER, traceId);

            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
