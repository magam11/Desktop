package sample.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.Main;
import sample.connection.ApiConnection;
import sample.model.animation.OpeningAnimation;
import sample.model.viewModel.FilterMonth;
import sample.service.MainStageService;
import sample.service.serviceImpl.MainStageServiceImpl;
import sample.service.serviceImpl.SliderServiceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MainStageController {
    @FXML
    public ImageView logoCamerImageVeiw;
    @FXML
    public Label phoneNumber;
    @FXML
    public ProgressBar memoryProgressBar;

    @FXML
    public Label fraction;
    @FXML
    public AnchorPane download;
    @FXML
    public AnchorPane delete;

    @FXML
    public ImageView downloadImageView;
    @FXML
    public ImageView deleteImageView;
    @FXML
    public SlideController slideController;
//    @FXML
//    public Label imageCountInto;

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
    public FlowPane flowPane;
    @FXML
    public Label memoryHint;
    @FXML
    public AnchorPane mainContent;
    @FXML
    public Label currentPageNumber;
    @FXML
    public CheckBox selectALL_checkBox;
    @FXML
    public Label deleteTxt;
    @FXML
    public Label downloadTxt;
    @FXML
    public BooleanProperty isShowCheckBox = new SimpleBooleanProperty(true); //else go to action
    @FXML
    public StackPane mainPane;
    @FXML
    public AnchorPane header;
    @FXML
    public AnchorPane row_1;
    @FXML
    public AnchorPane row_2;
    @FXML
    public ImageView filterImage;
    @FXML
    public AnchorPane filterButton;
    @FXML
    public Button filterBtn;
    @FXML
    public ChoiceBox filterYear;
    @FXML
    public ChoiceBox filterMonth;
    @FXML
    public ImageView phoneCell;
    @FXML
    public Label logOut;
    @FXML
    public Label recycleTxt;
    Font openSans = null;
    private static OpeningAnimation openingAnimation;
    private LocalDateTime lastScroledDate;


    //services
    private MainStageService mainStageService = MainStageServiceImpl.getInstance();

    private  void showPagination(ScrollEvent scrolEvent) {
        openingAnimation.stop();
        openingAnimation.start();
    }

    public void initialize() {
        flowPane.setOnScroll(this::showPagination);
//        scrollPane.setOnScroll(this::showPagination);
        openSans = Font.loadFont( getClass().getResourceAsStream("/font/OpenSans-Regular.ttf"),16);
        logOut.setFont(openSans);
        memoryHint.setFont(openSans);
        phoneNumber.setFont(openSans);
        memoryHint.setFont(openSans);
        recycleTxt.setFont(openSans);
        openSans = Font.loadFont( getClass().getResourceAsStream("/font/OpenSans-Regular.ttf"),14);
        deleteTxt.setFont(openSans);
        downloadTxt.setFont(openSans);
        filterBtn.setFont(openSans);
        phoneCell.setImage(new Image(getClass().getResourceAsStream("/image/phone-call.png")));
        filterMonth.setValue(FilterMonth.ALL);

        filterMonth.getItems().addAll(FilterMonth.list());
        SliderServiceImpl.getInstance().initializeMainStageController(this);
        LocalDate localDate = LocalDate.now();
        int currentYear = localDate.getYear();
        filterYear.setValue(currentYear);
        for (int i = currentYear; i >= 2019; i--) {
            filterYear.getItems().add(i);
        }
        selectALL_checkBox.setVisible(false);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        recycleImageView.setImage(new Image(this.getClass().getResourceAsStream("/image/recicleBin.png")));
        logoutImageView.setImage(new Image(this.getClass().getResourceAsStream("/image/logout.png")));
        mainStageService.initializeMainStageController(this);
        memoryProgressBar.setProgress(125.0 / 400.0);
        download.setEffect(new DropShadow(19, Color.rgb(0, 0, 0, 0.1)));
        delete.setEffect(new DropShadow(19, Color.rgb(0, 0, 0, 0.1)));
        header.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.25)));
        memoryProgressBar.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.1)));
//        row_1.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.2)));
//        row_2.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.2)));
        filterYear.valueProperty().addListener((observable, oldValue, newValue) -> {
            mainStageService.setFilterAction("NEW_FILTER");
        });
        filterMonth.valueProperty().addListener((observable, oldValue, newValue) -> {
            mainStageService.setFilterAction("NEW_FILTER");
        });
        openingAnimation = OpeningAnimation.getInstance();
    }



    @FXML
    public void logOut(MouseEvent mouseEvent) throws IOException {
        mainStageService.logOut();
    }

    @FXML
    public void showCheckBoxesՕrDelete(MouseEvent mouseEvent) {
        if (isShowCheckBox.get()) {
            download.setDisable(true);
            download.setStyle("-fx-cursor: default;-fx-background-radius: 5; -fx-background-color: #FFFFFF;");
            delete.setStyle("-fx-cursor: hand;-fx-background-radius: 5; -fx-background-color: #388e3c;");
            deleteTxt.setTextFill(Paint.valueOf("#FAFAFA"));
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
    public void downloadSelectedImage(MouseEvent mouseEvent) {
        if (isShowCheckBox.get()) {
            delete.setDisable(true);
            downloadTxt.setTextFill(Paint.valueOf("#FAFAFA"));
            delete.setStyle("-fx-cursor: default;-fx-background-radius: 5; -fx-background-color: #FFFFFF;");
            download.setStyle("-fx-cursor: hand;-fx-background-radius: 5; -fx-background-color: #388e3c;");
            mainStageService.showCheckBoxes();
            isShowCheckBox.set(false);
        } else {
            mainStageService.downloadSelectedImages();
//            action
        }
    }




    @FXML
    public void openRecycleBinPage(MouseEvent mouseEvent) {
        AnchorPane recycle = (AnchorPane) Main.getScreen("recycle");
        Stage mainStage = (Stage) header.getScene().getWindow();
        header.getScene().setRoot(recycle);
        recycle.setMinHeight(mainStage.getHeight());
        recycle.setMinWidth(mainStage.getWidth());
        recycle.widthProperty().add(mainStage.getWidth());
        recycle.heightProperty().add(mainStage.getHeight());
//        StackPane loader = (StackPane) Main.getScreen("loader");

        BorderPane loader = (BorderPane) Main.getScreen("loader");
        loader.setPrefWidth(mainStage.getWidth());
        loader.setPrefHeight(mainStage.getHeight());
        mainStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            loader.setPrefWidth(newValue.doubleValue());
        });
        mainStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            loader.setPrefHeight(newValue.doubleValue());
        });

        recycle.getChildren().add(loader);
        ApiConnection.getInstance().getDeletedImagePage(1);
    }


    public void fillerClick(MouseEvent mouseEvent) {
        mainStageService.filterClick();


    }
}

