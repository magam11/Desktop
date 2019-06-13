package sample.service;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.controller.MainStageController;

import java.io.IOException;

public class LoginService {
    private static LoginService instance = new LoginService();
    private Stage mainStage;

    public static LoginService getInstance() {
        return instance;
    }

    private LoginService() {
    }


    public Stage login(String phoneNumber, String password){
        Stage mainStage = new Stage();
        FXMLLoader fxmlLoader = null;
        Parent root = null;
        try {
            fxmlLoader = new  FXMLLoader(getClass().getResource("/view/main.fxml"));
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainStage.setTitle("Cloud Camera");
        mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/image/logo.png")));
        Scene scene = new Scene(root,906,427);
        mainStage.setScene(scene);
        mainStage.setMinHeight(427.0);
        mainStage.setMinWidth(906.0);
        mainStage.show();
        this.mainStage = mainStage;
        fxmlLoader.getController();
        FXMLLoader finalFxmlLoader = fxmlLoader;
        MainStageController controller = (MainStageController) finalFxmlLoader.getController();
        controller.mainStage = mainStage;
        mainStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            ((MainStageController) finalFxmlLoader.getController()).responsivWidth(newVal.doubleValue());
        });
        return mainStage;


    }


}
