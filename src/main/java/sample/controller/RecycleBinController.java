package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import sample.Main;
import sample.connection.ApiConnection;
import sample.service.MainStageService;
import sample.service.RecycleBinService;
import sample.service.serviceImpl.MainStageServiceImpl;
import sample.service.serviceImpl.NoInternetModalServiceImpl;
import sample.service.serviceImpl.RecycleBinServiceImpl;
import sample.util.Helper;

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
    @FXML
    public AnchorPane menuBar;
    @FXML
    public AnchorPane bin_delete_batch;
    @FXML
    public AnchorPane recover;
    @FXML
    public CheckBox bin_selectAll;

    private RecycleBinService recycleBinService = RecycleBinServiceImpl.getInstance();
    private MainStageService mainStageService = MainStageServiceImpl.getInstance();


    public void initialize() {
        bin_flowPane.setCache(false);
        bin_scroll.setCache(false);
//        bin_flowPane.setOnScroll(this::onScroll);
        bin_scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        bin_scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        bin_scroll.setOnScroll(this::onScroll);
//        bin_pagination.setVisible(false);
        recycleBinService.initializeRecycleController(this);

//        bin_pagination.setOnMouseEntered(event -> {
//            System.out.println("mouseEntered");
//            recycleBinService.stopAnimationForPagination();
//            bin_pagination.setOpacity(1.0);
//            bin_pagination.setVisible(true);
//        });
//        bin_pagination.setOnMouseExited(event -> {
//            System.out.println("extered");
//            recycleBinService.animationForPagination();
//        });
    }


    @FXML
    public void backToMain(MouseEvent mouseEvent) {
        if (Helper.getInstance().isInternetAvailable()) {
            NoInternetModalServiceImpl.getInstance().closeOldOpenedNotification();
            String text = mainStageService.getMainStageController().currentPageNumber.getText();
            ApiConnection.getInstance().loadBaseData(text);
            StackPane mainPane = mainStageService.getMainPane();
            bin_selectAll.getScene().setRoot(mainPane);
            BorderPane loader = (BorderPane) Main.getScreen("loader");
            loader.setPrefHeight(mainPane.getHeight());
            loader.setPrefWidth(mainPane.getWidth());
            mainPane.getChildren().add(loader);
            bin_selectAll.setSelected(false);
            bin_selectAll.setVisible(false);
            recycleBinService.recoverControllButtons();
            recycleBinService.closeAllCheckBoxes();
            recycleBinService.clearSelectedImageCollection();
        }else {
            NoInternetModalServiceImpl.getInstance().openNoInternetModal();
        }


    }


    @FXML
    public void recover(MouseEvent mouseEvent) {
        System.out.println("recoverClick");
        if (!bin_selectAll.isVisible()) {
            bin_selectAll.setSelected(false);
            bin_selectAll.setVisible(true);
            bin_delete_batch.setDisable(true);
            bin_delete_batch.setStyle("-fx-background-radius: 5; -fx-background-color: #fff; -fx-cursor: none;");
            recycleBinService.showCheckBoxes();
        } else {
            if(Helper.getInstance().isInternetAvailable()){
                NoInternetModalServiceImpl.getInstance().closeOldOpenedNotification();
                recycleBinService.recoverSelectedImages();
            }else {
                NoInternetModalServiceImpl.getInstance().openNoInternetModal();
            }

        }


    }

    public void selectAllClick(MouseEvent mouseEvent) {
        recycleBinService.selecetAllClick();
    }

    public void deleteInBatchClick(MouseEvent mouseEvent) {
        if (!bin_selectAll.isVisible()) {
            bin_selectAll.setSelected(false);
            bin_selectAll.setVisible(true);
            recover.setDisable(true);
            recover.setStyle("-fx-background-radius: 5; -fx-background-color: #fff; -fx-cursor: none;");
            recycleBinService.showCheckBoxes();
        } else {
            if(Helper.getInstance().isInternetAvailable()){
                NoInternetModalServiceImpl.getInstance().closeOldOpenedNotification();
                recycleBinService.deleteSelectedImage();
            }else {
                NoInternetModalServiceImpl.getInstance().openNoInternetModal();
            }
        }
    }
}
