package sample.controller.dialogController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.connection.ApiConnection;
import sample.service.RecycleBinService;
import sample.service.serviceImpl.RecycleBinServiceImpl;

import java.util.Collection;

public class DeleteDialogControllerForRecycle {
    @FXML
    public Button no_button;
    @FXML
    public Button yes_button;
    private RecycleBinService recycleBinService= RecycleBinServiceImpl.getInstance();

    @FXML
    public void closeDialogStage(MouseEvent mouseEvent) {
        ((Stage) no_button.getScene().getWindow()).close();
    }

    public void updateImageStatus(MouseEvent mouseEvent) {
        Collection<String> values = recycleBinService.getSelcetedImages().values();

        ApiConnection.getInstance().deleteSelectedImageFromRecycleBin(sample.dataTransferObject.request.ImageData.builder()
                .picNames(values)
                .page(recycleBinService.getCurrentPage())
                .build());
        recycleBinService.closeAllCheckBoxes();
        recycleBinService.clearSelectedImageCollection();
        recycleBinService.recoverControllButtons();
        recycleBinService.getCheckBoxes().clear();
        ((Stage) no_button.getScene().getWindow()).close();
    }
}
