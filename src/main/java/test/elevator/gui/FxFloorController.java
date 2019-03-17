package test.elevator.gui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class FxFloorController {
    @FXML
    public GridPane floorPane;
    @FXML
    public StackPane cabinPlace;

    public void setControlPanel(Node node){
        floorPane.add(node, 1, 1);
    }

    public void setCabin(Node node){
        cabinPlace.getChildren().add(node);
    }

    public void clearCabin(){
        cabinPlace.getChildren().clear();
    }

}
