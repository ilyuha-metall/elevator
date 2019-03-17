package test.elevator.app;

import test.elevator.core.ElevatorFacade;
import test.elevator.config.ConfStore;
import test.elevator.core.Elevator;
import test.elevator.core.callmanagement.Router;
import test.elevator.core.callmanagement.StrategyFactory;
import test.elevator.core.callmanagement.Transporter;
import test.elevator.core.cabin.Cabin;
import test.elevator.core.state.ElevatorState;
import test.elevator.core.state.ElevatorStateFactory;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    private final Map<String, ElevatorFacade> facades = new HashMap<>();
    private final Map<String, Runnable> tasks = new HashMap<>();

    public void creteElevator(String name, Cabin cabin, Transporter transporter, StrategyFactory strategyFactory) {
        ElevatorState startState = ElevatorStateFactory.getState(ElevatorStateFactory.STOP_STATE);

        Router router = new Router(strategyFactory.createStrategy());
        Elevator elevator = new Elevator(cabin, transporter, router, startState);
        elevator.init();

        ElevatorFacade facade = new ElevatorFacade(transporter, router, strategyFactory.createControlPanel());

        facades.put(name, facade);
        tasks.put(name, () -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    elevator.doJob();
                    Thread.sleep(ConfStore.getConf().getIntParam(ConfStore.THREAD_DELAY));
                } catch (InterruptedException ignore) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    public ElevatorFacade getFacadeOf(String name){
        return facades.get(name);
    }

    public Runnable getTaskOf(String name){
        return tasks.get(name);
    }
}
