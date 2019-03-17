package test.elevator.core;

import test.elevator.actions.ActionObserver;
import test.elevator.core.callmanagement.*;

public class ElevatorFacade {
    private final Transporter transporter;
    private final Router router;
    private final ControlPanel panel;

    public ElevatorFacade(Transporter transporter, Router router, ControlPanel panel) {
        this.transporter = transporter;
        this.router = router;
        this.panel = panel;
    }

    public void addActionObserver(ActionObserver observer){
        router.addActionObserver(observer);
    }

    public int getPosition(){
        return transporter.getPosition();
    }

    public void addCall(int position, Direction direction){
        panel.call(router, position, direction);

    }
}
