package sample.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

public class CellController {

    @FXML
    public ScrollPane scrollPane;
    @FXML
    public FlowPane floxPane;
    private MainStageController mainStageController;

    public void initializeMainStageController(MainStageController mainStageController){
        this.mainStageController = mainStageController;
    }



    public void initialize(){
        floxPane.setPadding(new Insets(50,0,0,0));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    public void responsiveWidth(double stageWith) {
        floxPane.setPrefWidth(stageWith);

    }

    @FXML
    public void openSlider(MouseEvent mouseEvent) {
        mainStageController.slideController.sliderContent.setVisible(true);
    }

}
