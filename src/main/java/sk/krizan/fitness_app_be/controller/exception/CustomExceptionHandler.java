package sk.krizan.fitness_app_be.controller.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request
    ) {
        List<String> errors = new ArrayList<>();
        ex.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));

        Map<String, List<String>> result = new HashMap<>();
        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ExceptionResponse handleNotFoundException(NotFoundException exception) {
        return ExceptionResponse.builder()
            .timestamp(LocalDateTime.now())
            .message(exception.getMessage())
            .build();
    }

    @ExceptionHandler(IllegalOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ExceptionResponse handleIllegalOperationException(
        IllegalOperationException exception
    ) {
        return ExceptionResponse.builder()
            .timestamp(LocalDateTime.now())
            .message(exception.getMessage())
            .build();
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ExceptionResponse handleAccessDeniedException(ForbiddenException exception) {
        return ExceptionResponse.builder()
            .timestamp(LocalDateTime.now())
            .message(exception.getMessage())
            .build();
    }
}