package sample.service.serviceImpl;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import sample.Constant;
import sample.connection.ApiConnection;
import sample.controller.RecycleBinController;
import sample.dataTransferObject.response.BaseUserData;
import sample.service.RecycleBinService;
import sample.storage.Storage;

public class RecycleBinServiceImpl implements RecycleBinService {
    private static RecycleBinService ourInstance = new RecycleBinServiceImpl();

    public static RecycleBinService getInstance() {
        return ourInstance;
    }

    private RecycleBinServiceImpl() {
    }
    private RecycleBinController recycleBinController;
    AnchorPane pageNumbersContainer = new AnchorPane();


    @Override
    public void initializeRecycleController(RecycleBinController recycleBinController){
        this.recycleBinController = recycleBinController;
    }


    @Override
    public void loadDataInRecycleBin(BaseUserData baseUserData, int page) {
        int count = baseUserData.getPicturesData()==null? 0:baseUserData.getPicturesData().size();

        recycleBinController.countData.setText("("+count+")");
        drawPagination(baseUserData,page);



    }

    @Override
    public void drawPagination(BaseUserData baseUserData, int page) {
        pageNumbersContainer.setStyle("-fx-alignment: center");
        Label label;

        ReadOnlyDoubleProperty widthPrp = recycleBinController.bin_pagination.widthProperty();
        if (pageNumbersContainer.getChildren() != null && pageNumbersContainer.getChildren().size() > 0) {
            pageNumbersContainer.getChildren().removeAll(pageNumbersContainer.getChildren());
            pageNumbersContainer = new AnchorPane();
        }
        for (int i = 0; i < baseUserData.getTotoalPageCount(); i++) {  //addes pages
            label = new Label(String.valueOf(i + 1));
            label.setLayoutX(widthPrp.getValue() / 2 - (baseUserData.getTotoalPageCount() - 1) * 20 + i * 20);
            label.setFont(Font.font(null, FontWeight.BOLD, 14));
            label.setId(String.valueOf("page_" + i + 1));
            if ((i + 1) == page) {
                label.setTextFill(Paint.valueOf("#388e3c"));
                label.setUnderline(true);
            }
            if ((i + 1) != page) {
                label.setStyle("-fx-cursor: hand");
            }
            int finalI = i;
            label.setOnMouseClicked(mouseEvent -> {
//                if (currentPageIndex.get() != (finalI + 1)) {
//                    ApiConnection.getInstance().getPage(Constant.BASE_DATA_URI, finalI + 1,
//                            Storage.getInstance().getCurrentToken());
//                }
            });

            pageNumbersContainer.getChildren().add(label);
            if(recycleBinController.bin_pagination.getChildren()!=null){
                recycleBinController.bin_pagination.getChildren().remove(0,recycleBinController.bin_pagination.getChildren().size());
            }
            recycleBinController.bin_pagination.getChildren().add(pageNumbersContainer);

        }

    }
}
