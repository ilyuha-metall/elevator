package test.elevator.core.cabin;

import test.elevator.core.callmanagement.Call;
import test.elevator.core.callmanagement.CallAcceptanceCheck;

public interface CabinAcceptor extends CallAcceptanceCheck {
    boolean canAccept(Call call);
    void accept(Call call);
    void release(Call call);
}
