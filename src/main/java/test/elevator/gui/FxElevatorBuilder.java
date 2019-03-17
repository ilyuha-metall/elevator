package test.elevator.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import test.elevator.exceptions.WrongConfigurationException;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FxElevatorBuilder {

    private FxElevatorBuilder(){}

    public static FxElevator createElevator(int minFloor, int maxFloor, FxInterfaceController controller) {
        try {
            FXMLLoader cabinLoader = new FXMLLoader(FxElevatorBuilder.class.getResource("/forms/cabin.fxml"));
            Node cabin = cabinLoader.load();
            final Map<Integer, FxFloorController> floorControllers = new HashMap<>();
            final Map<Integer, Node> floors = new HashMap<>();
            for(int i = minFloor; i<= maxFloor; i++) {
                URL path;
                if(i==minFloor)
                    path = FxElevatorBuilder.class.getResource("/forms/controls/bottomcontrolpanel.fxml");
                else if (i==maxFloor)
                    path = FxElevatorBuilder.class.getResource("/forms/controls/topcontrolpanel.fxml");
                else
                    path = FxElevatorBuilder.class.getResource("/forms/controls/controlpanel.fxml");

                FXMLLoader  controlsLoader = new FXMLLoader(path);
                Node control = controlsLoader.load();
                FxControlPanel panelController = controlsLoader.getController();
                panelController.setFloorLabel(i);
                panelController.setCallController(controller);

                FXMLLoader  floorLoader = new FXMLLoader(FxElevatorBuilder.class.getResource("/forms/floor.fxml"));
                Node floor = floorLoader.load();
                FxFloorController floorController = floorLoader.getController();


                floorController.setControlPanel(control);
                floorControllers.put(i, floorController);
                floors.put(i, floor);
            }

            FXMLLoader elevatorLoader = new FXMLLoader(FxElevatorBuilder.class.getResource("/forms/elevator.fxml"));
            elevatorLoader.load();
            FxElevator elevatorController = elevatorLoader.getController();
            elevatorController.setCabin(cabin);
            elevatorController.setFloorControllers(floorControllers);
            elevatorController.setFloors(floors);

            return elevatorController;
        }catch (Exception ex){
            throw new WrongConfigurationException("Can't create elevator", ex);
        }
    }
}
