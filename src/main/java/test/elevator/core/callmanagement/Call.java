package test.elevator.core.callmanagement;

public class Call<T> {
    private final int start;
    private final Direction direction;
    private T extraParams;

    public Call(int start, Direction direction) {
        this.start = start;
        this.direction = direction;
    }

    public Call(int start, Direction direction, T extraParams) {
        this.start = start;
        this.direction = direction;
        this.extraParams = extraParams;
    }

    public int getStart() {
        return start;
    }

    public Direction getDirection() {
        return direction;
    }

    public T getExtraParams() {
        return extraParams;
    }
}
