package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;

public class LoaderController {
    @FXML
    public ProgressIndicator indicator;

    public void initialize(){
        indicator.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
    }
}
