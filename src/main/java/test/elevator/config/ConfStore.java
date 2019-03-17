package test.elevator.config;

import test.elevator.exceptions.WrongConfigurationException;

public class ConfStore {
    public static final String TOP_FLOOR= "elevator.floor.top";
    public static final String BOTTOM_FLOOR = "elevator.floor.bottom";
    public static final String DEFAULT_POSITION = "elevator.position.default";
    public static final String CABIN_SEATS_HEAVY = "cabin.param.seats.heavy";
    public static final String CABIN_SEATS_LITE = "cabin.param.seats.lite";
    public static final String THREAD_DELAY = "application.threads.delay";
    public static final String MOVING_DELAY = "elevator.delay.moving";
    public static final String SWITCHING_DELAY = "elevator.delay.switching";

    private static Configuration configuration;

    private ConfStore(){}

    public static void setConfiguration(Configuration configuration){
        ConfStore.configuration = configuration;
    }

    public static Configuration getConf(){
        if(ConfStore.configuration == null)
            throw new WrongConfigurationException("Configuration not initialised!");
        return ConfStore.configuration;
    }
}
