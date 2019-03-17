package test.elevator.core.state;

import test.elevator.app.Util;
import test.elevator.config.ConfStore;
import test.elevator.core.callmanagement.Router;
import test.elevator.core.callmanagement.Transporter;
import test.elevator.exceptions.IllegalRouteContent;

import java.util.List;

public class StopState implements ElevatorState {

    @Override
    public boolean isFitting(int targetPosition, List<Integer> route, int currentPosition) {
        return route.isEmpty();
    }

    @Override
    public ElevatorState getNextState(int position, List<Integer> route) {
        if(route.isEmpty())
            return this;


        Util.safeSleep(ConfStore.getConf().getIntParam(ConfStore.SWITCHING_DELAY));

        if(position < route.get(0))
            return ElevatorStateFactory.getState(ElevatorStateFactory.UP_STATE);

        if(position > route.get(0))
            return ElevatorStateFactory.getState(ElevatorStateFactory.DOWN_STATE);

        throw new IllegalRouteContent(String.format("Current position %d exists in route", position));
    }

    @Override
    public void process(Transporter transporter, Router router) {
        router.processCalls(this, transporter);
        router.processStop(this, transporter);
        transporter.getRoute().removeIf(position -> position == transporter.getPosition());
    }
}
