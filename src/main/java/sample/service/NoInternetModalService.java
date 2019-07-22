package sample.service;

import sample.controller.modalController.NoInternetModalController;

public interface NoInternetModalService {

    void initilizeNoInternetModalController(NoInternetModalController noInternetModalController);

    void openNoInternetModal();

    void closeOldOpenedNotification();
}
