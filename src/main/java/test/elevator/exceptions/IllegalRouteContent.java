package test.elevator.exceptions;

public class IllegalRouteContent extends RuntimeException {
    public IllegalRouteContent(String format) {
        super(format);
    }

    public IllegalRouteContent(String message, Throwable cause) {
        super(message, cause);
    }
}
