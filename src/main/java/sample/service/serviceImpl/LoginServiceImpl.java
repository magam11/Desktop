package sample.service.serviceImpl;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.controller.MainStageController;
import sample.service.LoginService;

import java.io.IOException;

public class LoginServiceImpl implements LoginService {
    private static LoginServiceImpl instance = new LoginServiceImpl();


    public static LoginServiceImpl getInstance() {
        return instance;
    }

    private LoginServiceImpl() {
    }

    @Override
    public void login(String phoneNumber, String password){
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
        fxmlLoader.getController();
        FXMLLoader finalFxmlLoader = fxmlLoader;
        MainStageController controller = (MainStageController) finalFxmlLoader.getController();
        controller.mainStage = mainStage;
        mainStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            ((MainStageController) finalFxmlLoader.getController()).responsivWidth(newVal.doubleValue());
        });
        mainStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            ((MainStageController) finalFxmlLoader.getController()).responsivHeight(newVal.doubleValue());
        });


    }


}
