package sample.service.serviceImpl;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.controller.MainStageController;
import sample.controller.dialogController.DeleteDialogController;
import sample.service.DeleteDialogService;

import java.io.IOException;

public class DeleteDialogServiceImpl  implements DeleteDialogService {
    private static DeleteDialogServiceImpl ourInstance = new DeleteDialogServiceImpl();

    public static DeleteDialogServiceImpl getInstance() {
        return ourInstance;
    }

    private DeleteDialogServiceImpl() {
    }

    public void openConfirpationDialog(String picName, String stageName) {
        Stage mainStage = new Stage();
        FXMLLoader fxmlLoader = null;
        Parent root = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("/view/dialog/deleteConfirmationDialog.fxml"));
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainStage.setTitle("Confirmation dialog");
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.initStyle(StageStyle.UNDECORATED);
//        mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/image/logo.png")));
        Scene scene = new Scene(root,479.0,148.0);
        mainStage.setScene(scene);
        mainStage.setMinHeight(427.0);
        mainStage.setMinWidth(906.0);
        mainStage.show();
        DeleteDialogController deleteDialogController = (DeleteDialogController) fxmlLoader.getController();
        deleteDialogController.stageName.setText(stageName);
        deleteDialogController.deletedImageName.setText(picName);
    }

    @Override
    public void updateImageStatus(String imageName,String stageName, boolean imageStatus) {



    }
}
