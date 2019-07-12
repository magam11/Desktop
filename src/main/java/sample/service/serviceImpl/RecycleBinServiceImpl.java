package sample.service.serviceImpl;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Constant;
import sample.connection.ApiConnection;
import sample.controller.RecycleBinController;
import sample.dataTransferObject.request.ImageManagerRequest;
import sample.dataTransferObject.response.BaseUserData;
import sample.dataTransferObject.response.ImageData;
import sample.service.MainStageService;
import sample.service.RecycleBinService;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecycleBinServiceImpl implements RecycleBinService {
    private static RecycleBinService ourInstance = new RecycleBinServiceImpl();

    public static RecycleBinService getInstance() {
        return ourInstance;
    }


    private RecycleBinServiceImpl() {
    }

    private RecycleBinController recycleBinController;
    AnchorPane pageNumbersContainer = new AnchorPane();
    Map<Label, Integer> pages = new HashMap<>();
    private MainStageService mainStageService = MainStageServiceImpl.getInstance();





    @Override
    public void initializeRecycleController(RecycleBinController recycleBinController) {
        this.recycleBinController = recycleBinController;
    }


    @Override
    public void loadDataInRecycleBin(BaseUserData baseUserData, int page) {
        long count = baseUserData.getPicturesData() == null ? 0 : baseUserData.getTotalElementCount();
        recycleBinController.countData.setText("( " + count + " )");
        drawPagination(baseUserData, page);
        drawRecycleContent(baseUserData);
        Stage mainStage = (Stage) recycleBinController.countData.getScene().getWindow();
        double countDiwth = new Text(recycleBinController.countData.getText()).getLayoutBounds().getWidth();
        double titleWidth = new Text("Recycle Bin").getLayoutBounds().getWidth();
        mainStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            recycleBinController.recicleBin.setPrefWidth(newValue.doubleValue());
            recycleBinController.bin_scroll.setPrefWidth(newValue.doubleValue());
            recycleBinController.bin_pagination.setPrefWidth(newValue.doubleValue());
            recycleBinController.bin_flowPane.setPrefWidth(newValue.doubleValue());
            recycleBinController.countData.setLayoutX((newValue.doubleValue()-countDiwth)/2-3);
            recycleBinController.recycleTitle.setLayoutX((newValue.doubleValue()-titleWidth)/2-3);
        });
        mainStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            recycleBinController.recicleBin.setPrefHeight(newValue.doubleValue());
            recycleBinController.bin_scroll.setPrefHeight(newValue.doubleValue());
            recycleBinController.bin_flowPane.setPrefHeight(newValue.doubleValue());
//            recycleBinController.bin_pagination.setPrefHeight(newValue.doubleValue());
        });



    }


    public void drawRecycleContent(BaseUserData baseUserData) {
        List<ImageData> picturesData = baseUserData.getPicturesData();
        if (recycleBinController.bin_flowPane.getChildren() != null) {
            recycleBinController.bin_flowPane.getChildren().remove(0, recycleBinController.bin_flowPane.getChildren().size());
        }
        Tooltip recover = new Tooltip("Recover");
        Tooltip deleteTooltip = new Tooltip("Delete");
        hackTooltipStartTiming(recover);
        hackTooltipStartTiming(deleteTooltip);
        for (ImageData pictureData : picturesData) {
            AnchorPane cellContainer = new AnchorPane();
            cellContainer.setPrefWidth(308.0);
            cellContainer.setPrefHeight(208.0);
            cellContainer.setId("bin_" + pictureData.getPicName());
//            cellContainer.setStyle("-fx-cursor: hand");

            Image image = new Image(Constant.SERVER_ADDRESS + Constant.IMAGE_URI + pictureData.getPicName());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(cellContainer.getPrefWidth());
            imageView.setFitHeight(cellContainer.getPrefHeight());

            Label imageDate = new Label(pictureData.getDeletedAt().split("T")[0]);
            imageDate.setFont(Font.font(null, FontWeight.BOLD, 13));
            imageDate.setTextFill(Paint.valueOf("#F7001D"));
            imageDate.setLayoutX(cellContainer.getLayoutX() + 2);
            imageDate.setLayoutY(cellContainer.getLayoutY() + 2);
            imageDate.setVisible(false);


            Image recoverImage = new Image(this.getClass().getResourceAsStream("/image/restore.png"));
            ImageView recoverButtonImageIco = new ImageView(recoverImage);
            recoverButtonImageIco.setFitHeight(25.0);
            recoverButtonImageIco.setFitWidth(35.0);
            Label revoverButton = new Label();
            revoverButton.setGraphic(recoverButtonImageIco);
            revoverButton.setStyle("-fx-cursor: hand");
            revoverButton.setVisible(false);
            revoverButton.setLayoutX(cellContainer.getLayoutX() + 2);
            revoverButton.setLayoutY(cellContainer.getLayoutY() + cellContainer.getPrefHeight() - 31);

            Image deleteWհiteIco = new Image(this.getClass().getResourceAsStream("/image/deleteWhiteIco.png"));
            Image deleteGreen = new Image(this.getClass().getResourceAsStream("/image/delete_green.png"));
            ImageView deleteIco = new ImageView(deleteWհiteIco);
            deleteIco.setFitHeight(36.0);
            deleteIco.setFitWidth(36.0);
            Label delete = new Label();
            delete.setVisible(false);
            delete.setGraphic(deleteIco);
            delete.setLayoutX(cellContainer.getLayoutX() + cellContainer.getPrefWidth() - 38);
            delete.setLayoutY(cellContainer.getLayoutY() + cellContainer.getPrefHeight() - 40);
            delete.setStyle("-fx-cursor: hand");
            delete.setOnMouseEntered(event -> {
                deleteIco.setImage(deleteGreen);
            });
            delete.setOnMouseExited(event -> {
                deleteIco.setImage(deleteWհiteIco);
            });

            AnchorPane fone = new AnchorPane();
            fone.setPrefWidth(308);
            fone.setPrefHeight(208);
            fone.setStyle("-fx-background-color:  rgba(0,0,0,0.36) #000000;");
            fone.setVisible(false);

            cellContainer.getChildren().addAll(imageView, fone,imageDate, revoverButton,delete);
            FlowPane.setMargin(cellContainer, new Insets(5, 5, 5, 5));

            revoverButton.setTooltip(recover);
            delete.setTooltip(deleteTooltip);

            delete.setOnMouseClicked(mouseEvent->{
                ApiConnection.getInstance().deleteImage(pictureData.getPicName());
                recycleBinController.bin_flowPane.getChildren().remove(cellContainer);

            });

            revoverButton.setOnMouseClicked(mouseEvent->{
                ApiConnection.getInstance().recoverImageStatus(ImageManagerRequest.builder()
                        .picName(pictureData.getPicName())
                        .actionType("remake")
                        .build());
                recycleBinController.bin_flowPane.getChildren().remove(cellContainer);
            });
            cellContainer.setOnMouseEntered(mouseEvent -> {
                fone.setVisible(true);
                delete.setVisible(true);
                revoverButton.setVisible(true);
                imageDate.setVisible(true);
            });
            cellContainer.setOnMouseExited(mouseEvent -> {
                fone.setVisible(false);
                delete.setVisible(false);
                imageDate.setVisible(false);
                revoverButton.setVisible(false);
            });

            recycleBinController.bin_flowPane.getChildren().add(cellContainer);
        }


    }





    @Override
    public void drawPagination(BaseUserData baseUserData, int page) {
        pages.clear();
        pageNumbersContainer.setStyle("-fx-alignment: center");
        Label label;

        ReadOnlyDoubleProperty widthPrp = mainStageService.getMainPane().widthProperty();
//        ReadOnlyDoubleProperty widthPrp = recycleBinController.bin_pagination.widthProperty();
        if (pageNumbersContainer.getChildren() != null && pageNumbersContainer.getChildren().size() > 0) {
            pageNumbersContainer.getChildren().removeAll(pageNumbersContainer.getChildren());
            pageNumbersContainer = new AnchorPane();
        }
        for (int i = 0; i < baseUserData.getTotoalPageCount(); i++) {  //addes pages
            label = new Label(String.valueOf(i + 1));
            label.setLayoutX(widthPrp.getValue() / 2 - (baseUserData.getTotoalPageCount() - 1) * 20 + (i-1) * 20);
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
                if (page!= (finalI + 1)) {
                    ApiConnection.getInstance().getDeletedImagePage(finalI + 1);
                }
            });

            pageNumbersContainer.getChildren().add(label);
            if (recycleBinController.bin_pagination.getChildren() != null) {
                recycleBinController.bin_pagination.getChildren().remove(0, recycleBinController.bin_pagination.getChildren().size());
            }
            recycleBinController.bin_pagination.getChildren().add(pageNumbersContainer);
            pages.put(label, i + 1);
        }
        widthPrp.addListener((observable, oldValue, newValue) -> {
            int numbverIndex;
            for (Label label1 : pages.keySet()) {
                numbverIndex = pages.get(label1);
                label1.setLayoutX(newValue.doubleValue() / 2 - (pages.size() - 1) * 20 + (numbverIndex -1)* 20);
            }
        });
    }


    private   void hackTooltipStartTiming(Tooltip tooltip) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(1)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
