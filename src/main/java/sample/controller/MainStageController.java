package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.Size;
import sample.service.MainStageService;
import sample.service.serviceImpl.MainStageServiceImpl;

import java.io.IOException;

public class MainStageController {
    @FXML
    public ImageView logoCamerImageVeiw;
    @FXML
    public Label phoneNumber;
    @FXML
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
    public SlideController slideController;
    @FXML
    public Label imageCountInto;

    public  Stage mainStage;
    @FXML
    public ImageView recycleImageView;
    @FXML
    public ImageView logoutImageView;
    @FXML
    public AnchorPane logOut_btn;
    @FXML
    public AnchorPane recycle_btn;
    @FXML
    public AnchorPane cell_containerAnchorPane;
    @FXML
    public AnchorPane filterPane;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public AnchorPane pageNumbersPane;
    @FXML
    public FlowPane floxPane;
    @FXML
    public Label memoryHint;
    @FXML
    public AnchorPane CONTENET;
    @FXML
    public AnchorPane mainContent;
    @FXML
    public Label currentPageNumber;

    //services
    private MainStageService mainStageService = MainStageServiceImpl.getInstance();

    public void initialize() {
        floxPane.setPrefHeight(581);
        floxPane.setPrefWidth(897);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        recycleImageView.setImage(new Image(this.getClass().getResourceAsStream("/image/recicleBin.png")));
        logoutImageView.setImage(new Image(this.getClass().getResourceAsStream("/image/logout.png")));
        mainStageService.initializeMainStageController(this);
        slideController.sliderContent.setVisible(false);
        memoryProgressBar.setProgress(125.0 / 400.0);
        share.setEffect(new DropShadow(19, Color.rgb(0, 0, 0, 0.1)));
        download.setEffect(new DropShadow(19, Color.rgb(0, 0, 0, 0.1)));
        delete.setEffect(new DropShadow(19, Color.rgb(0, 0, 0, 0.1)));
        headerRow1.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.2)));
        headerRow2.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.2)));
    }

    public void responsivWidth(double stageWith) {
        mainStageService.responsivWidth(stageWith);
    }

    public void responsivHeight(double stageHeight) {

        cell_containerAnchorPane.setPrefHeight(mainContent.getHeight()-179);
        scrollPane.setPrefHeight(mainContent.getHeight()-242);
        floxPane.setPrefHeight(mainContent.getHeight()-242);

//        pageNumbersPane.setLayoutY(floxPane.getLayoutY()+floxPane.getPrefHeight());
        pageNumbersPane.setLayoutY(mainContent.getHeight()-50);

        slideController.responsiveHeght(stageHeight);
    }

    @FXML
    public void logOut(MouseEvent mouseEvent) throws IOException {
        mainStageService.logOut();
    }
}
