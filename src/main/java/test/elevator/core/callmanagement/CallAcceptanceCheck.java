package test.elevator.core.callmanagement;

@FunctionalInterface
public interface CallAcceptanceCheck {
    boolean canAccept(Call call);
}
