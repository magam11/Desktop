package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import sample.Size;
import sample.service.SliderService;

public class SliderController {

    @FXML
    public BorderPane sliderContainer;
    @FXML
    public Label closeSliderLabel;
    @FXML
    public ProgressBar progressBar;
    @FXML
    public ImageView shownImage;
    @FXML
    public Label nextLabel;
    @FXML
    public Label previousLabel;

    SliderService sliderService = SliderService.getInstance();


    public void responsiveWidth(double stageWith) {
        sliderContainer.setPrefWidth(stageWith);
        closeSliderLabel.setLayoutX(stageWith-37);
//        closeSliderLabel.setLayoutY(sliderContainer-);
        shownImage.setFitWidth(Size.WIDTH_COEFFICENT_FOR_SHOWN_IMAGE*stageWith);
    }

    @FXML
    public void closeSlider(MouseEvent mouseEvent) {
        sliderContainer.setVisible(false);

    }

    public void responsiveHeight(double stageHeight) {
        sliderContainer.setPrefHeight(stageHeight);
    }
}
