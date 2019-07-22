package sample.service.serviceImpl;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.Constant;
import sample.Main;
import sample.MessageNotifications;
import sample.connection.ApiConnection;
import sample.controller.LoginController;
import sample.controller.MainStageController;
import sample.dataTransferObject.request.AuthenticationRequest;
import sample.dataTransferObject.response.AuthenticationResponse;
import sample.dataTransferObject.response.BaseUserData;
import sample.dataTransferObject.response.ImageData;
import sample.service.LoginService;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoginServiceImpl implements LoginService {
    private static LoginServiceImpl instance = new LoginServiceImpl();

    BooleanProperty showLoader = new SimpleBooleanProperty(true);
    Timer timer;
    ProgressIndicator pb = new ProgressIndicator();
    MainStageController mainStageController;


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
                .build(), remember);

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
                    .build(), 1);
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

//    @Override
//    public void openMainStage(BaseUserData baseUserData, int loadedPageNumber) {
//        Platform.runLater(() -> {
//
//            ((Stage) loginController.passwordTextField.getScene().getWindow()).close();
//
//            Stage mainStage = new Stage();
//            FXMLLoader fxmlLoader = null;
//            Parent root = null;
//            try {
//                fxmlLoader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
//                root = fxmlLoader.load();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            mainStage.setTitle("Cloud Camera");
//            mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/image/logo.png")));
//            Scene scene = new Scene(root, 906, 840);
//            mainStage.setScene(scene);
//            mainStage.setMinHeight(500);
//            mainStage.setMinWidth(906.0);
//
//            mainStageController = (MainStageController) fxmlLoader.getController();
////----------------------------------------------------------------------------------------------------------------------------------------
//            LoaderTasl loaderTask = new LoaderTasl();
//            Timer timer = new Timer("Loader");
//            timer.schedule(loaderTask, 0, 999999999);
//
//
////            ExecutorService exService = Executors.newSingleThreadExecutor();
////            exService.submit(new CopyFileTask());
////----------------------------------------------------------------------------------------------------------------------------------------------
//
//            mainStage.show();
////            timer.cancel();
//            FXMLLoader finalFxmlLoader1 = fxmlLoader;
//
//            Platform.runLater(() -> {
//
//
//                FXMLLoader finalFxmlLoader = finalFxmlLoader1;
//                MainStageController controller = (MainStageController) finalFxmlLoader.getController();
//                Thread  thread = new Thread(() -> {
////                    Platform.runLater(() -> {
//                        MainStageServiceImpl.getInstance().loadMainStageData(baseUserData, loadedPageNumber, "general");
//                        controller.mainStage = mainStage;
////                    });
//                });
//                thread.start();
//
//
//                mainStage.setOnCloseRequest(we -> {
//                    System.exit(0);
//                });
//            });
//        });
//
//    }


    @Override
    public void openMainStage(BaseUserData baseUserData, int loadedPageNumber) {


        Platform.runLater(() -> {
            if (loginController != null) {
                System.out.println("null-i ifi mej");
                ((Stage) loginController.passwordTextField.getScene().getWindow()).close();
            }

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
            Scene scene = new Scene(root, 910, 840);
            mainStage.setScene(scene);
            mainStage.setMinHeight(500);
            mainStage.setMinWidth(920);
            mainStageController = (MainStageController) fxmlLoader.getController();
//            mainStage.iconifiedProperty().addListener((ov, t, t1) -> System.out.println("minimized:" + t1.booleanValue()));

            mainStage.maximizedProperty().addListener((ov, t, t1) -> {
//                mainStageController.header.setPrefWidth(mainStage.widthProperty().getValue());
                System.out.println("maximized:" + t1.booleanValue());
            });
            mainStage.show();
            BorderPane loader = (BorderPane) Main.getScreen("loader");
            loader.setPrefWidth(mainStageController.mainPane.getWidth());
            loader.setPrefHeight(mainStageController.mainPane.getHeight());
            mainStageController.mainPane.getChildren().add(loader);
//-----------------------------------------------------------------------------------------------------------------------------------------------------

//-----------------------------------------------------------------------------------------------------------------------------------------------------
            MainStageController controller = (MainStageController) fxmlLoader.getController();
            MainStageServiceImpl.getInstance().loadMainStageData(baseUserData, loadedPageNumber, "general");
            controller.mainStage = mainStage;


        });
    }

    public class LoaderTasl extends TimerTask {


        @Override
        public void run() {
            mainStageController.mainPane.getChildren().add(Main.getScreen("loader"));

        }
    }


    @Override
    public void authenticationFailure(String message) {
        Platform.runLater(() -> {
            loginController.failureMessage.setText("* " + message);
            loginController.failureMessage.setVisible(true);
        });


    }


    class LoaderTask extends TimerTask {
        @Override
        public void run() {
            final int[] progress = {0};
            Platform.runLater(() -> {
                showLoader.setValue(true);
                StackPane loader = new StackPane();
                loader.setId("loader");
                BorderPane anchorPane = new BorderPane();
                pb.setStyle("-fx-progress-color: #388e3c;");
                pb.setMaxHeight(300);
                pb.setMaxWidth(300);
                anchorPane.setCenter(pb);
                loader.getChildren().add(anchorPane);
                mainStageController.mainPane.getChildren().add(3, loader);
                System.out.println("ashxatec");
            });

        }
    }
}
