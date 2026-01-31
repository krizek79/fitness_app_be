package sk.krizan.fitness_app_be.common.exception;

import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import org.springframework.http.HttpStatus;

@Getter
@FieldNameConstants
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
