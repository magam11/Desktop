package sample.service;

import sample.controller.MainStageController;
import sample.controller.RecycleBinController;
import sample.dataTransferObject.response.BaseUserData;

public interface RecycleBinService {

    void initializeRecycleController(RecycleBinController recycleBinController);

    void loadDataInRecycleBin(BaseUserData totoalPageCount, int page);

    void drawPagination(BaseUserData baseUserData, int page);

    void animationForPagination();

    void stopAnimationForPagination();


    void showCheckBoxes();

    void selecetAllClick();

    void recoverControllButtons();

    void closeAllCheckBoxes();

    void clearSelectedImageCollection();

    void deleteSelectedImage();

    void recoverSelectedImages();
}
