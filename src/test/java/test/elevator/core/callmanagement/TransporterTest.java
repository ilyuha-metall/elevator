package test.elevator.core.callmanagement;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import test.elevator.config.ConfStore;
import test.elevator.exceptions.WrongConfigurationException;
import test.elevator.exceptions.WrongMovingException;

import static org.junit.Assert.*;

public class TransporterTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void prepare() {
        ConfStore.setConfiguration(param -> {
            switch (param) {
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
    public void moveUpEx() {
        thrown.expect(WrongMovingException.class);
        Transporter transporter = new Transporter(10);
        transporter.moveUp();
    }

    @Test
    public void moveDownEx() {
        thrown.expect(WrongMovingException.class);
        Transporter transporter = new Transporter(0);
        transporter.moveDown();
    }

    @Test
    public void moveUp() {
        Transporter transporter = new Transporter(0);
        for (int i = 1; i < 10; i++) {
            transporter.moveUp();
            Assert.assertEquals(i, transporter.getPosition());
        }
    }

    @Test
    public void moveDown() {
        Transporter transporter = new Transporter(10);
        for (int i = 1; i < 10; i++) {
            transporter.moveDown();
            Assert.assertEquals(10 - i, transporter.getPosition());
        }
    }

    @Test
    public void getPosition() {
        for (int i = 0; i < 10; i++) {
            Transporter transporter = new Transporter(i);
            Assert.assertEquals(i, transporter.getPosition());
        }
    }
}