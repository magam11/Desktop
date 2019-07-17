package sample.controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import sample.service.MainStageService;
import sample.service.SliderService;
import sample.service.serviceImpl.DeleteDialogServiceImpl;
import sample.service.serviceImpl.MainStageServiceImpl;
import sample.service.serviceImpl.SliderServiceImpl;


public class SlideController {

    @FXML
    public AnchorPane sliderContent;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public StackPane shownImage_container;
    @FXML
    public ImageView shownImage;
    @FXML
    public HBox menuBar;
    @FXML
    public AnchorPane leftContent;
    @FXML
    public AnchorPane rightContent;
    @FXML
    public AnchorPane header;
    @FXML
    public AnchorPane flutter;
//    @FXML
//    public Label nextLabel;

    @FXML
    public Label closeLabel;
    @FXML
    public Label fraction;
    @FXML
    public Label dateInfo;
    @FXML
    public ProgressBar sliderProgressBar;
    @FXML
    public Label sliderPercent;
    @FXML
    public Label shownImageName;
    @FXML
    public AnchorPane delete_btn;
    @FXML
    public AnchorPane download_btn;
    @FXML
    public ImageView downLoadImage;
    @FXML
    public Label downloadLabel;
    @FXML
    public ImageView deleteImage;
    @FXML
    public Label deleteLabel;
    @FXML
    public AnchorPane share_btn;
    @FXML
    public ImageView shareImage;
    @FXML
    public Label shareLabel;
    @FXML
    public Button previousButton;
    @FXML
    public Button nextButton;

    //services
    SliderService sliderService = SliderServiceImpl.getInstance();

    // local variables
    private MainStageService mainStageService = MainStageServiceImpl.getInstance();


    public void initialize() {
        downLoadImage.setImage(new Image(getClass().getResourceAsStream("/image/downloadBlack.png")));
        deleteImage.setImage(new Image(getClass().getResourceAsStream("/image/deleteBlack.png")));
        shareImage.setImage(new Image(getClass().getResourceAsStream("/image/share_white.png")));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sliderService.initializeSliderController(this);

    }

    @FXML
    public void closeSlide(MouseEvent mouseEvent) {
        mainStageService.getMainPane().getChildren().remove(1);
    }

    @FXML
    public void openNextImage(MouseEvent mouseEvent) {
        sliderService.openNextImage();
    }

    @FXML
    public void openPreviousImage(MouseEvent mouseEvent) {
        sliderService.openPreviousImage();
    }

    @FXML
    public void deleteImageFromSlidePage(MouseEvent mouseEvent) {
        DeleteDialogServiceImpl
                .getInstance()
                .openConfirmationDialog(shownImageName.getText(), "slide",
                        Integer.parseInt(fraction.getText().split("/")[0]) - (MainStageServiceImpl.getInstance().currentPageIndex.get()-1)*50);
    }
    @FXML
    public void downloadImageFromSlidePage(MouseEvent mouseEvent) {
        sliderService.downloadImage();
    }

    @FXML
    public void openShareDialog(MouseEvent mouseEvent) {
        MainStageServiceImpl.getInstance().shareImage(shownImageName.getText());
    }

    @FXML
    public void downLoadBtnMouseEntered(MouseEvent mouseEvent) {
        download_btn.setStyle("-fx-cursor: hand; -fx-background-color: #388e3c; -fx-background-radius: 5");
        downLoadImage.setImage(new Image(getClass().getResourceAsStream("/image/downloadWhite_WithoutContur.png")));
//        downLoadImage.setImage(new Image(getClass().getResourceAsStream("/image/downloadBlack.png")));
        downloadLabel.setTextFill(Paint.valueOf("#fff"));
    }

    @FXML
    public void downLoadBtnMouseExited(MouseEvent mouseEvent) {
        download_btn.setStyle("-fx-cursor: hand; -fx-background-color: #fafafa; -fx-background-radius: 5");
        downLoadImage.setImage(new Image(getClass().getResourceAsStream("/image/downloadBlack.png")));
        downloadLabel.setTextFill(Paint.valueOf("#000"));
    }

    @FXML
    public void deleteBtnMouseEntered(MouseEvent mouseEvent) {
        delete_btn.setStyle("-fx-cursor: hand; -fx-background-color: #388e3c; -fx-background-radius: 5");
        deleteImage.setImage(new Image(getClass().getResourceAsStream("/image/deleteWhite.png")));
        deleteLabel.setTextFill(Paint.valueOf("#fff"));
    }

    public void deleteBtnMouseExited(MouseEvent mouseEvent) {
        delete_btn.setStyle("-fx-cursor: hand; -fx-background-color: #Fafafa; -fx-background-radius: 5");
        deleteImage.setImage(new Image(getClass().getResourceAsStream("/image/deleteBlack.png")));
        deleteLabel.setTextFill(Paint.valueOf("#000"));
    }

    @FXML
    public void shareMouseEntered(MouseEvent mouseEvent) {
        share_btn.setStyle("-fx-cursor: hand; -fx-background-color: #388e3c; -fx-background-radius: 5");
        shareImage.setImage(new Image(getClass().getResourceAsStream("/image/share_white.png")));
        shareLabel.setTextFill(Paint.valueOf("#fff"));
    }

    @FXML
    public void shareMouseExited(MouseEvent mouseEvent) {
        share_btn.setStyle("-fx-cursor: hand; -fx-background-color: #Fafafa; -fx-background-radius: 5");
        shareImage.setImage(new Image(getClass().getResourceAsStream("/image/shareBlack.png")));
        shareLabel.setTextFill(Paint.valueOf("#000"));
    }
}
