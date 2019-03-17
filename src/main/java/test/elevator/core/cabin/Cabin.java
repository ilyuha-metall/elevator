package test.elevator.core.cabin;

import test.elevator.core.callmanagement.Call;

import java.util.ArrayList;
import java.util.List;

public class Cabin {

    private final List<CabinAcceptor> acceptors = new ArrayList<>();

    public void addCabinAcceptor(CabinAcceptor acceptor){
        acceptors.add(acceptor);
    }

    public boolean canAccept(Call call){
        return acceptors.stream().allMatch(cabinAcceptor -> cabinAcceptor.canAccept(call));
    }
    public void accept(Call call){
        acceptors.forEach(cabinAcceptor -> cabinAcceptor.accept(call));
    }
    public void release(Call call){
        acceptors.forEach(cabinAcceptor -> cabinAcceptor.release(call));
    }
}
