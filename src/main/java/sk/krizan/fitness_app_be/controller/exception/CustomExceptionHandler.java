package sk.krizan.fitness_app_be.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sk.krizan.fitness_app_be.controller.response.ExceptionResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        List<Map<String, String>> fieldErrors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            Map<String, String> fieldError = new HashMap<>();
            fieldError.put("field", error.getField());
            fieldError.put("message", error.getDefaultMessage());
            fieldErrors.add(fieldError);
        });

        ExceptionResponse response = ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .message("Validation failed")
                .detail(fieldErrors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ExceptionResponse handleNotFoundException(NotFoundException exception) {
        logException(exception);
        return ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(IllegalOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ExceptionResponse handleIllegalOperationException(IllegalOperationException exception) {
        logException(exception);
        return ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(exception.getMessage())
                .detail(exception.getMessage())
                .build();
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleAccessDeniedException(ForbiddenException exception) {
        logException(exception);
        return ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(exception.getMessage())
                .detail(exception.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleGenericException(Exception exception) {
        logException(exception);
        return ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .message("Unexpected server error.")
                .detail(exception.getMessage())
                .build();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleRuntimeException(RuntimeException exception) {
        logException(exception);
        return ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .message("Unexpected error occurred. Please try again later.")
                .detail(exception.getMessage())
                .build();
    }

    private void logException(Exception exception) {
        log.error("{}: {}", exception.getClass().getCanonicalName(), exception.getMessage(), exception);
    }
}