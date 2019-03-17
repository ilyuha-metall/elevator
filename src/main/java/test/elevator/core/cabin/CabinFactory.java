package test.elevator.core.cabin;

import test.elevator.config.ConfStore;

public class CabinFactory {
    public enum CabinType {
        HEAVY,
        LITE
    }

    private CabinFactory(){}

    public static Cabin createCabin(CabinType type) {
        Cabin cabin = new Cabin();
        int seats;
        switch (type) {
            case LITE:
                seats = ConfStore.getConf().getIntParam(ConfStore.CABIN_SEATS_LITE);
                break;
            case HEAVY:
                seats = ConfStore.getConf().getIntParam(ConfStore.CABIN_SEATS_HEAVY);
                break;
            default:
                throw new IllegalArgumentException("Unknown cabin type: " + type);
        }
        cabin.addCabinAcceptor(new BySeatsAcceptor(seats));
        return cabin;
    }
}
