package sample.service;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import okhttp3.Response;
import sample.controller.MainStageController;
import sample.dataTransferObject.response.BaseUserData;
import sample.dataTransferObject.response.ImageData;

import java.io.IOException;
import java.util.List;

public interface MainStageService {

    void initializeMainStageController(MainStageController mainStageController);

    void loadMainStageData(BaseUserData baseUserData,int loadedPageNumber,String filter_search);

    void logOut() throws IOException;

    void drawImagesInMainStage(List<ImageData> picturesData, String currentToken);

    void downloadImage(String picName, ProgressBar progressBar);

    void removeImageFromCellByIndex(int indexOfImageFromCell);

    void loadPage(Response response, int pageNumber);

    void showCheckBoxes();

    void selectOrCancelItems();

    void singleSelectOrCancelItem(CheckBox checkBox,int index,String imageName);

    void deleteSelectedImages();

    void cancelSelect();

    void removeAllCheckobox();

    void changeUserStatusAndUpdatePage();

    void downloadSelectedImages();

    void shareImage(String picName);

    StackPane getMainPane();

    MainStageController getMainStageController();

    void filterClick();

    void loadStageDataFilterTime(BaseUserData totoalPageCount, int page);

    void setFilterAction(String actionType);
}
