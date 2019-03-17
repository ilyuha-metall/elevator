package test.elevator.core.callmanagement;

import org.junit.Assert;
import org.junit.Test;
import test.elevator.actions.ActionObserver;
import test.elevator.actions.ElevatorActions;
import test.elevator.core.state.ElevatorState;
import test.elevator.core.state.StopState;

public class RouterTest {
    private final Call callStub1 = new Call(0, Direction.UP);
    private final Call callStub2 = new Call(1, Direction.DOWN);
    private final StopState stateStub = new StopState();
    private final Transporter transporterStub = new Transporter(0);

    @Test
    public void processCalls() {
        StrategyStub strategyStub = new StrategyStub();
        Router router = new Router(strategyStub);

        router.processCalls(stateStub, transporterStub);
        Assert.assertEquals(1, strategyStub.processCallNum);
        Assert.assertEquals(stateStub, strategyStub.lastState);
        Assert.assertEquals(transporterStub, strategyStub.lastTransporter);
        Assert.assertEquals(router, strategyStub.lastRouter);
    }

    @Test
    public void processStop() {
        StrategyStub strategyStub = new StrategyStub();
        Router router = new Router(strategyStub);

        router.processStop(stateStub, transporterStub);
        Assert.assertEquals(1, strategyStub.stopCallNum);
        Assert.assertEquals(stateStub, strategyStub.lastState);
        Assert.assertEquals(transporterStub, strategyStub.lastTransporter);
        Assert.assertEquals(router, strategyStub.lastRouter);
    }

    @Test
    public void addCall() {
        Router router = new Router(new StrategyStub());
        ActionObserverStub observer = new ActionObserverStub();
        router.addActionObserver(observer);
        Assert.assertEquals(0, router.getAvailableCalls().size());

        router.addCall(callStub1);
        Assert.assertEquals(1, router.getAvailableCalls().size());
        Assert.assertEquals(ElevatorActions.CALL_APPEARED, observer.lastAction);
        Assert.assertEquals(callStub1, observer.lastCall);
        Assert.assertEquals(1, observer.callNum);

        router.addCall(callStub1);
        Assert.assertEquals(2, router.getAvailableCalls().size());
        Assert.assertEquals(ElevatorActions.CALL_APPEARED, observer.lastAction);
        Assert.assertEquals(callStub1, observer.lastCall);
        Assert.assertEquals(2, observer.callNum);
    }

    @Test
    public void removeCall() {
        Router router = new Router(new StrategyStub());
        router.addCall(callStub1);
        router.removeCall(callStub1);
        Assert.assertEquals(0, router.getAvailableCalls().size());

        router.addCall(callStub1);
        router.addCall(callStub2);
        router.removeCall(callStub1);
        Assert.assertEquals(1, router.getAvailableCalls().size());
        Assert.assertEquals(callStub2, router.getAvailableCalls().get(0));
    }

    @Test
    public void getAvailableCalls() {
        Router router = new Router(new StrategyStub());
        router.addCall(callStub1);
        router.addCall(callStub2);

        Assert.assertEquals(callStub1, router.getAvailableCalls().get(0));
        Assert.assertEquals(callStub2, router.getAvailableCalls().get(1));

    }

    @Test
    public void canAcceptTrue() {
        Router router = new Router(new StrategyStub());

        Assert.assertTrue(router.canAccept(callStub1));

        CheckStub check1 = new CheckStub(true);
        CheckStub check2 = new CheckStub(true);

        router.addAdditionCheck(check1);
        router.addAdditionCheck(check2);

        Assert.assertEquals(0, check1.callNum);
        Assert.assertEquals(0, check2.callNum);

        Assert.assertTrue(router.canAccept(callStub1));
        Assert.assertEquals(1, check1.callNum);
        Assert.assertEquals(callStub1, check1.lastCall);

        Assert.assertEquals(1, check2.callNum);
        Assert.assertEquals(callStub1, check2.lastCall);
    }

    @Test
    public void canAcceptFalse() {
        Router router = new Router(new StrategyStub());

        Assert.assertTrue(router.canAccept(callStub1));

        CheckStub check1 = new CheckStub(true);
        CheckStub check2 = new CheckStub(false);

        router.addAdditionCheck(check1);
        router.addAdditionCheck(check2);

        Assert.assertFalse(router.canAccept(callStub1));

    }

    @Test
    public void acceptCall() {
        Router router = new Router(new StrategyStub());
        ActionObserverStub observer = new ActionObserverStub();

        router.addActionObserver(observer);
        Assert.assertEquals(0, router.getAcceptedCalls().size());

        router.acceptCall(callStub1);
        Assert.assertEquals(1, router.getAcceptedCalls().size());
        Assert.assertEquals(ElevatorActions.CALL_ACCEPTED, observer.lastAction);
        Assert.assertEquals(callStub1, observer.lastCall);
        Assert.assertEquals(1, observer.callNum);

        router.acceptCall(callStub1);
        Assert.assertEquals(2, router.getAcceptedCalls().size());
        Assert.assertEquals(ElevatorActions.CALL_ACCEPTED, observer.lastAction);
        Assert.assertEquals(callStub1, observer.lastCall);
        Assert.assertEquals(2, observer.callNum);
    }

    @Test
    public void finishCall() {
        Router router = new Router(new StrategyStub());
        ActionObserverStub observer = new ActionObserverStub();

        router.addActionObserver(observer);

        router.acceptCall(callStub1);
        router.finishCall(callStub1);
        Assert.assertEquals(0, router.getAcceptedCalls().size());
        Assert.assertEquals(ElevatorActions.CALL_FINISHED, observer.lastAction);
        Assert.assertEquals(callStub1, observer.lastCall);
        Assert.assertEquals(2, observer.callNum);

        router.acceptCall(callStub1);
        router.acceptCall(callStub2);
        router.finishCall(callStub1);
        Assert.assertEquals(1, router.getAcceptedCalls().size());
        Assert.assertEquals(callStub2, router.getAcceptedCalls().get(0));
    }

    @Test
    public void getAcceptedCalls() {
        Router router = new Router(new StrategyStub());
        router.acceptCall(callStub1);
        router.acceptCall(callStub2);

        Assert.assertEquals(callStub1, router.getAcceptedCalls().get(0));
        Assert.assertEquals(callStub2, router.getAcceptedCalls().get(1));

    }

    private class CheckStub implements CallAcceptanceCheck {
        boolean condition;
        Call lastCall;
        int callNum = 0;

        public CheckStub(boolean condition) {
            this.condition = condition;
        }

        @Override
        public boolean canAccept(Call call) {
            lastCall = call;
            callNum++;
            return condition;
        }
    }

    private class ActionObserverStub implements ActionObserver {
        ElevatorActions lastAction;
        Call lastCall;
        int callNum = 0;

        @Override
        public void onAction(ElevatorActions action, Call call) {
            lastAction = action;
            lastCall = call;
            callNum++;
        }
    }

    private class StrategyStub implements Strategy {
        int processCallNum = 0;
        int stopCallNum = 0;
        ElevatorState lastState;
        Transporter lastTransporter;
        Router lastRouter;

        @Override
        public void processCalls(ElevatorState state, Transporter transporter, Router router) {
            lastState = state;
            lastTransporter = transporter;
            lastRouter = router;
            processCallNum++;
        }

        @Override
        public void processStop(ElevatorState state, Transporter transporter, Router router) {
            lastState = state;
            lastTransporter = transporter;
            lastRouter = router;
            stopCallNum++;
        }
    }
}