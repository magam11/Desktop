package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.Size;
import sample.service.MainStageService;
import sample.service.serviceImpl.MainStageServiceImpl;

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
    @FXML
    public SlideController slideController;
    @FXML
    public Label imageCountInto;

    public  Stage mainStage;

    //services
    private MainStageService mainStageService = MainStageServiceImpl.getInstance();

    public void initialize() {
        mainStageService.initializeMainStageController(this);
        cellController.initializeMainStageController(this);
        slideController.sliderContent.setVisible(false);
        cellController.scrollPane.setLayoutX(15);
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
        slideController.responsiveHeght(stageHeight);
    }
}
