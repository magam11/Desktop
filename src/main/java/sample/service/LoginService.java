package sample.service;

import javafx.stage.Stage;
import okhttp3.Response;
import sample.controller.LoginController;
import sample.controller.MainStageController;
import sample.dataTransferObject.response.AuthenticationResponse;
import sample.dataTransferObject.response.BaseUserData;

public interface LoginService {

     void login(String phoneNumber, String password, boolean remember);

     void authenticationFailure(String message);

     void initializeLoginController(LoginController loginController);

     void authenticationSuccessful(AuthenticationResponse build);

     void onRespoinseOfDataApiAnalysis(Response response);

     void openMainStage(BaseUserData baseUserData,int loadedPageNumber);

     void showLoader(MainStageController mainStageController, Stage stage);

     void closeLoader(MainStageController mainStageController);



}
