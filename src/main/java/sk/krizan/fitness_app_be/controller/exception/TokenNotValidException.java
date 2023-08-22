package sk.krizan.fitness_app_be.controller.exception;

public class TokenNotValidException extends RuntimeException {

    public TokenNotValidException(String message) {
        super(message);
    }
}