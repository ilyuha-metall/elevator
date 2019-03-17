package test.elevator.actions;

import test.elevator.core.callmanagement.Call;

public interface ActionObserver {
    void onAction(ElevatorActions action, Call call);
}
