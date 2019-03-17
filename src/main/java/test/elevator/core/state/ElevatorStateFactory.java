package test.elevator.core.state;

import test.elevator.exceptions.UnknownStateId;

import java.util.HashMap;
import java.util.Map;

public class ElevatorStateFactory {
    public static final String STOP_STATE = "stop_state";
    public static final String UP_STATE = "up_state";
    public static final String DOWN_STATE = "down_stat";

    private static final Map<String, ElevatorState> cachedStates = new HashMap<>();

    private ElevatorStateFactory() {
    }

    public static ElevatorState getState(String state){
        if(!cachedStates.containsKey(state)){
            switch (state) {
                case STOP_STATE :
                    cachedStates.put(STOP_STATE, new StopState());
                    break;
                case UP_STATE:
                    cachedStates.put(UP_STATE, new UpState());
                    break;
                case DOWN_STATE:
                    cachedStates.put(DOWN_STATE, new DownState());
                    break;
                default:
                    throw new UnknownStateId(state);
            }
        }

        return cachedStates.get(state);

    }
}
