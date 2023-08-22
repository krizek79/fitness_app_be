package sk.krizan.fitness_app_be.controller.exception;

public class IllegalOperationException extends RuntimeException {

    public IllegalOperationException(String message) {
        super(message);
    }
}