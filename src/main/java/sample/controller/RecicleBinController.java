package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

public class RecicleBinController {
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

    public void initialize(){

    }

    public void responsiveHeight(double height){

        recicleBin.setPrefHeight(height);
    }
}
