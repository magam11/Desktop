package sample.service.serviceImpl;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Constant;
import sample.connection.ApiConnection;
import sample.controller.dialogController.DeleteDialogController;
import sample.dataTransferObject.request.ImageManagerRequest;
import sample.service.DeleteDialogService;

import java.io.IOException;

public class DeleteDialogServiceImpl implements DeleteDialogService {
    private static DeleteDialogServiceImpl ourInstance = new DeleteDialogServiceImpl();
    private DeleteDialogController deleteDialogController;
    public IntegerProperty indexOfImageFromCell;

    public static DeleteDialogServiceImpl getInstance() {
        return ourInstance;
    }

    private DeleteDialogServiceImpl() {
    }

    @Override
    public void openConfirmationDialog(String picName, String stageName, int indexOfImage) {
        indexOfImageFromCell = new SimpleIntegerProperty(indexOfImage);
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
        Scene scene = new Scene(root, 479.0, 148.0);
        mainStage.setScene(scene);
        mainStage.setMinHeight(148.0);
        mainStage.setMinWidth(479.0);
        mainStage.show();
        this.deleteDialogController = (DeleteDialogController) fxmlLoader.getController();
        deleteDialogController.stageName.setText(stageName);
        deleteDialogController.deletedImageName.setText(picName);
    }

    @Override
    public void updateImageStatus(String imageName, String stageName, boolean imageStatus) {
        if (!imageStatus) {// when image deleted
            ApiConnection.getInstance().updateImageStatus(Constant.IMAGE_URI, ImageManagerRequest.builder()
                    .actionType("delete")
                    .picName(imageName)
                    .build());
            if (stageName.equals("cell")) {
                MainStageServiceImpl.getInstance().removeImageFromCellByIndex(indexOfImageFromCell.get());
            } else { //stugel nkari hamary, ete verjinn e apa slidery pakel ete voch apa cuyc tal hajord nkary, jnjel nkari hamarov voroshvac contenty cellPag-um
                // ETE ET HAMAROV ARDEN ET NKARY HAVAQVEL E SELECTEDiMAGES LISTUM DRANIC EL JNJEL
//                CellServiceImpl.getInstance().removeImageFromCellByIndex(DeleteDialogServiceImpl.getInstance().indexOfImageFromCell.get());
                try {
                    MainStageServiceImpl.getInstance().removeImageFromCellByIndex(indexOfImageFromCell.get()-1);
                    // որ ցուցադրվող հաջորդ նկարի ժամանակ համարի մեկով պակաս ցույց տա,
                    SliderServiceImpl.getInstance().isIncrementFructionNumbering.set(true);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (!SliderServiceImpl.getInstance().isShowedLastImage()) { //when there is another picture to be shown
                    SliderServiceImpl.getInstance().openNextImage();
                } else { // when there is not a picture to show, close slider
                    SliderServiceImpl.getInstance().closeSlidePage();

                }

            }
        }
    }

    @Override
    public void closeDeleteDialog() {
        ((Stage) deleteDialogController.deletedImageName.getScene().getWindow()).close();
    }

    @Override
    public void openConfirmationDialogForBatch() {
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
        mainStage.initStyle(StageStyle.UNDECORATED);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root, 479.0, 148.0);
        mainStage.setScene(scene);
        mainStage.setMinHeight(148.0);
        mainStage.setMinWidth(479.0);
        mainStage.show();
        this.deleteDialogController = (DeleteDialogController) fxmlLoader.getController();
        deleteDialogController.stageName.setText("main_Batch");
    }


}
