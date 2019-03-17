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
import java.util.Collections;

import static org.junit.Assert.*;

public class DownStateTest {

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
        route.add(0);
        DownState downState = new DownState();
        Assert.assertTrue(downState.isFitting(3, route, 5));
    }

    @Test
    public void isFittingFalse() {
        ArrayList<Integer> route = new ArrayList<>();
        route.add(0);
        DownState downState = new DownState();
        Assert.assertFalse(downState.isFitting(5, route, 3));
    }

    @Test
    public void getNextState() {
        DownState downState = new DownState();
        ArrayList<Integer> route = new ArrayList<>();
        route.add(5);
        Assert.assertFalse(downState.getNextState(0, route) instanceof StopState);
        Assert.assertTrue(downState.getNextState(0, route) instanceof DownState);
        route.add(0);
        Assert.assertTrue(downState.getNextState(0, route) instanceof StopState);
    }

    @Test
    public void process() {
        DownState downState = new DownState();
        Transporter transporter = new Transporter(5);

        StrategyStub strategy = new StrategyStub();
        Router router = new Router(strategy);
        downState.process(transporter, router);

        Assert.assertEquals(4, transporter.getPosition());
        Assert.assertEquals(downState, strategy.state);
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