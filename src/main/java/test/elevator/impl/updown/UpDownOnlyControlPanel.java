package test.elevator.impl.updown;

import test.elevator.core.callmanagement.Call;
import test.elevator.core.callmanagement.ControlPanel;
import test.elevator.core.callmanagement.Direction;
import test.elevator.core.callmanagement.Router;

public class UpDownOnlyControlPanel implements ControlPanel {
    @Override
    public void call(Router router, int position, Direction direction) {
        router.addCall(new Call(position, direction));
    }
}
