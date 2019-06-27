package sample.service.serviceImpl;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.Constant;
import sample.MessageNotifications;
import sample.connection.ApiConnection;
import sample.controller.LoginController;
import sample.controller.MainStageController;
import sample.dataTransferObject.request.AuthenticationRequest;
import sample.dataTransferObject.response.AuthenticationResponse;
import sample.dataTransferObject.response.BaseUserData;
import sample.dataTransferObject.response.ImageData;
import sample.service.LoginService;
import sample.service.MainStageService;
import sample.storage.Storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginServiceImpl implements LoginService {
    private static LoginServiceImpl instance = new LoginServiceImpl();


    public static LoginServiceImpl getInstance() {
        return instance;
    }

    private LoginServiceImpl() {
    }

    //controllers
    private LoginController loginController;

    @Override
    public void initializeLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    @Override
    public void authenticationSuccessful(AuthenticationResponse auth) {
        ApiConnection.getInstance().baseData(Constant.BASE_DATA_URI, 1, auth.getToken());


    }


    @Override
    public void login(String phoneNumber, String password, boolean remember) {
        ApiConnection.getInstance().loginConnection(Constant.LOGIN_URI, AuthenticationRequest.builder()
                .phoneNumber(phoneNumber)
                .password(password)
                .build(),remember);

    }

    @Override
    public void onRespoinseOfDataApiAnalysis(Response response) {
        if (response.isSuccessful()) { //HttpStatusCode = 200
            JSONObject responseJson = null;
            try {
                responseJson = new JSONObject(new String(response.body().bytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<ImageData> imageData = new ArrayList<>();
            JSONArray picturesData = responseJson.getJSONArray("picturesData");
            JSONObject imageDataJson = null;
            if (picturesData != null) {
                for (int i = 0; i < picturesData.length(); i++) {
                    imageDataJson = picturesData.getJSONObject(i);
                    imageData.add(ImageData.builder()
                            .createdAt(imageDataJson.getString("createdAt"))
                            .picName(imageDataJson.getString("picName"))
                            .picSize(imageDataJson.getDouble("picSize"))
                            .build());
                }
            }
            openMainStage(BaseUserData.builder()
                    .fruction(responseJson.getString("fruction"))
                    .totoalPageCount(responseJson.getInt("totoalPageCount"))
                    .picturesData(imageData)
                    .phoneNumber(responseJson.getString("phoneNumber"))
                    .build(),1);
        } else if (response.code() == 401) {
            authenticationFailure(MessageNotifications.status401_text);
        } else if (response.code() == 403) {
            authenticationFailure(MessageNotifications.status403_text);
        } else if (response.code() == 410) {
            authenticationFailure(MessageNotifications.status410_text);
        } else {
            authenticationFailure("Try again");
        }
    }

    @Override
    public void openMainStage(BaseUserData baseUserData,int loadedPageNumber) {
        Platform.runLater(() -> {

            ((Stage) loginController.passwordTextField.getScene().getWindow()).close();

            Stage mainStage = new Stage();
            FXMLLoader fxmlLoader = null;
            Parent root = null;
            try {
                fxmlLoader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mainStage.setTitle("Cloud Camera");
            mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/image/logo.png")));
            Scene scene = new Scene(root, 906, 840);
            mainStage.setScene(scene);
            mainStage.setMinHeight(427.0);
            mainStage.setMinWidth(906.0);
            mainStage.show();
            fxmlLoader.getController();
            FXMLLoader finalFxmlLoader = fxmlLoader;
            MainStageController controller = (MainStageController) finalFxmlLoader.getController();
            MainStageServiceImpl.getInstance().loadMainStageData(baseUserData,loadedPageNumber);
            controller.mainStage = mainStage;
            mainStage.widthProperty().addListener((obs, oldVal, newVal) -> {
                ((MainStageController) finalFxmlLoader.getController()).responsivWidth(newVal.doubleValue());
            });
            mainStage.heightProperty().addListener((obs, oldVal, newVal) -> {
                ((MainStageController) finalFxmlLoader.getController()).responsivHeight(newVal.doubleValue());
            });
            mainStage.setOnCloseRequest(we -> {
                System.exit(0);
            });

        });


    }


    @Override
    public void authenticationFailure(String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                loginController.failureMessage.setText("* " + message);
                loginController.failureMessage.setVisible(true);
            }
        });


    }


}
