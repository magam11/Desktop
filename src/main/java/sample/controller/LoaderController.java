package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import sample.service.serviceImpl.LoaderServiceImpl;

public class LoaderController {
    @FXML
    public ProgressIndicator indicator;

    private LoaderServiceImpl loaderService = LoaderServiceImpl.getInstance();

    public void initialize(){
        loaderService.initializeLoaderController(this);
        indicator.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
    }
}
