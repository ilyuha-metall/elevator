package test.elevator.core.callmanagement;

public interface StrategyFactory {
    Strategy createStrategy();
    ControlPanel createControlPanel();
}
