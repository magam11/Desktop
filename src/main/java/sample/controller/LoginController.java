package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import sample.Constant;

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

    public void initialize(){
        javafx.scene.image.Image phoneNumberIco = new Image(this.getClass().getResourceAsStream("/image/phone_icon.png"));
        javafx.scene.image.Image loginImage = new Image(this.getClass().getResourceAsStream("/image/loginImage.png"));
        javafx.scene.image.Image passwordImage = new Image(this.getClass().getResourceAsStream("/image/password_icon.png"));
        phoneNumberImageView.setImage(phoneNumberIco);
        loginImageImageView.setImage(loginImage);
//        login_button.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.8)));
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
}
