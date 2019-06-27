package sample.service.serviceImpl;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.Constant;
import sample.Size;
import sample.connection.ApiConnection;
import sample.controller.MainStageController;
import sample.dataTransferObject.response.BaseUserData;
import sample.dataTransferObject.response.ImageData;
import sample.service.MainStageService;
import sample.storage.Storage;
import sample.util.Helper;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class MainStageServiceImpl implements MainStageService {
    private static MainStageServiceImpl mainStageService = new MainStageServiceImpl();
    public static IntegerProperty myImageCount;

    private DoubleProperty phoneNumberLenth;

    private MainStageServiceImpl() {
    }

    public static MainStageServiceImpl getInstance() {
        return mainStageService;
    }

    //local variables
    private MainStageController mainStageController;
    Map<Label, Integer> pages = new HashMap<>();
    public volatile IntegerProperty currentPageIndex = new SimpleIntegerProperty(1);
    public volatile Map<CheckBox, Integer> checkboxes = new HashMap<>();
    public volatile Map<Integer, String> selectedImage = new HashMap<>();

    AnchorPane pageNumbersContainer = new AnchorPane();


    @Override
    public void responsivWidth(double stageWith) {
        mainStageController.imageCountInto.setLayoutX(28.0);
        mainStageController.memoryProgressBar.setLayoutX(81);
        mainStageController.memoryHint.setLayoutX(28.0);
//        +++layoutX="14.0"
        mainStageController.floxPane.setPrefWidth(stageWith - 10);
        mainStageController.slideController.responsiveWidth(stageWith);
        if (stageWith <= 1000.0) {
            mainStageController.memoryProgressBar.setPrefWidth(Size.WIDTH_COEFFICENT_FOR_PROGRESS_BAR_WIDTH * stageWith);
            mainStageController.fraction.setLayoutX(Size.WIDTH_COEFFICENT_FOR_PROGRESS_BAR_WIDTH * stageWith + mainStageController.memoryProgressBar.getLayoutX() + 7);
        }
        mainStageController.logoCamerImageVeiw.setLayoutX(Size.WIDTH_COEFFICENT_FOR_CAMERA_LOGO_CAMERA * stageWith);
        mainStageController.share.setLayoutX(stageWith - 299);
        mainStageController.delete.setLayoutX(stageWith - 202);
        mainStageController.download.setLayoutX(stageWith - 105);
        mainStageController.logOut_btn.setLayoutX(stageWith - 130);
        mainStageController.recycle_btn.setLayoutX(stageWith - 227);
        mainStageController.scrollPane.setPrefWidth(stageWith - 40);
        mainStageController.cell_containerAnchorPane.setPrefWidth(stageWith);
        mainStageController.cell_containerAnchorPane.setLayoutX(20);
        mainStageController.filterPane.setPrefWidth(stageWith);
        mainStageController.pageNumbersPane.setPrefWidth(stageWith);
        mainStageController.headerRow1.setPrefWidth(stageWith);
        mainStageController.phoneNumber.setLayoutX(mainStageController.recycle_btn.getLayoutX() - phoneNumberLenth.getValue() - 8);
    }

    @Override
    public void initializeMainStageController(MainStageController mainStageController) {
        this.mainStageController = mainStageController;
    }


    @Override
    public void loadMainStageData(BaseUserData baseUserData, int loadedPageNumber) {
        int totoalPageCount = baseUserData.getTotoalPageCount();
        ReadOnlyDoubleProperty widthPrp = mainStageController.pageNumbersPane.widthProperty();
        pageNumbersContainer.setStyle("-fx-alignment: center");
        Label label;
        if (pageNumbersContainer.getChildren() != null && pageNumbersContainer.getChildren().size() > 0) {
            pageNumbersContainer.getChildren().removeAll(pageNumbersContainer.getChildren());
            pageNumbersContainer = new AnchorPane();
            pages.clear();
        }
        for (int i = 0; i < totoalPageCount; i++) {
            label = new Label(String.valueOf(i + 1));
            label.setLayoutX(widthPrp.getValue() / 2 - (totoalPageCount - 1) * 20 + i * 20);
            label.setFont(Font.font(null, FontWeight.BOLD, 14));
            label.setId(String.valueOf(i + 1));
            if ((i + 1) == loadedPageNumber) {
                label.setTextFill(Paint.valueOf("#388e3c"));
                label.setUnderline(true);
                pages.put(label, i + 1);
            }
            if ((i + 1) != loadedPageNumber) {
                label.setStyle("-fx-cursor: hand");
            }
            int finalI = i;
            label.setOnMouseClicked(mouseEvent -> {
                if (currentPageIndex.get() != (finalI + 1)) {
                    ApiConnection.getInstance().getPage(Constant.BASE_DATA_URI, finalI + 1, Storage.getInstance().getCurrentToken());
                    currentPageIndex.set(finalI + 1);
                }
            });
            pages.put(label, i + 1);
            pageNumbersContainer.getChildren().add(label);
        }
        widthPrp.addListener((observable, oldValue, newValue) -> {
            int numbverIndex;
            for (Label label1 : pages.keySet()) {
                numbverIndex = pages.get(label1);
                label1.setLayoutX(newValue.doubleValue() / 2 - (pages.size() - 1) * 20 + numbverIndex * 20);
            }
        });
        mainStageController.pageNumbersPane.setStyle("-fx-alignment: center");
        mainStageController.pageNumbersPane.getChildren().add(pageNumbersContainer);
        mainStageController.fraction.setText(baseUserData.getFruction());
        myImageCount = new SimpleIntegerProperty(Integer.parseInt(baseUserData.getFruction().split("/")[0]));
        int imagesConunt = baseUserData.getPicturesData() == null ? 0 : baseUserData.getPicturesData().size();
        mainStageController.imageCountInto.setText("Storage (" + imagesConunt + ")");
        mainStageController.phoneNumber.setText(baseUserData.getPhoneNumber());
        phoneNumberLenth = new SimpleDoubleProperty(mainStageController.phoneNumber.getLayoutBounds().getWidth());
        mainStageController.phoneNumber.setLayoutX(mainStageController.recycle_btn.getLayoutX() - phoneNumberLenth.getValue() - 8);
        mainStageController.memoryProgressBar.setProgress((double) imagesConunt / 500.0);
        if (imagesConunt != 0) {
            drawImagesInMainStage(baseUserData.getPicturesData(), Storage.getInstance().getCurrentToken());
        } else {
            // TODO something when user does not have pictures ...
        }
    }

    @Override
    public void logOut() throws IOException {
        Storage storage = Storage.getInstance();
        storage.setCurrentToken("");
        storage.setToken("");
        ((Stage) mainStageController.headerRow1.getScene().getWindow()).close();
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Log in");
        javafx.scene.image.Image logo = new Image(this.getClass().getResourceAsStream("/image/logo.png"));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(logo);


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(we -> {
            System.exit(0);
        });
        primaryStage.show();
    }


    @Override
    public void drawImagesInMainStage(List<ImageData> picturesData, String currentToken) {
        boolean firstTime = true;
        checkboxes.clear();
        int index = 0;
        for (ImageData pictureData : picturesData) {

            AnchorPane cellContent = new AnchorPane();
            cellContent.setPrefWidth(200.0);
            cellContent.setPrefHeight(177.0);
            FlowPane.setMargin(cellContent, new Insets(5.0, 5.0, 5.0, 5.0));
            cellContent.setId(pictureData.getPicName());
            cellContent.setStyle("-fx-cursor: hand");

            Image image = new Image(Constant.SERVER_ADDRESS + Constant.IMAGE_URI + pictureData.getPicName());
            ImageView imageView = new ImageView(image);
            imageView.setId("" + index);                               //ամեն նկարի որպես id տրվում է իր ինդեքսի համարը
            imageView.setFitWidth(cellContent.getPrefWidth());
            imageView.setFitHeight(cellContent.getPrefHeight());

            Label imageDate = new Label(pictureData.getCreatedAt().split(":")[0] + ":" + pictureData.getCreatedAt().split(":")[1]);
            imageDate.setFont(Font.font(null, FontWeight.BOLD, 12));
            imageDate.setTextFill(Paint.valueOf("#d7d7d7"));
            imageDate.setLayoutX(cellContent.getLayoutX() + 2);
            imageDate.setLayoutY(cellContent.getLayoutY() + 2);
            imageDate.setVisible(false);

            Image deleteWհiteIco = new Image(this.getClass().getResourceAsStream("/image/deleteWhiteIco.png"));
            ImageView deleteIco = new ImageView(deleteWհiteIco);
            deleteIco.setFitHeight(25.0);
            deleteIco.setFitWidth(25.0);
            Label delete = new Label();
            delete.setGraphic(deleteIco);
            delete.setVisible(false);
            delete.setLayoutX(cellContent.getLayoutX() + cellContent.getPrefWidth() - 30);
            delete.setLayoutY(cellContent.getLayoutY() + cellContent.getPrefHeight() - 31);

            Image shateWհiteIco = new Image(this.getClass().getResourceAsStream("/image/shareWhite.png"));
            ImageView shareIco = new ImageView(shateWհiteIco);
            shareIco.setFitHeight(25.0);
            shareIco.setFitWidth(25.0);
            Label share = new Label();
            share.setGraphic(shareIco);
            share.setLayoutX(cellContent.getLayoutX() + cellContent.getPrefWidth() - 62);
            share.setLayoutY(cellContent.getLayoutY() + cellContent.getPrefHeight() - 31);
            share.setVisible(false);

            ProgressBar progressBar = new ProgressBar();
            progressBar.setProgress(0);
            progressBar.setPrefWidth(150);
            progressBar.setLayoutX(cellContent.getLayoutX() + 25); // in the middle of thecellContent
            progressBar.setLayoutY(cellContent.getLayoutY() + cellContent.getPrefHeight() / 2);
            progressBar.setVisible(false);


            Image downlodWհiteIco = new Image(this.getClass().getResourceAsStream("/image/downloadWhite.png"));
            ImageView downloadIco = new ImageView(downlodWհiteIco);
            downloadIco.setFitHeight(25.0);
            downloadIco.setFitWidth(25.0);
            Label download = new Label();
            download.setGraphic(downloadIco);
            download.setLayoutX(cellContent.getLayoutX() + cellContent.getPrefWidth() - 94);
            download.setLayoutY(cellContent.getLayoutY() + cellContent.getPrefHeight() - 31);
            download.setPrefHeight(25.0);
            download.setPrefWidth(25.0);
            download.setVisible(false);
            Label percent = new Label("0 %");
            Text text = new Text(percent.getText());

            percent.setLayoutX(progressBar.getLayoutX() + progressBar.getPrefWidth() / 2 - text.getLayoutBounds().getWidth() / 2);
            percent.setLayoutY(progressBar.getLayoutY() - 20);
            percent.setTextFill(Paint.valueOf("#d7d7d7"));
            percent.setVisible(false);

            CheckBox checkBox = new CheckBox();
            checkBox.setLayoutX(cellContent.getLayoutX() + cellContent.getPrefWidth() - 25);
            checkBox.setLayoutY(cellContent.getLayoutY() + 5);
            checkBox.setVisible(false);
            checkboxes.put(checkBox, index);

            int finalIndex = index;
            checkBox.setOnMouseClicked(mouseEvent->{
                singleSelectOrCancelItem(checkBox,finalIndex,pictureData.getPicName());
            });
            cellContent.setOnMouseEntered(mouseEvent -> {
                imageDate.setVisible(true);
                delete.setVisible(true);
                share.setVisible(true);
                download.setVisible(true);


            });
            cellContent.setOnMouseExited(mouseEvent -> {
                imageDate.setVisible(false);
                delete.setVisible(false);
                share.setVisible(false);
                download.setVisible(false);

            });


            cellContent.getChildren().addAll(imageView, imageDate, delete, share, progressBar, percent, download, checkBox);

            if (firstTime && mainStageController.floxPane.getChildren() != null && mainStageController.floxPane.getChildren().size() > 0) {

                mainStageController.floxPane.getChildren().remove(0, mainStageController.floxPane.getChildren().size());
            }
            mainStageController.floxPane.getChildren().add(cellContent);
            firstTime = false;

            delete.setOnMouseClicked(mouseEvent -> {
                DeleteDialogServiceImpl.getInstance().openConfirmationDialog(pictureData.getPicName(), "cell", mainStageController.floxPane.getChildren().indexOf(cellContent));
            });
            download.setOnMouseClicked(mouseEvent -> {
                downloadImage(pictureData.getPicName(), progressBar);
            });
            imageView.setOnMouseClicked(mouseEvent -> {

                SliderServiceImpl.getInstance().openSlider(pictureData.getPicName(), (currentPageIndex.getValue() - 1) * 50 + mainStageController.floxPane.getChildren().indexOf(cellContent));
            });
            index++;
        }
    }

    /**
     * Download image from server
     *
     * @param picName
     */
    @Override
    public void downloadImage(String picName, ProgressBar progressBar) {
        progressBar.setProgress(0);
        progressBar.setVisible(true);
        try {
            URL url = new URL(Constant.SERVER_ADDRESS + Constant.IMAGE_URI + picName);
            URLConnection urlConnection = url.openConnection();
            double pictureSize = new Double(urlConnection.getHeaderField("pictureSize") + "D");
            urlConnection.connect();
            String dirPath = Helper.getInstance().decideDirectionPath();
            InputStream inputStream = urlConnection.getInputStream();
            int bufferSize = 128;
            byte[] buffer = new byte[bufferSize];

            BufferedOutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(dirPath + picName));
            int len = 0;
            double downloadedSize = 0;
            progressBar.setProgress(0);
            progressBar.setVisible(true);
            while ((len = inputStream.read(buffer, 0, bufferSize)) > -1) {
                outputStream.write(buffer, 0, len);
                downloadedSize += progressBar.getProgress() + len;
                double v = (downloadedSize) / pictureSize;
                progressBar.setProgress(v);
            }
            outputStream.flush();
            outputStream.close();
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));
            pauseTransition.play();
            pauseTransition.setOnFinished(event -> {
                Platform.runLater(() -> {
                    progressBar.setProgress(0);
                    progressBar.setVisible(false);
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void removeImageFromCellByIndex(int indexOfImageFromCell) {
        for (Map.Entry<CheckBox, Integer> checkBoxIntegerEntry : checkboxes.entrySet()) {
            if (checkBoxIntegerEntry.getValue() == indexOfImageFromCell) checkboxes.remove(checkBoxIntegerEntry);
        }
        mainStageController.floxPane.getChildren().remove(indexOfImageFromCell);
    }

    @Override
    public void loadPage(Response response, int loadedPageNumber) {
        Platform.runLater(() -> {
            if (response.isSuccessful()) { //HttpStatusCode = 200
                JSONObject responseJson = null;
                try {
                    responseJson = new JSONObject(new String(response.body().bytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                List<ImageData> imageData = new ArrayList<>();
                JSONArray picturesData = responseJson.getJSONArray("picturesData");
                JSONObject imageDataJson = null;
                if (picturesData != null) {
                    for (int i = 0; i < picturesData.length(); i++) {
                        System.out.println();
                        imageDataJson = picturesData.getJSONObject(i);
                        imageData.add(ImageData.builder()
                                .createdAt(imageDataJson.getString("createdAt"))
                                .picName(imageDataJson.getString("picName"))
                                .picSize(imageDataJson.getDouble("picSize"))
                                .build());
                    }
                }
                loadMainStageData(BaseUserData.builder()
                        .fruction(responseJson.getString("fruction"))
                        .totoalPageCount(responseJson.getInt("totoalPageCount"))
                        .picturesData(imageData)
                        .phoneNumber(responseJson.getString("phoneNumber"))
                        .build(), loadedPageNumber);
            } else if (response.code() == 401) {
//          TODO something
//          TODO something
            } else if (response.code() == 403) {
//          TODO something
            } else if (response.code() == 410) {
//          TODO something
            } else {
//          TODO something
            }
        });
    }

    @Override
    public void showCheckBoxes() {
        for (CheckBox checkBox : checkboxes.keySet()) {
            checkBox.setVisible(true);
        }
        mainStageController.selectAllHint.setVisible(true);
        mainStageController.selectALL_checkBox.setVisible(true);
    }

    @Override
    public void selectOrCancelItems() {
        Scene scene = mainStageController.floxPane.getScene();
        ObservableList<Node> items = mainStageController.floxPane.getChildren();
        String imageName;
        if (mainStageController.selectALL_checkBox.isSelected()) {
            for (Map.Entry<CheckBox, Integer> checkBoxIntegerEntry : checkboxes.entrySet()) {
                checkBoxIntegerEntry.getKey().setSelected(true);
                imageName = ((ImageView) scene.lookup("#" + checkBoxIntegerEntry.getValue()))
                        .getImage().impl_getUrl().split(Constant.SPLITER)[1];
                selectedImage.put(checkBoxIntegerEntry.getValue(),imageName);
            }
        } else {
            for (Map.Entry<CheckBox, Integer> checkBoxIntegerEntry : checkboxes.entrySet()) {
                checkBoxIntegerEntry.getKey().setSelected(false);
                selectedImage.clear();
            }
        }

    }
    @Override
    public void singleSelectOrCancelItem(CheckBox checkBox,int index,String imageName){
        if(checkBox.isSelected()){
            selectedImage.put(index,imageName);
        }else {
            for (Map.Entry<Integer, String> entry : selectedImage.entrySet()) {
                if(entry.getKey().equals(index)){
                 selectedImage.remove(entry);
                }
            }
        }
    };
}
