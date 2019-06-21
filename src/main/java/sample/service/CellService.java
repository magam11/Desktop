package sample.service;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import sample.controller.CellController;
import sample.dataTransferObject.response.ImageData;

import java.util.List;

public interface CellService {

    void initilizeCellController(CellController cellController);

    void drawImagesInMainStage(List<ImageData> picturesData, String currentToken);

    void downloadImage(String picName, ProgressBar progressBar);

}
