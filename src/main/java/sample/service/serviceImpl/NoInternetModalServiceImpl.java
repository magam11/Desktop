package sample.service.serviceImpl;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import sample.controller.modalController.NoInternetModalController;
import sample.service.NoInternetModalService;

public class NoInternetModalServiceImpl implements NoInternetModalService {

    private final static NoInternetModalService instance = new NoInternetModalServiceImpl();

    private NoInternetModalController noInternetModalController;
    Stage noInternetStage;
    FXMLLoader modalLoader;
    Parent noInternetModal;
    Notifications notification;


    private NoInternetModalServiceImpl() {
    }

    public static NoInternetModalService getInstance() {
        return instance;
    }


    @Override
    public void initilizeNoInternetModalController(NoInternetModalController noInternetModalController) {
        this.noInternetModalController = noInternetModalController;
    }

    @Override
    public void openNoInternetModal() {

        if (modalLoader == null) {
            try {
                noInternetStage = new Stage();
                modalLoader = new FXMLLoader(getClass().getResource("/view/noInternet.fxml"));
                noInternetModal = (Parent) modalLoader.load();
                Scene scene = new Scene(noInternetModal);
                noInternetStage.setScene(scene);
                noInternetStage.setResizable(false);
                noInternetStage.initModality(Modality.APPLICATION_MODAL);
                noInternetStage.setTitle("No Internet");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());

            }
        }
        if (notification != null) {
            noInternetModalController.cloudImage.getScene().getWindow().hide();
        }
        notification = Notifications.create();
        notification
                .hideCloseButton()
                .graphic(noInternetModal)
                .hideAfter(Duration.INDEFINITE)
                .position(Pos.CENTER)
                .show();
    }

    @Override
    public void closeOldOpenedNotification() {
        if (notification != null) {
            noInternetModalController.cloudImage.getScene().getWindow().hide();
        }
    }


}
