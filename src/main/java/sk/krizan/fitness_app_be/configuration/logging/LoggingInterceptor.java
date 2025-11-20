package sk.krizan.fitness_app_be.configuration.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex)
    {
        if (ex != null) {
            return;
        }

        int status = response.getStatus();

        String startTimeStr = MDC.get(MdcConstants.START_TIME.getValue());
        long startTime = startTimeStr != null ? Long.parseLong(startTimeStr) : 0;
        long duration = System.currentTimeMillis() - startTime;

        LogUtils.logRequest(status, duration);
    }
}
