package sample.controller.dialogController;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.service.DeleteDialogService;
import sample.service.serviceImpl.DeleteDialogServiceImpl;

public class DeleteDialogController {
    @FXML
    public Button no_button;
    @FXML
    public Button yes_button;
    @FXML
    public ImageView recicleBin_imageView;
    @FXML
    public Label deletedImageName;
    @FXML
    public Label stageName;


    // services
    private DeleteDialogService deleteDialogService = DeleteDialogServiceImpl.getInstance();

    public  void initialize(){
        Image recicleBinImage = new Image(this.getClass().getResourceAsStream("/image/recicleBin.png"));
        recicleBin_imageView.setImage(recicleBinImage);
    }

    @FXML
    public void closeDialogStage(MouseEvent mouseEvent) {
        ((Stage) no_button.getScene().getWindow()).close();

    }
    @FXML
    public void updateImageStatus(MouseEvent mouseEvent) {
        deleteDialogService.updateImageStatus(deletedImageName.getText(),stageName.getText(),false);
    }
}
