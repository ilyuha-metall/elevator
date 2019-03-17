package test.elevator.exceptions;

public class WrongMovingException extends RuntimeException {
    private final int sourcePosition;
    private final int destPosition;

    public WrongMovingException(int source, int dest) {
        super(String.format("Can't change elevator position from %d to %d", source, dest));
        this.sourcePosition = source;
        this.destPosition = dest;
    }

    public int getSourcePosition() {
        return sourcePosition;
    }

    public int getDestPosition() {
        return destPosition;
    }
}
