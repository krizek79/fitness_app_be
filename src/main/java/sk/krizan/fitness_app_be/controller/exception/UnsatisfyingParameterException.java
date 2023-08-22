package sk.krizan.fitness_app_be.controller.exception;

public class UnsatisfyingParameterException extends RuntimeException {

    public UnsatisfyingParameterException(String message) {
        super(message);
    }
}