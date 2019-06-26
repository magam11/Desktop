package sample.service.serviceImpl;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.Size;
import sample.controller.MainStageController;
import sample.dataTransferObject.response.BaseUserData;
import sample.service.MainStageService;
import sample.storage.Storage;

import java.io.IOException;

public class MainStageServiceImpl implements MainStageService {
    private static MainStageService mainStageService = new MainStageServiceImpl();
    public static IntegerProperty myImageCount;

    private DoubleProperty phoneNumberLenth;

    private MainStageServiceImpl() {
    }

    public static MainStageService getInstance() {
        return mainStageService;
    }

    //local variables
    private MainStageController mainStageController;


    @Override
    public void responsivWidth(double stageWith) {
        mainStageController.cellController.responsiveWidth(stageWith);
        mainStageController.slideController.responsiveWidth(stageWith);
        if (stageWith <= 1000.0) {
            mainStageController.memoryProgressBar.setPrefWidth(Size.WIDTH_COEFFICENT_FOR_PROGRESS_BAR_WIDTH * stageWith);
            mainStageController.fraction.setLayoutX(Size.WIDTH_COEFFICENT_FOR_PROGRESS_BAR_WIDTH * stageWith + mainStageController.memoryProgressBar.getLayoutX() + 7);
        }
        mainStageController.logoCamerImageVeiw.setLayoutX(Size.WIDTH_COEFFICENT_FOR_CAMERA_LOGO_CAMERA * stageWith);
        mainStageController.share.setLayoutX(stageWith - 299);
        mainStageController.delete.setLayoutX(stageWith - 202);
        mainStageController.download.setLayoutX(stageWith - 105);
        mainStageController.logOut_btn.setLayoutX(stageWith-130);
        mainStageController.recycle_btn.setLayoutX(stageWith-227);
        mainStageController.cellController.scrollPane.setPrefWidth(stageWith-40);
        mainStageController.cellController.cellContent.setPrefWidth(stageWith);
        mainStageController.cellController.cellContent.setLayoutX(20);
        mainStageController.cellController.filterPane.setPrefWidth(stageWith);
        mainStageController.cellController.pageNumbersPane.setPrefWidth(stageWith);
        mainStageController.headerRow1.setPrefWidth(stageWith);
        mainStageController.phoneNumber.setLayoutX(mainStageController.recycle_btn.getLayoutX()-phoneNumberLenth.getValue()-8);
    }

    @Override
    public void initializeMainStageController(MainStageController mainStageController) {
        this.mainStageController = mainStageController;
    }


    @Override
    public void loadMainStageData(BaseUserData baseUserData) {
        mainStageController.fraction.setText(baseUserData.getFruction());
        myImageCount = new SimpleIntegerProperty(Integer.parseInt(baseUserData.getFruction().split("/")[0]));
        int imagesConunt = baseUserData.getPicturesData() == null ? 0 : baseUserData.getPicturesData().size();
        mainStageController.imageCountInto.setText("Storage (" + imagesConunt + ")");
        mainStageController.phoneNumber.setText(baseUserData.getPhoneNumber());
        phoneNumberLenth = new SimpleDoubleProperty(mainStageController.phoneNumber.getLayoutBounds().getWidth());
        mainStageController.phoneNumber.setLayoutX(mainStageController.recycle_btn.getLayoutX()-phoneNumberLenth.getValue()-8);
        mainStageController.memoryProgressBar.setProgress(Double.valueOf(imagesConunt) / 400.0);
        if (imagesConunt != 0) {
            CellServiceImpl.getInstance().drawImagesInMainStage(baseUserData.getPicturesData(), Storage.getInstance().getCurrentToken());
        } else {
            // TODO something when user does not have pictures ...
        }
    }

    @Override
    public void logOut() throws IOException {
        Storage storage = Storage.getInstance();
        storage.setCurrentToken("");
        storage.setToken("");
        ((Stage)mainStageController.headerRow1.getScene().getWindow()).close();
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Log in");
        javafx.scene.image.Image logo = new Image(this.getClass().getResourceAsStream("/image/logo.png"));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(logo);


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(we->{
            System.exit(0);
        });
        primaryStage.show();
    }


}
