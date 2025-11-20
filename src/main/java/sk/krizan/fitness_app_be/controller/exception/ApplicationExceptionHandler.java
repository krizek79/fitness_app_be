package sk.krizan.fitness_app_be.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sk.krizan.fitness_app_be.configuration.logging.MdcConstants;
import sk.krizan.fitness_app_be.controller.response.ProblemDetails;
import sk.krizan.fitness_app_be.configuration.logging.LogUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String DEFAULT_TYPE = "about:blank";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        Map<String, List<String>> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        LinkedHashMap::new,
                        Collectors.mapping(DefaultMessageSourceResolvable::getDefaultMessage, Collectors.toList())
                ));

        return buildProblemDetails(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                "Validation error on input fields",
                request.getDescription(false),
                fieldErrors,
                ex
        );
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Object> handleApplicationException(ApplicationException ex, WebRequest request) {
        return buildProblemDetails(ex.getHttpStatus(), ex.getMessage(), "Business exception", request.getDescription(false), ex.getDetail(), ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        return buildProblemDetails(HttpStatus.FORBIDDEN, "Access denied", "You do not have permission", request.getDescription(false), null, ex);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<Object> handleUnexpectedException(Exception ex, WebRequest request) {
        return buildProblemDetails(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected internal server error", "Server error", request.getDescription(false), null, ex);
    }

    private ResponseEntity<Object> buildProblemDetails(
            HttpStatus status,
            String title,
            String type,
            String instance,
            Object errors,
            Exception exception
    ) {
        String traceId = MDC.get(MdcConstants.TRACE_ID.getValue());
        String errorId = UUID.randomUUID().toString();

        LogUtils.logException(errorId, exception, status);

        ProblemDetails problemDetails = ProblemDetails.builder()
                .type(type != null ? type : DEFAULT_TYPE)
                .title(title)
                .status(status.value())
                .detail(exception.getMessage())
                .instance(instance)
                .timestamp(LocalDateTime.now())
                .traceId(traceId)
                .errorId(errorId)
                .errors(errors)
                .build();

        return new ResponseEntity<>(problemDetails, status);
    }
}
