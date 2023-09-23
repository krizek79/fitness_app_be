package sk.krizan.fitness_app_be.controller.exception;

public class ForbiddenException extends RuntimeException {

    private static final String MESSAGE = "No permission";

    public ForbiddenException() {
        super(MESSAGE);
    }
}
