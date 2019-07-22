package sample.controller.modalController;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import sample.service.NoInternetModalService;
import sample.service.serviceImpl.NoInternetModalServiceImpl;

public class NoInternetModalController {


    public ImageView cloudImage;

    private NoInternetModalService noInternetModalService = NoInternetModalServiceImpl.getInstance();

    public void initialize(){
        noInternetModalService.initilizeNoInternetModalController(this);
        cloudImage.setImage(new Image(getClass().getResourceAsStream("/image/noInternet/no_internet.png")));
    }

    public void closeNoInternetModal(MouseEvent mouseEvent) {
        ((Button)mouseEvent.getSource()).getScene().getWindow().hide();
    }
}
