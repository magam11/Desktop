package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import sample.service.RecycleBinService;
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

    RecycleBinService recycleBinService = RecycleBinServiceImpl.getInstance();
    MainStageController mainStageController;



    public void initializeMainStageController(MainStageController mainStageController){

        this.mainStageController = mainStageController;
    }


    public void initialize(){
        recycleBinService.initializeRecycleController(this);
    }




    public void responsiveHeight(double height){
        recicleBin.setPrefHeight(height);
    }

    @FXML
    public void backToMain(MouseEvent mouseEvent) {
        mainStageController.recycleBinContainer.setVisible(false);


    }
}
