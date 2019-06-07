package sample.controller;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.Size;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainStageController {
    @FXML
    public ImageView logoCamerImageVeiw;
    @FXML
    public Label phoneNumber;
    @FXML
    public Label logOutLabel;
    public ProgressBar memoryProgressBar;
    @FXML
    public AnchorPane headerRow1;
    @FXML
    public AnchorPane headerRow2;
    @FXML
    public Label fraction;
    @FXML
    public AnchorPane download;
    @FXML
    public AnchorPane delete;
    @FXML
    public AnchorPane share;
    @FXML
    public ImageView downloadImageView;
    @FXML
    public ImageView deleteImageView;
    @FXML
    public ImageView shareImageView;
    @FXML
    public CellController cellController;
    public  Stage mainStage;


    public void initialize() {
        cellController.scrolPane.setLayoutX(15);

//        vboxContainer.setPrefHeight(500);
        memoryProgressBar.setProgress(125.0 / 400.0);
        share.setEffect(new DropShadow(19, Color.rgb(0, 0, 0, 0.1)));
        download.setEffect(new DropShadow(19, Color.rgb(0, 0, 0, 0.1)));
        delete.setEffect(new DropShadow(19, Color.rgb(0, 0, 0, 0.1)));
        headerRow1.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.2)));
        headerRow2.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.2)));
//        headerRow2.setEffect(new DropShadow(19, Color.rgb(0, 0, 0, 0.9)));

    }


    public void responsivWidth(double stageWith) {

        if (stageWith <= 1000.0) {
            memoryProgressBar.setPrefWidth(Size.WIDTH_COEFFICENT_FOR_PROGRESS_BAR_WIDTH * stageWith);
            fraction.setLayoutX(Size.WIDTH_COEFFICENT_FOR_PROGRESS_BAR_WIDTH * stageWith + memoryProgressBar.getLayoutX() + 7);
        }
        logoCamerImageVeiw.setLayoutX(Size.WIDTH_COEFFICENT_FOR_CAMERA_LOGO_CAMERA * stageWith);
        phoneNumber.setLayoutX(stageWith - 210);
        logOutLabel.setLayoutX(stageWith - 73);
        share.setLayoutX(stageWith - 299);
        delete.setLayoutX(stageWith - 202);
        download.setLayoutX(stageWith - 105);
        cellController.scrolPane.setPrefWidth(stageWith);
        ObservableList<Node> children = cellController.gridPane.getChildren();
        for (Node child : children) {
            AnchorPane itemAnchorPane = (AnchorPane)child;
            itemAnchorPane.setPrefWidth(300);
        }

        cellController.scrolPaneCell.setPrefWidth(stageWith);
    }


}
