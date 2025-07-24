package sk.krizan.fitness_app_be.controller.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {

    private final Object detail;
    private final HttpStatus httpStatus;

    public ApplicationException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.detail = null;
    }

    public ApplicationException(HttpStatus httpStatus, String message, Object detail) {
        super(message);
        this.httpStatus = httpStatus;
        this.detail = detail;
    }
}
