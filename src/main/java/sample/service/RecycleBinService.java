package sample.service;

import javafx.scene.control.CheckBox;
import sample.controller.MainStageController;
import sample.controller.RecycleBinController;
import sample.dataTransferObject.response.BaseUserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

    HashMap<CheckBox, Integer> getCheckBoxes();

    Map<Integer, String> getSelcetedImages();

    int getCurrentPage();
}
