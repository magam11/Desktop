package sample.service;

import sample.controller.MainStageController;
import sample.dataTransferObject.response.BaseUserData;
import sample.dataTransferObject.response.ImageData;

import java.io.IOException;
import java.util.List;

public interface MainStageService {

    void responsivWidth(double stageWith);

    void initializeMainStageController(MainStageController mainStageController);

    void loadMainStageData(BaseUserData baseUserData);


    void logOut() throws IOException;
}
