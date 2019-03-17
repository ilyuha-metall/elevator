package test.elevator.core.cabin;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import test.elevator.config.ConfStore;
import test.elevator.config.Configuration;

import java.lang.reflect.Array;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CabinFactoryTest {

    @Before
    public void init() {
        ConfStore.setConfiguration(param -> {
            if (param == ConfStore.CABIN_SEATS_HEAVY)
                return 2;
            if (param == ConfStore.CABIN_SEATS_LITE)
                return 1;
            else return 0;
        });
    }

    @Test
    public void createHeavyCabin() {
        Cabin cabin = CabinFactory.createCabin(CabinFactory.CabinType.HEAVY);
        Assert.assertNotNull(cabin);
        Assert.assertTrue(cabin.canAccept(null));
        cabin.accept(null);
        Assert.assertTrue(cabin.canAccept(null));
        cabin.accept(null);
        Assert.assertFalse(cabin.canAccept(null));
    }

    @Test
    public void createLiteCabin() {
        Cabin cabin = CabinFactory.createCabin(CabinFactory.CabinType.LITE);
        Assert.assertNotNull(cabin);
        Assert.assertTrue(cabin.canAccept(null));
        cabin.accept(null);
        Assert.assertFalse(cabin.canAccept(null));
    }
}