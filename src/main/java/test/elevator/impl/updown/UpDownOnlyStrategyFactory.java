package test.elevator.impl.updown;

import test.elevator.core.callmanagement.ControlPanel;
import test.elevator.core.callmanagement.Strategy;
import test.elevator.core.callmanagement.StrategyFactory;

public class UpDownOnlyStrategyFactory implements StrategyFactory {

    @Override
    public Strategy createStrategy() {
        return new UpDownOnlyStrategy();
    }

    @Override
    public ControlPanel createControlPanel() {
        return new UpDownOnlyControlPanel();
    }
}
