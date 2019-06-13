package sample.controller;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.Constant;
import sample.service.LoginService;

import javax.xml.soap.Text;
import java.awt.*;
import java.net.URI;

public class LoginController {

    @FXML
    public TextField passwordTextField;
    @FXML
    public TextField phoneNumberTextField;
    @FXML
    public ImageView phoneNumberImageView;
    @FXML
    public ImageView loginImageImageView;
    @FXML
    public ImageView passwordImageView;
    @FXML
    public Button login_button;
    @FXML
    public AnchorPane loginForm;
    @FXML
    public TextField passwordTF;
    @FXML
    public ImageView showPassword;

    private LoginService loginService =  LoginService.getInstance();

    public void initialize() {
        javafx.scene.image.Image phoneNumberIco = new Image(this.getClass().getResourceAsStream("/image/phone_icon.png"));
        javafx.scene.image.Image loginImage = new Image(this.getClass().getResourceAsStream("/image/loginImage.png"));
        javafx.scene.image.Image passwordImage = new Image(this.getClass().getResourceAsStream("/image/password_icon.png"));
        phoneNumberImageView.setImage(phoneNumberIco);
        loginImageImageView.setImage(loginImage);
        loginForm.setEffect(new DropShadow(19, Color.rgb(0, 0, 0, 0.1)));
        passwordImageView.setImage(passwordImage);
    }

    @FXML
    public void openWebForgotPasswordPage(MouseEvent mouseEvent) {
        try {
            Desktop.getDesktop().browse(new URI(Constant.FORGOT_PASSWORD_WEB_LINK));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showPassword(MouseEvent mouseEvent) {
        passwordTF.setText(passwordTextField.getText());
        passwordTextField.setVisible(false);
        passwordTF.setVisible(true);

    }

    @FXML
    public void hidePassword(MouseEvent mouseDragEvent) {
        passwordTextField.setVisible(true);
        passwordTF.setVisible(false);
    }

    public void login(MouseEvent mouseEvent) {
        Stage loginStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        loginStage.close();
        String phoneNumber = phoneNumberTextField.getText();
        String password = passwordTextField.getText();
        loginService.login(phoneNumber, password);

    }


}
