package test.elevator.core.callmanagement;

import test.elevator.core.state.ElevatorState;

public interface Strategy {
    void processCalls(ElevatorState state, Transporter transporter, Router router);

    void processStop(ElevatorState state, Transporter transporter, Router router);
}
