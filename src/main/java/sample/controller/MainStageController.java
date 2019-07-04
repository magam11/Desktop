package sample.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.util.Callback;
import sample.connection.ApiConnection;
import sample.service.MainStageService;
import sample.service.serviceImpl.MainStageServiceImpl;
import sample.service.serviceImpl.RecycleBinServiceImpl;

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
    public FlowPane flowPane;
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
    public DatePicker fromDate;
    @FXML
    public Label filterHint;
    @FXML
    public Button fromDateCancel;
    public Button toDateCancel;
    @FXML
    public AnchorPane recycleBinContainer;
    StringProperty startDate = new SimpleStringProperty();
    StringProperty endDate = new SimpleStringProperty();
    @FXML
    public RecycleBinController recycleBinController;

    private Text title;


    //services
    private MainStageService mainStageService = MainStageServiceImpl.getInstance();

    public void initialize() {
        RecycleBinServiceImpl.getInstance().initializeMainStageController(this);
        title = new Text(recycleBinController.recycleTitle.getText());
        recycleBinController.initializeMainStageController(this);
        fromDateCancel.setVisible(false);
        toDateCancel.setVisible(false);
        fromDateCancel.setStyle("-fx-background-image: url('/image/cancelFilter.png');-fx-background-repeat: no-repeat;" +
                "    -fx-background-position: center; -fx-background-size: 25px 25px;-fx-cursor: hand");
        toDateCancel.setStyle("-fx-background-image: url('/image/cancelFilter.png');-fx-background-repeat: no-repeat;" +
                "    -fx-background-position: center; -fx-background-size: 25px 25px;-fx-cursor: hand");
        search.setVisible(false);
        fromDate.valueProperty().addListener((observable, oldValue, newValue) -> {

            if(newValue!=null){
                startDate.setValue(newValue.toString());
                search.setVisible(true);
                fromDateCancel.setVisible(true);
            }
            else {
                startDate.setValue(null);
                if(endDate.get()==null)
                    search.setVisible(false);
            }
        });
        toDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                endDate.setValue(newValue.toString());
                search.setVisible(true);
                toDateCancel.setVisible(true);
            }else {
                endDate.setValue(null);
                if(startDate.get()==null)
                    search.setVisible(false);
            }
        });
        fromDate.setPromptText("From");
        toDate.setPromptText("To");
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {

                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.equals(LocalDate.now())) {
                            setStyle("-fx-background-color: #dbffb7;");
                        }
                    }
                };
            }
        };

//        DatePicker picker = new DatePicker();
        toDate.setDayCellFactory(dayCellFactory);
        fromDate.setDayCellFactory(dayCellFactory);


        cancel.setVisible(false);
        selectALL_checkBox.setText("");
        selectAllHint.setVisible(false);
        selectALL_checkBox.setVisible(false);
        flowPane.setPrefHeight(581);
        flowPane.setPrefWidth(897);
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
        headerRow2.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.2)));
    }

    public void responsivWidth(double stageWith) {
        fromDate.setLayoutX(stageWith - 298);
        filterHint.setLayoutX(stageWith - 400);
        fromDateCancel.setLayoutX(stageWith-216);
        toDateCancel.setLayoutX(stageWith-85);
        search.setLayoutX(stageWith - 51);
        toDate.setLayoutX(stageWith - 167);
        toDate.setFocusTraversable(false);
        mainStageService.responsivWidth(stageWith);

        recycleBinContainer.setPrefWidth(stageWith);
        recycleBinController.bin_scroll.setPrefWidth(stageWith);
        recycleBinController.bin_flowPane.setPrefWidth(stageWith);
        recycleBinController.recicleBin.setPrefWidth(stageWith);
        recycleBinController.bin_header.setPrefWidth(stageWith);
        recycleBinController.bin_pagination.setPrefWidth(stageWith);

        recycleBinController.recycleTitle.setLayoutX((stageWith- title.getLayoutBounds().getWidth())/2);
        recycleBinController.countData.setLayoutX(stageWith/2-3);
    }

    public void responsivHeight(double stageHeight) {
        cell_containerAnchorPane.setPrefHeight(mainContent.getHeight() - 179);
        scrollPane.setPrefHeight(mainContent.getHeight() - 220);
        flowPane.setPrefHeight(mainContent.getHeight() - 220);
        slideController.responsiveHeght(stageHeight);

        recycleBinController.recicleBin.setPrefHeight(stageHeight);
        recycleBinController.recicleBin.setPrefHeight(stageHeight);
        recycleBinController.recicleBin.setPrefHeight(stageHeight);
        recycleBinController.bin_scroll.setPrefHeight(stageHeight-118);
        recycleBinController.bin_flowPane.setPrefHeight(stageHeight-118);
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
    public void filter(MouseEvent mouseEvent) {
        ApiConnection.getInstance().getDataByInterval(startDate.get(),endDate.get(),1);
        System.out.println("startdate "+startDate.get());
        System.out.println("endDate "+endDate.get());
    }


    @FXML
    public void openRecycleBinPage(MouseEvent mouseEvent) {
        recycleBinContainer.setVisible(true);
        ApiConnection.getInstance().getDeletedImagePage(1);

    }
}
