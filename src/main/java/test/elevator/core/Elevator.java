package test.elevator.core;

import test.elevator.actions.ActionObserver;
import test.elevator.actions.ElevatorActions;
import test.elevator.core.cabin.Cabin;
import test.elevator.core.callmanagement.Call;
import test.elevator.core.callmanagement.Router;
import test.elevator.core.callmanagement.Transporter;
import test.elevator.core.state.ElevatorState;

public class Elevator {
    private final Cabin cabin;
    private final Transporter transporter;
    private final Router router;
    private ElevatorState state;

    public Elevator(Cabin cabin, Transporter transporter, Router router, ElevatorState state) {
        this.cabin = cabin;
        this.transporter = transporter;
        this.router = router;
        this.state = state;
    }

    public void init(){
        router.addActionObserver(new CabinActionObserver());

        router.addAdditionCheck(cabin::canAccept);
    }

    public void doJob(){
        state.process(transporter, router);
        state = state.getNextState(transporter.getPosition(), transporter.getRoute());

    }

    private class CabinActionObserver implements ActionObserver {
        @Override
        public void onAction(ElevatorActions action, Call call) {
            switch (action) {
                case CALL_ACCEPTED:
                    cabin.accept(call);
                    break;
                case CALL_FINISHED:
                    cabin.release(call);
                    break;
                case CALL_APPEARED:
                default:
                    //do nothing
            }
        }
    }
}
