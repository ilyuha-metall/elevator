package test.elevator.exceptions;

public class UnknownStateId extends RuntimeException {
    public UnknownStateId(String state) {
        super(String.format("%s is unknown state", state));
    }
}
