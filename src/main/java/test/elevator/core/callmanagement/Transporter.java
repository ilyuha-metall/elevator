package test.elevator.core.callmanagement;

import test.elevator.config.ConfStore;
import test.elevator.exceptions.WrongMovingException;

import java.util.*;

public class Transporter {
    private int position;
    private final List<Integer> route = new LinkedList<>();

    public Transporter(int defaultPosition) {
        this.position = defaultPosition;
    }

    public void moveUp(){
        if(position + 1 > ConfStore.getConf().getIntParam(ConfStore.TOP_FLOOR))
            throw new WrongMovingException(position, position+1);

        position++;
    }

    public void moveDown(){
        if(position - 1 < ConfStore.getConf().getIntParam(ConfStore.BOTTOM_FLOOR))
            throw new WrongMovingException(position, position-1);

        position--;
    }

    public int getPosition() {
        return position;
    }

    public List<Integer> getRoute() {
        return route;
    }
}
