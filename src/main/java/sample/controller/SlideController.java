package sample.controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import sample.service.SliderService;
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
    public VBox menuBar;
    @FXML
    public AnchorPane leftContent;
    @FXML
    public AnchorPane rightContent;
    @FXML
    public AnchorPane header;
    @FXML
    public AnchorPane flutter;
    @FXML
    public Label nextLabel;
    @FXML
    public Label previousLabel;
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

    //services
    SliderService sliderService = SliderServiceImpl.getInstance();

    // local variables
    private DoubleProperty halfHeghtOfNextLabel;
    private DoubleProperty halfHeghtOfPreviousLabel;
    private DoubleProperty fractionWidth;
    private DoubleProperty sliderPercentWidth;


    public void initialize(){
        halfHeghtOfNextLabel =new SimpleDoubleProperty(nextLabel.getPrefHeight()/2);
        halfHeghtOfPreviousLabel = new SimpleDoubleProperty(previousLabel.getPrefHeight()/2);
        Text fractionText = new Text("1/127");
        Text sliderPercentText = new Text("1/127");
        sliderPercentWidth = new SimpleDoubleProperty(sliderPercentText.getLayoutBounds().getWidth());
        fractionWidth = new SimpleDoubleProperty(fractionText.getLayoutBounds().getWidth());
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }


    public void responsiveWidth(double stageWidth){
        shownImage.setFitWidth(stageWidth-230);
        shownImage.setFitHeight(shownImage.getFitWidth()*(439.0/596.0));
        shownImage_container.setPrefWidth(stageWidth-230);
        shownImage_container.setPrefHeight(shownImage.getFitHeight());
        header.setPrefWidth(stageWidth-120);
        scrollPane.setPrefWidth(stageWidth-225);
        sliderContent.setPrefWidth(stageWidth);
        rightContent.setLayoutX(stageWidth-60);
        menuBar.setLayoutX(shownImage_container.getLayoutX()+shownImage_container.getPrefWidth()+5);
        flutter.setPrefWidth(stageWidth-120);
        fraction.setLayoutX(scrollPane.getPrefWidth()+scrollPane.getLayoutX()-fractionWidth.getValue());
        sliderProgressBar.setLayoutX((stageWidth-sliderProgressBar.getPrefWidth())/2);
        sliderPercent.setLayoutX((stageWidth-sliderPercentWidth.getValue())/2);

    }

    public void responsiveHeght(double stageHeight){
        scrollPane.setPrefHeight(stageHeight-100);
        rightContent.setLayoutY(0);
        flutter.setLayoutY(stageHeight-60);
        nextLabel.setLayoutY(stageHeight/2-halfHeghtOfNextLabel.getValue());
        previousLabel.setLayoutY(stageHeight/2-halfHeghtOfPreviousLabel.getValue());
        sliderContent.setPrefHeight(stageHeight);
        rightContent.setPrefHeight(stageHeight);
        leftContent.setPrefHeight(stageHeight);

    }

    @FXML
    public void closeSlide(MouseEvent mouseEvent) {
        sliderContent.setVisible(false);

    }
}
