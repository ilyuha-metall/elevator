package test.elevator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import test.elevator.core.*;
import test.elevator.core.cabin.BySeatsAcceptor;
import test.elevator.core.cabin.Cabin;
import test.elevator.config.ConfStore;
import test.elevator.core.callmanagement.Call;
import test.elevator.core.callmanagement.Direction;
import test.elevator.core.callmanagement.Router;
import test.elevator.core.callmanagement.Transporter;
import test.elevator.exceptions.WrongConfigurationException;
import test.elevator.core.state.ElevatorStateFactory;
import test.elevator.impl.updown.UpDownOnlyStrategy;

import java.util.List;

public class ElevatorTest {

    private Elevator elevator;
    private Transporter transporter;
    private Router router;
    private Cabin cabin;

    @Before
    public void prepare(){
        ConfStore.setConfiguration(param -> {
            switch (param){
                case ConfStore.BOTTOM_FLOOR : return 0;
                case ConfStore.TOP_FLOOR : return 10;
                default: throw new WrongConfigurationException(String.format("Param %s not found.", param));
            }
        });

        cabin = new Cabin();
        cabin.addCabinAcceptor(new BySeatsAcceptor(2));

        transporter = new Transporter(0);

        router = new Router(new UpDownOnlyStrategy());

        elevator = new Elevator(cabin, transporter, router, ElevatorStateFactory.getState(ElevatorStateFactory.STOP_STATE));
        elevator.init();
    }

    @Test
    public void testSingleUpCall() {
        System.out.println("doJob");
        elevator.doJob();
        logElevatorState(transporter, router);

        router.addCall(new Call(5, Direction.UP));

        for(int i=0; i<15; i++){
            System.out.println("doJob");
            elevator.doJob();
            logElevatorState(transporter, router);
        }
        Assert.assertEquals(10, transporter.getPosition());
    }


    @Test
    public void testSingleDownCall() {
        System.out.println("doJob");
        elevator.doJob();
        logElevatorState(transporter, router);

        router.addCall(new Call(5, Direction.DOWN));

        for(int i=0; i<15; i++){
            System.out.println("doJob");
            elevator.doJob();
            logElevatorState(transporter, router);
        }
        Assert.assertEquals(0, transporter.getPosition());
    }

    @Test
    public void testUpCallWithCompanion() {
        System.out.println("doJob");
        elevator.doJob();
        logElevatorState(transporter, router);

        System.out.println("==ADD CALL");
        router.addCall(new Call(5, Direction.UP));

        System.out.println("doJob");
        elevator.doJob();
        logElevatorState(transporter, router);

        System.out.println("doJob");
        elevator.doJob();
        logElevatorState(transporter, router);

        System.out.println("==ADD CALL");
        router.addCall(new Call(6, Direction.UP));



        for(int i=0; i<15; i++){
            System.out.println("doJob");
            elevator.doJob();
            logElevatorState(transporter, router);
        }
        Assert.assertEquals(10, transporter.getPosition());
    }

    @Test
    public void testUpCallWithNoCompanion() {
        System.out.println("doJob");
        elevator.doJob();
        logElevatorState(transporter, router);

        System.out.println("==ADD CALL");
        router.addCall(new Call(5, Direction.UP));

        System.out.println("doJob");
        elevator.doJob();
        logElevatorState(transporter, router);

        System.out.println("doJob");
        elevator.doJob();
        logElevatorState(transporter, router);

        System.out.println("==ADD CALL");
        router.addCall(new Call(6, Direction.DOWN));



        for(int i=0; i<25; i++){
            System.out.println("doJob");
            elevator.doJob();
            logElevatorState(transporter, router);
        }
        Assert.assertEquals(0, transporter.getPosition());
    }

    @Test
    public void testUpCallWithEndCompanion() {
        System.out.println("doJob");
        elevator.doJob();
        logElevatorState(transporter, router);

        System.out.println("==ADD CALL");
        router.addCall(new Call(5, Direction.UP));

        System.out.println("doJob");
        elevator.doJob();
        logElevatorState(transporter, router);

        System.out.println("doJob");
        elevator.doJob();
        logElevatorState(transporter, router);

        System.out.println("==ADD CALL");
        router.addCall(new Call(10, Direction.DOWN));



        for(int i=0; i<25; i++){
            System.out.println("doJob");
            elevator.doJob();
            logElevatorState(transporter, router);
        }
        Assert.assertEquals(0, transporter.getPosition());
    }

    @Test
    public void testUpCallWithBackwardCompanion() {
        System.out.println("doJob");
        transporter.moveUp();
        transporter.moveUp();
        transporter.moveUp();
        transporter.moveUp();
        transporter.moveUp();

        elevator.doJob();
        logElevatorState(transporter, router);

        System.out.println("==ADD CALL");
        router.addCall(new Call(7, Direction.DOWN));

        System.out.println("doJob");
        elevator.doJob();
        logElevatorState(transporter, router);

        System.out.println("doJob");
        elevator.doJob();
        logElevatorState(transporter, router);

        System.out.println("==ADD CALL");
        router.addCall(new Call(3, Direction.DOWN));



        for(int i=0; i<25; i++){
            System.out.println("doJob");
            elevator.doJob();
            logElevatorState(transporter, router);
        }
        Assert.assertEquals(0, transporter.getPosition());
    }


    private void logElevatorState(Transporter context, Router router){
        int position = context.getPosition();
        List<Integer> route = context.getRoute();
        List<Call> availableCalls = router.getAvailableCalls();
        List<Call> acceptedCalls = router.getAcceptedCalls();

        System.out.println(String.format("Position: %s", position));
        System.out.println("route:");
        System.out.println(route);
        System.out.println("available callmanagement:");
        System.out.println(availableCalls);
        System.out.println("accepted callmanagement:");
        System.out.println(acceptedCalls);
        System.out.println("----------------------------------------------------------");
    }
}