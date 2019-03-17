package test.elevator.core.state;

import test.elevator.core.callmanagement.Router;
import test.elevator.core.callmanagement.Transporter;

import java.util.List;

public interface ElevatorState {

    boolean isFitting(int targetPosition, List<Integer> route, int currentPosition);
    ElevatorState getNextState(int position, List<Integer> route);
    void process(Transporter transporter, Router router);
}
