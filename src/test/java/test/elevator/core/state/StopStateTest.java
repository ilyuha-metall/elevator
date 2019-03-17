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

public class StopStateTest {

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
        StopState stopState = new StopState();
        Assert.assertTrue(stopState.isFitting(0, Collections.emptyList(), 0));
    }

    @Test
    public void isFittingFalse() {
        StopState stopState = new StopState();
        ArrayList<Integer> route = new ArrayList<>();
        route.add(1);
        Assert.assertFalse(stopState.isFitting(0, route, 0));
    }

    @Test
    public void getNextState() {
        StopState stopState = new StopState();
        ArrayList<Integer> route = new ArrayList<>();
        Assert.assertTrue(stopState.getNextState(0, route) instanceof StopState);
        route.add(5);
        Assert.assertTrue(stopState.getNextState(0, route) instanceof UpState);
        route.set(0, 5);
        Assert.assertTrue(stopState.getNextState(10, route) instanceof DownState);

    }

    @Test
    public void process() {
        StopState stopState = new StopState();
        Transporter transporter = new Transporter(5);
        transporter.getRoute().add(1);
        transporter.getRoute().add(2);
        transporter.getRoute().add(5);
        transporter.getRoute().add(3);
        transporter.getRoute().add(5);

        StrategyStub strategy = new StrategyStub();
        Router router = new Router(strategy);
        stopState.process(transporter, router);

        Assert.assertEquals(5, transporter.getPosition());
        Assert.assertEquals(stopState, strategy.stateProc);
        Assert.assertEquals(transporter, strategy.transporterProc);
        Assert.assertEquals(router, strategy.routerProc);
        Assert.assertEquals(1, strategy.callNumProc);

        Assert.assertEquals(stopState, strategy.stateStop);
        Assert.assertEquals(transporter, strategy.transporterStop);
        Assert.assertEquals(router, strategy.routerStop);
        Assert.assertEquals(1, strategy.callNumStop);

        Assert.assertEquals(3, (int) transporter.getRoute().size());
        Assert.assertEquals(1, (int) transporter.getRoute().get(0));
        Assert.assertEquals(2, (int) transporter.getRoute().get(1));
        Assert.assertEquals(3, (int) transporter.getRoute().get(2));
    }

    private class StrategyStub implements Strategy {
        ElevatorState stateProc;
        Transporter transporterProc;
        Router routerProc;
        int callNumProc = 0;

        ElevatorState stateStop;
        Transporter transporterStop;
        Router routerStop;
        int callNumStop = 0;

        @Override
        public void processCalls(ElevatorState state, Transporter transporter, Router router) {
            this.stateProc = state;
            this.transporterProc = transporter;
            this.routerProc = router;
            callNumProc++;
        }

        @Override
        public void processStop(ElevatorState state, Transporter transporter, Router router) {
            this.stateStop = state;
            this.transporterStop = transporter;
            this.routerStop = router;
            callNumStop++;
        }
    }
}