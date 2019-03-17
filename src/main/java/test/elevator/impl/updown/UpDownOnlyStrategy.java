package test.elevator.impl.updown;

import test.elevator.core.callmanagement.*;
import test.elevator.config.ConfStore;
import test.elevator.core.state.ElevatorState;

import java.util.List;
import java.util.stream.Collectors;

public class UpDownOnlyStrategy implements Strategy {
    @Override
    public void processCalls(ElevatorState state, Transporter transporter, Router router) {

        List<Call> availableCalls = router.getAvailableCalls();
        availableCalls.forEach(call -> {
            final List<Integer> route = transporter.getRoute();
            final int position = transporter.getPosition();
            if (router.canAccept(call) &&
                    state.isFitting(call.getStart(), route, position) &&
                    state.isFitting(toDest(call.getDirection()), route, position)) {
                route.add(call.getStart());
                router.acceptCall(call);
            }
        });
    }

    @Override
    public void processStop(ElevatorState state, Transporter transporter, Router router) {
        router.getAcceptedCalls().forEach(call -> {
            if(transporter.getPosition() == call.getStart()){
                transporter.getRoute().add(toDest(call.getDirection()));
            }
        });

        router.getAcceptedCalls().stream().filter(
                call -> (
                    !transporter.getRoute().contains(call.getStart()) &&
                        transporter.getPosition() == toDest(call.getDirection())
                )
        ).collect(Collectors.toList()).forEach(router::finishCall);
    }

    private int toDest(Direction direction){
       return ConfStore.getConf().getIntParam( direction==Direction.UP ? ConfStore.TOP_FLOOR : ConfStore.BOTTOM_FLOOR);
    }
}
