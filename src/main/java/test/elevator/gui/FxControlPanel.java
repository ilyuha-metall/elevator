package test.elevator.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Optional;

public class FxControlPanel {

    @FXML
    public Label floorLabel;
    private int floorNumber;

    private FxInterfaceController callController;

    public void setFloorLabel(int number){
        floorNumber = number;
        floorLabel.setText(Integer.toString(number));
    }

    public void setCallController (FxInterfaceController controller){
        callController = controller;
    }

    @FXML
    public void onUp(){
        Optional.ofNullable(callController).ifPresent(controller -> controller.onCall(floorNumber, FxDirection.UP));
    }

    @FXML
    public void onDown(){
        Optional.ofNullable(callController).ifPresent(controller -> controller.onCall(floorNumber, FxDirection.DOWN));
    }
}
