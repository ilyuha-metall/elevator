package test.elevator.core.cabin;

import test.elevator.core.callmanagement.Call;

public class BySeatsAcceptor implements CabinAcceptor {
    private final int numberOfSeats;
    private int occupiedSeats;

    public BySeatsAcceptor(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
        this.occupiedSeats = 0;
    }

    @Override
    public boolean canAccept(Call call) {
        return occupiedSeats < numberOfSeats;
    }

    @Override
    public void accept(Call call) {
        occupiedSeats++;
    }

    @Override
    public void release(Call call) {
        occupiedSeats--;
    }
}
