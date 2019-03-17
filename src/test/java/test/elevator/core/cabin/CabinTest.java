package test.elevator.core.cabin;

import org.junit.Assert;
import org.junit.Test;
import test.elevator.core.callmanagement.Call;
import test.elevator.core.callmanagement.Direction;

public class CabinTest {
    private final Call callStub1 = new Call(0, Direction.UP);
    private final Call callStub2 = new Call(1, Direction.DOWN);

    @Test
    public void testNoAcceptors() {
        Cabin cabin = new Cabin();
        Assert.assertTrue(cabin.canAccept(callStub1));
    }

    @Test
    public void testWithAcceptors1True() {
        Cabin cabin = new Cabin();
        cabin.addCabinAcceptor(new CabinAcceptorStub(true));
        Assert.assertTrue(cabin.canAccept(callStub1));
    }

    @Test
    public void testWithAcceptors1False() {
        Cabin cabin = new Cabin();
        cabin.addCabinAcceptor(new CabinAcceptorStub(false));
        Assert.assertFalse(cabin.canAccept(callStub1));
    }

    @Test
    public void testWithAcceptors5() {
        Cabin cabin = new Cabin();
        for (int i = 0; i < 5; i++)
            cabin.addCabinAcceptor(new CabinAcceptorStub(true));
        Assert.assertTrue(cabin.canAccept(callStub1));
    }

    @Test
    public void testWithAcceptors5False() {
        Cabin cabin = new Cabin();
        for (int i = 0; i < 5; i++)
            cabin.addCabinAcceptor(new CabinAcceptorStub(false));
        Assert.assertFalse(cabin.canAccept(callStub1));
    }

    @Test
    public void testWithAcceptors5TrueLastFalse() {
        Cabin cabin = new Cabin();

        for (int i = 0; i < 5; i++)
            cabin.addCabinAcceptor(new CabinAcceptorStub(true));
        cabin.addCabinAcceptor(new CabinAcceptorStub(false));
        Assert.assertFalse(cabin.canAccept(callStub1));
    }

    @Test
    public void testWithAcceptors5TrueFirstFalse() {
        Cabin cabin = new Cabin();

        cabin.addCabinAcceptor(new CabinAcceptorStub(false));
        for (int i = 0; i < 5; i++)
            cabin.addCabinAcceptor(new CabinAcceptorStub(true));

        Assert.assertFalse(cabin.canAccept(callStub1));
    }

    @Test
    public void testCalling() {
        Cabin cabin = new Cabin();
        CabinAcceptorStub acceptor = new CabinAcceptorStub(false);
        cabin.addCabinAcceptor(acceptor);
        Assert.assertEquals(0, acceptor.getCanCallNum());
        Assert.assertEquals(0, acceptor.getAcceptCallNum());
        Assert.assertEquals(0, acceptor.getReleaseCallNum());

        cabin.canAccept(callStub1);
        Assert.assertEquals(1, acceptor.getCanCallNum());
        Assert.assertEquals(0, acceptor.getAcceptCallNum());
        Assert.assertEquals(0, acceptor.getReleaseCallNum());
        Assert.assertEquals(callStub1, acceptor.getLastArg());

        cabin.canAccept(callStub2);
        Assert.assertEquals(2, acceptor.getCanCallNum());
        Assert.assertEquals(0, acceptor.getAcceptCallNum());
        Assert.assertEquals(0, acceptor.getReleaseCallNum());
        Assert.assertEquals(callStub2, acceptor.getLastArg());

        cabin.accept(callStub1);
        Assert.assertEquals(2, acceptor.getCanCallNum());
        Assert.assertEquals(1, acceptor.getAcceptCallNum());
        Assert.assertEquals(0, acceptor.getReleaseCallNum());
        Assert.assertEquals(callStub1, acceptor.getLastArg());

        cabin.accept(callStub2);
        Assert.assertEquals(2, acceptor.getCanCallNum());
        Assert.assertEquals(2, acceptor.getAcceptCallNum());
        Assert.assertEquals(0, acceptor.getReleaseCallNum());
        Assert.assertEquals(callStub2, acceptor.getLastArg());

        cabin.release(callStub1);
        Assert.assertEquals(2, acceptor.getCanCallNum());
        Assert.assertEquals(2, acceptor.getAcceptCallNum());
        Assert.assertEquals(1, acceptor.getReleaseCallNum());
        Assert.assertEquals(callStub1, acceptor.getLastArg());

        cabin.release(callStub2);
        Assert.assertEquals(2, acceptor.getCanCallNum());
        Assert.assertEquals(2, acceptor.getAcceptCallNum());
        Assert.assertEquals(2, acceptor.getReleaseCallNum());
        Assert.assertEquals(callStub2, acceptor.getLastArg());
    }

    @Test
    public void testCallingNull() {
        Cabin cabin = new Cabin();
        CabinAcceptorStub acceptor = new CabinAcceptorStub(true);
        cabin.addCabinAcceptor(acceptor);

        cabin.canAccept(null);
        Assert.assertNull(acceptor.getLastArg());
        cabin.accept(null);
        Assert.assertNull(acceptor.getLastArg());
        cabin.release(null);
        Assert.assertNull(acceptor.getLastArg());
    }

    private class CabinAcceptorStub implements CabinAcceptor {
        private final boolean condition;
        private int acceptCallNum = 0;
        private int releaseCallNum = 0;
        private int canCallNum = 0;
        private Call lastArg;

        private CabinAcceptorStub(boolean condition) {
            this.condition = condition;
        }

        @Override
        public boolean canAccept(Call call) {
            lastArg = call;
            canCallNum++;
            return condition;
        }

        @Override
        public void accept(Call call) {
            acceptCallNum++;
            lastArg = call;
        }

        @Override
        public void release(Call call) {
            releaseCallNum++;
            lastArg = call;
        }

        public int getAcceptCallNum() {
            return acceptCallNum;
        }

        public int getReleaseCallNum() {
            return releaseCallNum;
        }

        public int getCanCallNum() {
            return canCallNum;
        }

        public Call getLastArg() {
            return lastArg;
        }
    }
}