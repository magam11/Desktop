package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import sample.service.SliderService;

public class SliderController {

    @FXML
    public BorderPane sliderContainer;
    SliderService sliderService = SliderService.getInstance();



}
