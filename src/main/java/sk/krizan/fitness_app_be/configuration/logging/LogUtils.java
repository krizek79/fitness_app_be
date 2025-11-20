package sk.krizan.fitness_app_be.configuration.logging;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogUtils {

    public static void logRequest(
            int status,
            long duration
    ) {
        String traceId = MDC.get(MdcConstants.TRACE_ID.getValue());
        String userId = MDC.get(MdcConstants.USER_ID.getValue());
        String roles = MDC.get(MdcConstants.USER_ROLES.getValue());
        String clientIp = MDC.get(MdcConstants.CLIENT_IP.getValue());
        String method = MDC.get(MdcConstants.HTTP_METHOD.getValue());
        String path = MDC.get(MdcConstants.PATH.getValue());

        String prefix = Optional.ofNullable(traceId).map(t -> "[traceId=" + t + "] ").orElse("");
        String userInfo = "";
        if (userId != null) userInfo += "[userId=" + userId + "] ";
        if (roles != null) userInfo += "[roles=" + roles + "] ";
        if (clientIp != null) userInfo += "[clientIp=" + clientIp + "] ";

        if (status >= 500) {
            log.error("{}{}{} {} -> {} ({}ms)", prefix, userInfo, method, path, status, duration);
        } else if (status >= 400) {
            log.warn("{}{}{} {} -> {} ({}ms)", prefix, userInfo, method, path, status, duration);
        } else {
            log.info("{}{}{} {} -> {} ({}ms)", prefix, userInfo, method, path, status, duration);
        }
    }

    public static void logException(
            String errorId,
            Exception exception,
            HttpStatus status
    ) {
        String traceId = MDC.get(MdcConstants.TRACE_ID.getValue());
        String userId = MDC.get(MdcConstants.USER_ID.getValue());
        String roles = MDC.get(MdcConstants.USER_ROLES.getValue());

        String prefix = Optional.ofNullable(traceId).map(t -> "[traceId=" + t + "] ").orElse("");
        String userInfo = "";
        if (userId != null) userInfo += "[userId=" + userId + "] ";
        if (roles != null) userInfo += "[roles=" + roles + "] ";
        String errorInfo = "[errorId=" + errorId + "] ";

        if (status.is5xxServerError()) {
            log.error("{}{}{} {}", prefix, userInfo, errorInfo, exception.getMessage(), exception);
        } else {
            log.warn("{}{}{} {}", prefix, userInfo, errorInfo, exception.getMessage());
        }
    }
}
