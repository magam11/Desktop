package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import sample.Size;
import sample.service.SliderService;

public class SliderController {

    @FXML
    public ScrollPane sliderContainer;
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
//    @FXML
//    public AnchorPane center_content;
    @FXML
    public VBox menuBar;
    @FXML
    public BorderPane content;
    @FXML
    public Label fraction;
    private SliderService sliderService = SliderService.getInstance();
    private MainStageController mainStageController;

    public void initializeMainStageController(MainStageController mainStageController){
        this.mainStageController = mainStageController;
    }


    public void initialize() {
        sliderContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sliderContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        menuBar.setLayoutX(shownImage.getLayoutX()+shownImage.getFitWidth());

    }


    void responsiveWidth(double stageWith) {
        sliderContainer.setLayoutY(mainStageController.headerRow1.getLayoutY());
        sliderContainer.setPrefWidth(stageWith);
//        sliderContainer.setPrefHeight(stageWith);
        sliderContainer.setPrefHeight((427.0/906.0)*stageWith);
        content.setPrefWidth(stageWith);
        content.setPrefHeight((427.0/906.0)*stageWith);
        closeSliderLabel.setLayoutX(content.getPrefWidth()-53);
//        center_content.setPrefWidth(stageWith-90);
//        center_content.setPrefHeight((center_content.getScene().getWindow()).getHeight());
        shownImage.setFitWidth(stageWith-200);
        shownImage.setFitHeight(sliderContainer.getPrefWidth());
//        shownImage.setFitHeight((center_content.getScene().getWindow()).getHeight());



//        center_content.setPrefWidth((820.0/906.0)*stageWith);
//        center_content.setPrefHeight((center_content.getPrefWidth()*363)/427)
//        shownImage.setFitWidth((676.0/906.0)*stageWith);
//        shownImage.setFitHeight((279.0/906.0)*stageWith);
//        center_content.setPrefHeight(stageWith*3);

        menuBar.setLayoutX(shownImage.getLayoutX()+shownImage.getFitWidth());
        fraction.setLayoutX(shownImage.getLayoutX()+shownImage.getFitWidth()-210);
        progressBar.setLayoutY(shownImage.getLayoutY()+shownImage.getFitHeight()+20);
    }


    void responsiveHeight(double stageHeight) {
        sliderContainer.setPrefHeight(stageHeight);
//        center_content.setPrefHeight((center_content.getScene().getWindow()).getHeight());

//        content.setPrefHeight(stageHeight);
//        center_content.setPrefWidth((200.0/427.0)*stageHeight);
//        sliderContainer.setPrefHeight(stageHeight);
//        shownImage.setFitWidth((523.0/906.00)*stageHeight);
//        shownImage.setFitHeight((284.0/906.00)*stageHeight);
//        menuBar.setLayoutX(shownImage.getLayoutX()+shownImage.getFitWidth()+20);
////        closeSliderLabel.setLayoutY(22);
    }

    @FXML
    public void closeSlider(MouseEvent mouseEvent) {
        sliderContainer.setVisible(false);

    }


}
