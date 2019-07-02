package sample.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.service.MainStageService;
import sample.service.serviceImpl.MainStageServiceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.MonthDay;

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
    //    @FXML
//    public AnchorPane share;
    @FXML
    public ImageView downloadImageView;
    @FXML
    public ImageView deleteImageView;
    //    @FXML
//    public ImageView shareImageView;
    @FXML
    public SlideController slideController;
    @FXML
    public Label imageCountInto;

    public Stage mainStage;
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
    public AnchorPane mainContent;
    @FXML
    public Label currentPageNumber;
    @FXML
    public Label selectAllHint;
    @FXML
    public CheckBox selectALL_checkBox;
    @FXML
    public Label deleteTxt;
    @FXML
    public Label cancel;
    public Label shareTxt;
    public Label downloadTxt;
    @FXML
    public BooleanProperty isShowCheckBox = new SimpleBooleanProperty(true); //else go to action
    @FXML
    public StackPane mainPane;
    @FXML
    public ImageView search;
    @FXML
    public DatePicker toDate;
    @FXML
    public Label toHint;
    @FXML
    public DatePicker fromDate;
    @FXML
    public Label filterHint;
//    @FXML
//    public RecicleBinController recicleBinController;


    //services
    private MainStageService mainStageService = MainStageServiceImpl.getInstance();

    public void initialize() {
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {

                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (MonthDay.from(item).equals(MonthDay.of(7, 15)) &&
                                !(getStyleClass().contains("next-month") || getStyleClass().contains("previous-month"))
                        ) {
                            setTooltip(new Tooltip("Beware the Ides of March!"));
                            setStyle("-fx-background-color: #ff4444;");
                       }
//                        if(empty){
//                            setStyle("-fx-background-color: #ff4444;");
//                        }
//                        else {
//                            setTooltip(null);
//                            setStyle(null);
//                        }
                    }
                };
            }
        };

//        DatePicker picker = new DatePicker();
        toDate.setDayCellFactory(dayCellFactory);



        cancel.setVisible(false);
        selectALL_checkBox.setText("");
        selectAllHint.setVisible(false);
        selectALL_checkBox.setVisible(false);
        floxPane.setPrefHeight(581);
        floxPane.setPrefWidth(897);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        recycleImageView.setImage(new Image(this.getClass().getResourceAsStream("/image/recicleBin.png")));
        logoutImageView.setImage(new Image(this.getClass().getResourceAsStream("/image/logout.png")));
        mainStageService.initializeMainStageController(this);
        slideController.sliderContent.setVisible(false);
        memoryProgressBar.setProgress(125.0 / 400.0);
        download.setEffect(new DropShadow(19, Color.rgb(0, 0, 0, 0.1)));
        delete.setEffect(new DropShadow(19, Color.rgb(0, 0, 0, 0.1)));
        headerRow1.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.2)));
        headerRow2.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.2)));
    }

    public void responsivWidth(double stageWith) {
        fromDate.setLayoutX(stageWith-298);
        filterHint.setLayoutX(stageWith-423);
        toHint.setLayoutX(stageWith-188);
        search.setLayoutX(stageWith-51);
        toDate.setLayoutX(stageWith-167);
        mainStageService.responsivWidth(stageWith);
    }

    public void responsivHeight(double stageHeight) {
        cell_containerAnchorPane.setPrefHeight(mainContent.getHeight() - 179);
        scrollPane.setPrefHeight(mainContent.getHeight() - 220);
        floxPane.setPrefHeight(mainContent.getHeight() - 220);
        slideController.responsiveHeght(stageHeight);
    }

    @FXML
    public void logOut(MouseEvent mouseEvent) throws IOException {
        mainStageService.logOut();
    }

    @FXML
    public void showCheckBoxesՕrDelete(MouseEvent mouseEvent) {
        if (isShowCheckBox.get()) {
            download.setDisable(true);
            download.setStyle("-fx-cursor: default;-fx-background-radius: 15; -fx-background-color: #FFFFFF;");
            delete.setStyle("-fx-cursor: hand;-fx-background-radius: 15; -fx-background-color: #388e3c;");
            deleteTxt.setTextFill(Paint.valueOf("#FAFAFA"));
            cancel.setVisible(true);
            mainStageService.showCheckBoxes();
            isShowCheckBox.set(false);
        } else {
            mainStageService.deleteSelectedImages();
//            action
        }

    }

    @FXML
    public void selectOrCancelItems(MouseEvent mouseEvent) {
        mainStageService.selectOrCancelItems();
    }

    @FXML
    public void cancelSelected(MouseEvent mouseEvent) {
        mainStageService.cancelSelect();
    }

    @FXML
    public void downloadSelectedImage(MouseEvent mouseEvent) {
        if (isShowCheckBox.get()) {
            delete.setDisable(true);
            downloadTxt.setTextFill(Paint.valueOf("#FAFAFA"));
            delete.setStyle("-fx-cursor: default;-fx-background-radius: 15; -fx-background-color: #FFFFFF;");
            download.setStyle("-fx-cursor: hand;-fx-background-radius: 15; -fx-background-color: #388e3c;");
            cancel.setVisible(true);
            mainStageService.showCheckBoxes();
            isShowCheckBox.set(false);
        } else {
            mainStageService.downloadSelectedImages();
//            action
        }
    }
}
