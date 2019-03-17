package test.elevator.core.state;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import test.elevator.config.ConfStore;
import test.elevator.core.callmanagement.Router;
import test.elevator.core.callmanagement.Strategy;
import test.elevator.core.callmanagement.Transporter;
import test.elevator.exceptions.WrongConfigurationException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UpStateTest {

    @Before
    public void prepare() {
        ConfStore.setConfiguration(param -> {
            switch (param) {
                case ConfStore.SWITCHING_DELAY:
                    return 10;
                case ConfStore.MOVING_DELAY:
                    return 10;
                case ConfStore.BOTTOM_FLOOR:
                    return 0;
                case ConfStore.TOP_FLOOR:
                    return 10;
                default:
                    throw new WrongConfigurationException(String.format("Param %s not found.", param));
            }
        });
    }

    @Test
    public void isFittingTrue() {
        ArrayList<Integer> route = new ArrayList<>();
        route.add(5);
        UpState upState = new UpState();
        Assert.assertTrue(upState.isFitting(3, route, 0));
    }

    @Test
    public void isFittingFalse() {
        ArrayList<Integer> route = new ArrayList<>();
        route.add(0);
        UpState upState = new UpState();
        Assert.assertFalse(upState.isFitting(5, route, 3));
    }

    @Test
    public void getNextState() {
        UpState upState = new UpState();
        ArrayList<Integer> route = new ArrayList<>();
        route.add(5);
        Assert.assertFalse(upState.getNextState(0, route) instanceof StopState);
        Assert.assertTrue(upState.getNextState(0, route) instanceof UpState);
        route.add(0);
        Assert.assertTrue(upState.getNextState(0, route) instanceof StopState);
    }

    @Test
    public void process() {
        UpState upState = new UpState();
        Transporter transporter = new Transporter(5);

        StrategyStub strategy = new StrategyStub();
        Router router = new Router(strategy);
        upState.process(transporter, router);

        Assert.assertEquals(6, transporter.getPosition());
        Assert.assertEquals(upState, strategy.state);
        Assert.assertEquals(transporter, strategy.transporter);
        Assert.assertEquals(router, strategy.router);
        Assert.assertEquals(1, strategy.callNum);
    }

    private class StrategyStub implements Strategy {
        ElevatorState state;
        Transporter transporter;
        Router router;
        int callNum = 0;

        @Override
        public void processCalls(ElevatorState state, Transporter transporter, Router router) {
            this.state = state;
            this.transporter = transporter;
            this.router = router;
            callNum++;
        }

        @Override
        public void processStop(ElevatorState state, Transporter transporter, Router router) {

        }
    }
}