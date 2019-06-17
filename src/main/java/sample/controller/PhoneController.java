package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class PhoneController {
    @FXML
    public ScrollPane sclollpane;
    @FXML
    public BorderPane content;
    @FXML
    public AnchorPane image_content;
    @FXML
    public VBox benuBar;
    @FXML
    public ImageView shownImage;
    public StackPane shownImage_container;

    public void responsiveWidth(double stageWith) {
        shownImage_container.setPrefWidth(stageWith-250);
        shownImage.setFitWidth(stageWith-250);
        shownImage.setFitHeight(shownImage.getFitWidth()*(301.0/666.0));
        benuBar.setLayoutX(stageWith-223);
        image_content.setPrefWidth(stageWith-100);
        System.out.println(image_content.getPrefWidth());
        content.setPrefWidth(stageWith);
        sclollpane.setPrefWidth(stageWith);
    }

    public void responsiveHeight(double stageHeight) {
        sclollpane.setPrefHeight(stageHeight);
        content.setPrefHeight(stageHeight);
    }
}
