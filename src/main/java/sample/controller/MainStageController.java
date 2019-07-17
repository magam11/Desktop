package sample.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Main;
import sample.connection.ApiConnection;
import sample.model.viewModel.FilterMonth;
import sample.service.MainStageService;
import sample.service.serviceImpl.MainStageServiceImpl;
import sample.service.serviceImpl.SliderServiceImpl;

import java.io.IOException;
import java.time.LocalDate;

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
    //    @FXML
//    public Label selectAllHint;
    @FXML
    public CheckBox selectALL_checkBox;
    @FXML
    public Label deleteTxt;
    //    @FXML
//    public Label cancel;
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
    public DatePicker fromDate;

    @FXML
    public Button fromDateCancel;
    public Button toDateCancel;
    @FXML
    public AnchorPane recycleBinContainer;
    @FXML
    public AnchorPane header;
    @FXML
    public AnchorPane row_1;
    @FXML
    public AnchorPane row_2;
    @FXML
    public AnchorPane slideContainer;
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


    StringProperty startDate = new SimpleStringProperty();
    StringProperty endDate = new SimpleStringProperty();


    private Text title;


    //services
    private MainStageService mainStageService = MainStageServiceImpl.getInstance();

    public void initialize() {
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
//        slideController.sliderContent.setVisible(false);
        memoryProgressBar.setProgress(125.0 / 400.0);
        download.setEffect(new DropShadow(19, Color.rgb(0, 0, 0, 0.1)));
        delete.setEffect(new DropShadow(19, Color.rgb(0, 0, 0, 0.1)));
        header.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.2)));
        row_1.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.2)));
        row_2.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.2)));
        filterYear.valueProperty().addListener((observable, oldValue, newValue) -> {
            mainStageService.setFilterAction("NEW_FILTER");
        });
        filterMonth.valueProperty().addListener((observable, oldValue, newValue) -> {
            mainStageService.setFilterAction("NEW_FILTER");
        });
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
            mainStageService.showCheckBoxes();
            isShowCheckBox.set(false);
        } else {
            mainStageService.downloadSelectedImages();
//            action
        }
    }

    @FXML
    public void cancelFromDate(MouseEvent mouseEvent) {
        fromDateCancel.setVisible(false);
        startDate.setValue(null);
        fromDate.setValue(null);

    }

    @FXML
    public void cancelToDate(MouseEvent mouseEvent) {
        toDateCancel.setVisible(false);
        endDate.setValue(null);
        toDate.setValue(null);

    }




    @FXML
    public void openRecycleBinPage(MouseEvent mouseEvent) {
        AnchorPane recycle = (AnchorPane) Main.getScreen("recycle");
        Stage mainStage = (Stage) header.getScene().getWindow();
        mainPane.getChildren().add(1, recycle);
        recycle.setLayoutX(0);
        recycle.setLayoutY(0);
        recycle.setPrefWidth(mainPane.getWidth());
        recycle.setPrefHeight(mainPane.getHeight());
        ApiConnection.getInstance().getDeletedImagePage(1);

    }


    public void fillerClick(MouseEvent mouseEvent) {
        mainStageService.filterClick();


    }
}

