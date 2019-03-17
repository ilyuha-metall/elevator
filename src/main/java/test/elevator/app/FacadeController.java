package test.elevator.app;


import test.elevator.core.ElevatorFacade;
import test.elevator.core.callmanagement.Direction;
import test.elevator.gui.FxDirection;
import test.elevator.gui.FxInterfaceController;

public class FacadeController implements FxInterfaceController {
    private final ElevatorFacade facade;

    public FacadeController(ElevatorFacade facade) {
        this.facade = facade;
    }

    @Override
    public void onCall(int position, FxDirection direction) {
        facade.addCall(position, toCoreDirection(direction));
    }

    private Direction toCoreDirection(FxDirection direction) {
        return Direction.valueOf(direction.name());
    }

    @Override
    public int getPosition() {
        return facade.getPosition();
    }
}