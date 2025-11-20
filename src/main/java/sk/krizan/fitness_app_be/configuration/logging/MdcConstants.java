package sk.krizan.fitness_app_be.configuration.logging;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MdcConstants {

    TRACE_ID("traceId"),
    CLIENT_IP("clientIp"),
    HTTP_METHOD("httpMethod"),
    PATH("path"),
    START_TIME("startTime"),
    USER_ID("userId"),
    USER_ROLES("roles");

    private final String value;
}
