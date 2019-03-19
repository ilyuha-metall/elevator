package test.elevator.core.callmanagement;

import test.elevator.actions.ActionObserver;
import test.elevator.actions.ElevatorActions;
import test.elevator.core.state.ElevatorState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Router {
    private final List<Call> availableCalls = Collections.synchronizedList(new LinkedList<>());
    private final List<Call> acceptedCalls = new ArrayList<>();
    private final List<ActionObserver> actionObservers = new ArrayList<>();
    private final List<CallAcceptanceCheck> additionChecks = new ArrayList<>();

    private final Strategy strategy;

    public Router(Strategy strategy) {
        this.strategy = strategy;
    }

    public void processCalls(ElevatorState state, Transporter transporter){
        strategy.processCalls(state, transporter, this);
        availableCalls.removeAll(acceptedCalls);
    }

    public void processStop(ElevatorState state, Transporter transporter){
        strategy.processStop(state, transporter, this);
    }

    public void addCall(Call call){
        availableCalls.add(call);
        actionObservers.forEach(observer -> observer.onAction(ElevatorActions.CALL_APPEARED, call));
    }

    public void removeCall(Call call){
        availableCalls.remove(call);
    }

    public List<Call> getAvailableCalls() {
        return Collections.unmodifiableList(availableCalls);
    }

    public boolean canAccept(Call call) {
        return additionChecks.stream().allMatch(check -> check.canAccept(call));
    }

    public void acceptCall(Call call) {
        acceptedCalls.add(call);
        actionObservers.forEach(observer -> observer.onAction(ElevatorActions.CALL_ACCEPTED, call));
    }

    public void finishCall(Call call) {
        acceptedCalls.remove(call);
        actionObservers.forEach(observer -> observer.onAction(ElevatorActions.CALL_FINISHED, call));
    }

    public List<Call> getAcceptedCalls() {
        return Collections.unmodifiableList(acceptedCalls);
    }

    public void addActionObserver(ActionObserver observer){
        actionObservers.add(observer);
    }

    public void addAdditionCheck(CallAcceptanceCheck check){
        additionChecks.add(check);
    }
}
