package test.elevator.gui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.*;

public class FxElevator {
    private final Map<Integer, FxFloorController> floors = new HashMap<>();
    private Integer currentPosition;
    private Node cabin;

    @FXML
    public ScrollPane mainPane;
    @FXML
    public VBox elevator;

    public void changePosition(int newPosition) {
        Optional.ofNullable(currentPosition).ifPresent(pos -> floors.get(currentPosition).clearCabin());
        currentPosition = newPosition;
        floors.get(currentPosition).setCabin(cabin);
    }

    public void setCabin(Node cabin) {
        this.cabin = cabin;
    }

    public void setFloors(Map<Integer, Node> floors) {
        elevator.getChildren().clear();
        floors.keySet().stream().sorted(Comparator.reverseOrder())
                .forEach(num -> elevator.getChildren().add(floors.get(num)));
    }

    public void setFloorControllers(Map<Integer, FxFloorController> controllers) {
        floors.clear();
        floors.putAll(controllers);
    }
}
