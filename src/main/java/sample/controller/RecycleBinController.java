package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import sample.connection.ApiConnection;
import sample.service.MainStageService;
import sample.service.RecycleBinService;
import sample.service.serviceImpl.MainStageServiceImpl;
import sample.service.serviceImpl.RecycleBinServiceImpl;

public class RecycleBinController {
    @FXML
    public AnchorPane recicleBin;
    @FXML
    public AnchorPane bin_header;
    @FXML
    public AnchorPane bin_pagination;
    @FXML
    public ScrollPane bin_scroll;
    @FXML
    public FlowPane bin_flowPane;
    @FXML
    public Label countData;
    @FXML
    public Label recycleTitle;

    RecycleBinService recycleBinService = RecycleBinServiceImpl.getInstance();
     MainStageService mainStageService= MainStageServiceImpl.getInstance();






    public void initialize(

    ){

        recycleBinService.initializeRecycleController(this);
    }




    public void responsiveHeight(double height){
        recicleBin.setPrefHeight(height);
    }

    @FXML
    public void backToMain(MouseEvent mouseEvent) {
        String text = mainStageService.getMainStageController().currentPageNumber.getText();
        ApiConnection.getInstance().loadBaseData(text);
        mainStageService.getMainPane().getChildren().remove(1);



    }
}
