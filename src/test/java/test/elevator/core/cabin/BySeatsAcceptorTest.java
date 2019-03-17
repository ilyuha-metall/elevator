package test.elevator.core.cabin;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import test.elevator.core.callmanagement.Call;
import test.elevator.core.callmanagement.Direction;

public class BySeatsAcceptorTest {
    private final Call callStub = new Call(0, Direction.UP);


    @Test
    public void testAcceptCorrectNull() {
        BySeatsAcceptor acceptor = new BySeatsAcceptor(1);
        Assert.assertTrue(acceptor.canAccept(null));
        acceptor.accept(null);
        Assert.assertFalse(acceptor.canAccept(null));
    }

    @Test
    public void testAcceptCorrect1() {
        BySeatsAcceptor acceptor = new BySeatsAcceptor(1);
        Assert.assertTrue(acceptor.canAccept(callStub));
        acceptor.accept(callStub);
        Assert.assertFalse(acceptor.canAccept(callStub));
    }

    @Test
    public void testAcceptCorrect5() {
        BySeatsAcceptor acceptor = new BySeatsAcceptor(5);
        for (int i = 0; i < 5; i++) {
            Assert.assertTrue(acceptor.canAccept(callStub));
            acceptor.accept(callStub);
        }
        Assert.assertFalse(acceptor.canAccept(callStub));
    }

    @Test
    public void testReleaseCorrect1() {
        BySeatsAcceptor acceptor = new BySeatsAcceptor(1);
        acceptor.accept(callStub);
        acceptor.release(callStub);
        Assert.assertTrue(acceptor.canAccept(callStub));
    }

    @Test
    public void testAcceptReleaseCorrect5() {
        BySeatsAcceptor acceptor = new BySeatsAcceptor(5);
        for (int i = 0; i < 5; i++) {
            acceptor.accept(callStub);
        }
        for (int i = 0; i < 5; i++) {
            acceptor.release(callStub);
        }
        Assert.assertTrue(acceptor.canAccept(callStub));
    }


}