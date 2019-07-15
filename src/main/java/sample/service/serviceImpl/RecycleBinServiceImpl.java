package sample.service.serviceImpl;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.Data;
import sample.Constant;
import sample.connection.ApiConnection;
import sample.controller.RecycleBinController;
import sample.dataTransferObject.request.ImageManagerRequest;
import sample.dataTransferObject.response.BaseUserData;
import sample.dataTransferObject.response.ImageData;
import sample.service.MainStageService;
import sample.service.RecycleBinService;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
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
    public volatile Map<Integer, String> selectedImage = new HashMap<>();
    private HashMap<CheckBox, Integer> checkboxes = new HashMap();
    private IntegerProperty currentPage = new SimpleIntegerProperty();
    private volatile Timeline anim;
    private volatile Timeline fadeIn;

    @Override
    public HashMap<CheckBox, Integer> getCheckBoxes() {
        return checkboxes;
    }

    @Override
    public  Map<Integer, String> getSelcetedImages() {
        return selectedImage;
    }

    @Override
    public int getCurrentPage(){
        return currentPage.get();
    }

    @Override
    public void initializeRecycleController(RecycleBinController recycleBinController) {
        this.recycleBinController = recycleBinController;
    }


    @Override
    public void loadDataInRecycleBin(BaseUserData baseUserData, int page) {
        currentPage.set(page);
        recoverControllButtons();
        clearSelectedImageCollection();
        recycleBinController.bin_selectAll.setVisible(false);
        recycleBinController.bin_selectAll.setSelected(false);
        closeAllCheckBoxes();
        long count = baseUserData.getPicturesData() == null ? 0 : baseUserData.getTotalElementCount();
        recycleBinController.countData.setText("( " + count + " )");
        drawPagination(baseUserData, page);
        drawRecycleContent(baseUserData);
        Stage mainStage = (Stage) recycleBinController.countData.getScene().getWindow();
        double countDiwth = new Text(recycleBinController.countData.getText()).getLayoutBounds().getWidth();
        double titleWidth = new Text("Recycle Bin").getLayoutBounds().getWidth();
        animationForPagination();
        mainStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            recycleBinController.recicleBin.setPrefWidth(newValue.doubleValue());
            recycleBinController.bin_scroll.setPrefWidth(newValue.doubleValue());
            recycleBinController.bin_pagination.setPrefWidth(newValue.doubleValue());
            recycleBinController.bin_flowPane.setPrefWidth(newValue.doubleValue());
            recycleBinController.menuBar.setPrefWidth(newValue.doubleValue());
            recycleBinController.bin_header.setPrefWidth(newValue.doubleValue());
            recycleBinController.bin_delete_batch.setLayoutX(newValue.doubleValue() - 330);
            recycleBinController.recover.setLayoutX(newValue.doubleValue() - 172);
            recycleBinController.countData.setLayoutX((newValue.doubleValue() - countDiwth) / 2 - 3);
            recycleBinController.recycleTitle.setLayoutX((newValue.doubleValue() - titleWidth) / 2 - 3);

        });
        mainStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            recycleBinController.bin_pagination.setLayoutY(newValue.doubleValue() - 60);
            recycleBinController.recicleBin.setPrefHeight(newValue.doubleValue());
            recycleBinController.bin_scroll.setPrefHeight(newValue.doubleValue() - 150);
            recycleBinController.bin_flowPane.setPrefHeight(newValue.doubleValue() - 155);
//            recycleBinController.bin_pagination.setPrefHeight(newValue.doubleValue());
        });


    }


    @Override
    public void animationForPagination() {
        recycleBinController.bin_pagination.setVisible(true);
        final DoubleProperty opacity = recycleBinController.bin_pagination.opacityProperty();
//        Timeline showPage = new Timeline(
//                new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
//                new KeyFrame(new Duration(1000), new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent t) {
////
//
//                        Timeline fadeIn = new Timeline(
//                                new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
//                                new KeyFrame(new Duration(800), new KeyValue(opacity, 1.0)));
//                        fadeIn.play();
//                    }
//                }, new KeyValue(opacity, 0.0)));
//        showPage.play();
        anim = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                new KeyFrame(new Duration(5000), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        fadeIn = new Timeline(
                                new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                                new KeyFrame(new Duration(1000), new KeyValue(opacity, 0.0)));
                        recycleBinController.bin_pagination.setVisible(false);
                        fadeIn.play();
                    }
                }, new KeyValue(opacity, 1.0)));
        anim.play();
    }

    @Override
    public void stopAnimationForPagination() {
        this.fadeIn.stop();
        this.anim.stop();
    }

    @Override
    public void showCheckBoxes() {
        for (CheckBox checkBox : checkboxes.keySet()) {
            checkBox.setVisible(true);
        }
    }

    @Override
    public void selecetAllClick() {
        Scene scene = recycleBinController.bin_flowPane.getScene();
        String imageName;
        if (recycleBinController.bin_selectAll.isSelected()) {
            for (Map.Entry<CheckBox, Integer> checkBoxIntegerEntry : checkboxes.entrySet()) {
                checkBoxIntegerEntry.getKey().setSelected(true);
                ImageView imageView = (ImageView) scene.lookup("#bin" + checkBoxIntegerEntry.getValue());
                imageName = ((ImageView) scene.lookup("#bin" + checkBoxIntegerEntry.getValue()))
                        .getImage().impl_getUrl().split(Constant.SPLITER)[1];
                selectedImage.put(checkBoxIntegerEntry.getValue(), imageName);
            }
        } else {
            clearSelectedImageCollection();
            closeAllCheckBoxes();
            recoverControllButtons();
            recycleBinController.bin_selectAll.setSelected(false);
            recycleBinController.bin_selectAll.setVisible(false);
        }
    }

    @Override
    public void clearSelectedImageCollection() {
        for (Map.Entry<CheckBox, Integer> checkBoxIntegerEntry : checkboxes.entrySet()) {
            checkBoxIntegerEntry.getKey().setSelected(false);
            selectedImage.clear();
        }
    }


    @Override
    public void recoverControllButtons() {
        recycleBinController.bin_selectAll.setVisible(false);
        recycleBinController.bin_selectAll.setSelected(false);
        recycleBinController.recover.setDisable(false);
        recycleBinController.bin_delete_batch.setDisable(false);
        recycleBinController.bin_delete_batch.setStyle("-fx-cursor: hand;-fx-background-radius: 25; -fx-background-color: #fff;");
        recycleBinController.recover.setStyle("-fx-cursor: hand;-fx-background-radius: 25; -fx-background-color: #fff;");
    }

    @Override
    public void closeAllCheckBoxes() {
        for (CheckBox checkBox : checkboxes.keySet()) {
            checkBox.setSelected(false);
            checkBox.setVisible(false);
        }
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
        int index = 0;
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
            imageView.setId("bin" + index);

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

            CheckBox checkBox = new CheckBox();
            checkBox.setLayoutX(cellContainer.getLayoutX() + cellContainer.getPrefWidth() - 25);
            checkBox.setLayoutY(cellContainer.getLayoutY() + 5);
            checkBox.setVisible(false);
            checkBox.setId("binCheckBox_" + index);
            checkboxes.put(checkBox, index);
            int finalIndex = index;
            checkBox.setOnMouseClicked(event -> {
                singleSelecOrCancelItem(checkBox, finalIndex, pictureData.getPicName());

            });

            cellContainer.getChildren().addAll(imageView, fone, checkBox, imageDate, revoverButton, delete);
            FlowPane.setMargin(cellContainer, new Insets(5, 5, 5, 5));

            revoverButton.setTooltip(recover);
            delete.setTooltip(deleteTooltip);

            delete.setOnMouseClicked(mouseEvent -> {
                ApiConnection.getInstance().deleteImage(pictureData.getPicName());
                recycleBinController.bin_flowPane.getChildren().remove(cellContainer);

            });

            revoverButton.setOnMouseClicked(mouseEvent -> {
                ApiConnection.getInstance().recoverImageStatus(ImageManagerRequest.builder()
                        .picName(pictureData.getPicName())
                        .actionType("remake")
                        .build());
                recycleBinController.bin_flowPane.getChildren().remove(cellContainer);
            });
            cellContainer.setOnMouseEntered(mouseEvent -> {
                if (!recycleBinController.bin_selectAll.isVisible()) {
                    fone.setVisible(true);
                    delete.setVisible(true);
                    revoverButton.setVisible(true);
                    imageDate.setVisible(true);
                }
            });
            cellContainer.setOnMouseExited(mouseEvent -> {
                fone.setVisible(false);
                delete.setVisible(false);
                imageDate.setVisible(false);
                revoverButton.setVisible(false);
            });

            recycleBinController.bin_flowPane.getChildren().add(cellContainer);
            index++;
        }


    }

    private void singleSelecOrCancelItem(CheckBox checkBox, int index, String picName) {
        if (checkBox.isSelected()) {
            selectedImage.put(index, picName);
        } else {
            selectedImage.keySet().removeIf(key -> key.equals(index));
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
            label.setLayoutX(widthPrp.getValue() / 2 - (baseUserData.getTotoalPageCount() - 1) * 20 + (i - 1) * 20);
            label.setFont(Font.font(null, FontWeight.BOLD, 14));
            label.setId(String.valueOf("page_" + i + 1));
            if ((i + 1) == page) {
                label.setTextFill(Paint.valueOf("#fff"));
                label.setStyle("font-weight: bold");
                label.setUnderline(true);
            }
            if ((i + 1) != page) {
                label.setStyle("-fx-cursor: hand; -fx-background-color: #fff; -fx-font-weight: regular");
            }
            int finalI = i;
            label.setOnMouseClicked(mouseEvent -> {
                if (page != (finalI + 1)) {
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
                label1.setLayoutX(newValue.doubleValue() / 2 - (pages.size() - 1) * 20 + (numbverIndex - 1) * 20);
            }
        });
    }


    private void hackTooltipStartTiming(Tooltip tooltip) {
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


    @Override
    public void deleteSelectedImage() {
        if (selectedImage != null && selectedImage.size() > 0) {

            openConfirmationDialogForRecycleDeleteInBatch("recycleMany");

        } else {
            recoverControllButtons();
            recycleBinController.bin_selectAll.setSelected(false);
            recycleBinController.bin_selectAll.setVisible(false);
            closeAllCheckBoxes();

        }
    }

    private void openConfirmationDialogForRecycleDeleteInBatch(String recycleMany) {
        if(recycleMany.equals("recycleMany")){
            Stage mainStage = new Stage();
            FXMLLoader fxmlLoader = null;
            Parent root = null;
            try {
                fxmlLoader = new FXMLLoader(getClass().getResource("/view/dialog/deleteConfirmationDialogForRecycle.fxml"));
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mainStage.setTitle("Confirmation dialog");
            mainStage.setResizable(false);
            mainStage.initModality(Modality.APPLICATION_MODAL);
            mainStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            mainStage.show();
        }
    }

    @Override
    public void recoverSelectedImages() {
        if (selectedImage != null && selectedImage.size() > 0) {
            closeAllCheckBoxes();
            checkboxes.clear();
            ApiConnection.getInstance().recoverImagesFromRecycle(sample.dataTransferObject.request.ImageData.builder()
                    .picNames(selectedImage.values())
                    .page(currentPage.get())
                    .build());
        } else {
            recoverControllButtons();
            recycleBinController.bin_selectAll.setSelected(false);
            recycleBinController.bin_selectAll.setVisible(false);
            closeAllCheckBoxes();
        }
    }
}
