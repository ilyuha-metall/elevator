package test.elevator.core.state;

import test.elevator.app.Util;
import test.elevator.config.ConfStore;
import test.elevator.core.callmanagement.Router;
import test.elevator.core.callmanagement.Transporter;

import java.util.List;

public class UpState implements ElevatorState {

    @Override
    public boolean isFitting(int targetPosition, List<Integer> route, int currentPosition) {
        return !route.isEmpty() && currentPosition <= targetPosition && targetPosition <= route.get(0);
    }

    @Override
    public ElevatorState getNextState(int position, List<Integer> route) {
        if (route.contains(position)) {
            Util.safeSleep(ConfStore.getConf().getIntParam(ConfStore.SWITCHING_DELAY));
            return ElevatorStateFactory.getState(ElevatorStateFactory.STOP_STATE);
        } else
            return this;
    }

    @Override
    public void process(Transporter transporter, Router router) {

        transporter.moveUp();
        router.processCalls(this, transporter);
        Util.safeSleep(ConfStore.getConf().getIntParam(ConfStore.MOVING_DELAY));
    }
}
