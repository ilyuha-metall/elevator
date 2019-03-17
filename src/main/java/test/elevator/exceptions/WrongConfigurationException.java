package test.elevator.exceptions;

public class WrongConfigurationException extends RuntimeException {
    public WrongConfigurationException(String message) {
        super(message);
    }

    public WrongConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
