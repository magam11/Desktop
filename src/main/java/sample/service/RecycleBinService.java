package sample.service;

import sample.controller.MainStageController;
import sample.controller.RecycleBinController;
import sample.dataTransferObject.response.BaseUserData;

public interface RecycleBinService {

    void initializeRecycleController(RecycleBinController recycleBinController);

    void loadDataInRecycleBin(BaseUserData totoalPageCount, int page);

    void drawPagination(BaseUserData baseUserData, int page);


}
