package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import sample.connection.ApiConnection;
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
    MainStageController mainStageController;



    public void initializeMainStageController(MainStageController mainStageController){

        this.mainStageController = mainStageController;
    }


    public void initialize(

    ){

        recycleBinService.initializeRecycleController(this);
    }




    public void responsiveHeight(double height){
        recicleBin.setPrefHeight(height);
    }

    @FXML
    public void backToMain(MouseEvent mouseEvent) {
        String text = mainStageController.currentPageNumber.getText();
        ApiConnection.getInstance().loadBaseData(text);
        mainStageController.recycleBinContainer.setVisible(false);



    }
}
