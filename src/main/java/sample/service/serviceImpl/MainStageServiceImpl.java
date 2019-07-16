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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.Constant;
import sample.connection.ApiConnection;
import sample.controller.MainStageController;
import sample.dataTransferObject.response.BaseUserData;
import sample.dataTransferObject.response.ImageData;
import sample.model.viewModel.FilterMonth;
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
import java.util.List;

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
    private StringProperty filterAction = new SimpleStringProperty("FIRST_SHOW");  //NEW_FILTER, SAVE_FILTER

    AnchorPane pageNumbersContainer = new AnchorPane();


    @Override
    public void responsivWidth(double stageWith) {
//        mainStageController.memoryProgressBar.setLayoutX(81);
//        mainStageController.memoryHint.setLayoutX(28.0);
//        +++layoutX="14.0"
//        mainStageController.flowPane.setPrefWidth(stageWith - 10);
//        mainStageController.slideController.responsiveWidth(stageWith);
//        if (stageWith <= 1000.0) {
//            mainStageController.memoryProgressBar.setPrefWidth((200.0/722.0) * stageWith);
//            mainStageController.fraction
//                    .setLayoutX((200.0/722.0) * stageWith + mainStageController.memoryProgressBar.getLayoutX() + 7);
//        }
//        mainStageController.logoCamerImageVeiw.setLayoutX(Size.WIDTH_COEFFICENT_FOR_CAMERA_LOGO_CAMERA * stageWith);
//        mainStageController.delete.setLayoutX(stageWith - 202);
//        mainStageController.download.setLayoutX(stageWith - 105);
//        mainStageController.logOut_btn.setLayoutX(stageWith - 130);
//        mainStageController.recycle_btn.setLayoutX(stageWith - 227);
//        mainStageController.cell_containerAnchorPane.setPrefWidth(stageWith);
//        mainStageController.cell_containerAnchorPane.setLayoutX(20);
//        mainStageController.filterPane.setPrefWidth(stageWith);
//        mainStageController.pageNumbersPane.setPrefWidth(stageWith);
//        mainStageController.phoneNumber.setLayoutX(mainStageController.recycle_btn.getLayoutX() - phoneNumberLenth.getValue() - 8);
    }

    @Override
    public void initializeMainStageController(MainStageController mainStageController) {
        this.mainStageController = mainStageController;
    }


    @Override
    public void loadMainStageData(BaseUserData baseUserData, int loadedPageNumber, String filter_search) {
        Scene scene = mainStageController.flowPane.getScene();
        selectedImage.clear();
        mainStageController.download.setDisable(false);
        mainStageController.delete.setDisable(false);
        int totoalPageCount = baseUserData.getTotoalPageCount();
        ReadOnlyDoubleProperty widthPrp = mainStageController.pageNumbersPane.widthProperty();
        pageNumbersContainer.setStyle("-fx-alignment: center");
        Label label;
        if (pageNumbersContainer.getChildren() != null && pageNumbersContainer.getChildren().size() > 0) {
            pageNumbersContainer.getChildren().removeAll(pageNumbersContainer.getChildren());
            pageNumbersContainer = new AnchorPane();
            pages.clear();
        }
        for (int i = 0; i < totoalPageCount; i++) {  //addes pages

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            label = new Label(String.valueOf(i + 1));
            label.setLayoutX(widthPrp.getValue() / 2 - (totoalPageCount - 1) * 20 + i * 20);
            label.setFont(Font.font(null, FontWeight.BOLD, 14));
            label.setId(String.valueOf("page_" + i + 1));
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
                    mainStageController.currentPageNumber.setText("" + (finalI + 1));
                    ApiConnection.getInstance().getPage(Constant.BASE_DATA_URI, finalI + 1,
                            Storage.getInstance().getCurrentToken());
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
        if (mainStageController.pageNumbersPane.getChildren() != null) {
            mainStageController.pageNumbersPane.getChildren().remove(0, mainStageController.pageNumbersPane.getChildren().size());
        }
        mainStageController.pageNumbersPane.getChildren().add(pageNumbersContainer);
        int imagesConunt = baseUserData.getPicturesData() == null ? 0 : baseUserData.getPicturesData().size();
        if (!filter_search.equals("filter_search")) {
            mainStageController.phoneNumber.setText(baseUserData.getPhoneNumber());
            phoneNumberLenth = new SimpleDoubleProperty(mainStageController.phoneNumber.getLayoutBounds().getWidth());
            mainStageController.phoneNumber.setLayoutX(mainStageController.recycle_btn.getLayoutX() - phoneNumberLenth.getValue() - 8);
            mainStageController.fraction.setText(baseUserData.getFruction());
            myImageCount = new SimpleIntegerProperty(Integer.parseInt(baseUserData.getFruction().split("/")[0]));
            mainStageController.memoryProgressBar.setProgress((double) imagesConunt / 500.0);
        }
        if (imagesConunt != 0) {
            drawImagesInMainStage(baseUserData.getPicturesData(), Storage.getInstance().getCurrentToken());
        } else {
            mainStageController.pageNumbersPane.getChildren().remove(0, mainStageController.pageNumbersPane.getChildren().size());
            pageNumbersContainer.getChildren().remove(0, pageNumbersContainer.getChildren().size());
            Label no_content = new Label("No Content");
            no_content.setFont(Font.font(null, FontWeight.BOLD, 14));
            no_content.setTextFill(Paint.valueOf("#388e3c"));
            pageNumbersContainer.getChildren().add(no_content);
            mainStageController.pageNumbersPane.widthProperty().addListener((observable, oldValue, newValue) -> {
                no_content.setLayoutX(newValue.doubleValue() / 2 - 5);
            });
            mainStageController.pageNumbersPane.getChildren().add(pageNumbersContainer);
            no_content.setLayoutX(mainStageController.mainStage.getWidth() / 2 - 5);
            if (mainStageController.flowPane.getChildren() != null) //jnjel flowPane-i naxkin parunakutyuny
                mainStageController.flowPane.getChildren().remove(0, mainStageController.flowPane.getChildren().size());
            // TODO something when user does not have pictures ...
        }
    }

    @Override
    public void logOut() throws IOException {
        Storage storage = Storage.getInstance();
        storage.setCurrentToken("");
        storage.setToken("");
        ((Stage) mainStageController.header.getScene().getWindow()).close();
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
            cellContent.setPrefWidth(308);
            cellContent.setPrefHeight(208);
            FlowPane.setMargin(cellContent, new Insets(5.0, 5.0, 5.0, 5.0));
            cellContent.setId(pictureData.getPicName());
//            cellContent.setStyle("-fx-cursor: hand");

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
            deleteIco.setFitHeight(36.0);
            deleteIco.setFitWidth(36.0);
            Label delete = new Label();
            delete.setGraphic(deleteIco);
            delete.setVisible(false);
            delete.setLayoutX(cellContent.getLayoutX() + cellContent.getPrefWidth() - 36);
            delete.setLayoutY(cellContent.getLayoutY() + cellContent.getPrefHeight() - 40);

            Image shateWհiteIco = new Image(this.getClass().getResourceAsStream("/image/shareWhite.png"));
            ImageView shareIco = new ImageView(shateWհiteIco);
            shareIco.setFitHeight(36.0);
            shareIco.setFitWidth(36.0);
            Label share = new Label();
            share.setGraphic(shareIco);
            share.setLayoutX(cellContent.getLayoutX() + cellContent.getPrefWidth() - 75);
            share.setLayoutY(cellContent.getLayoutY() + cellContent.getPrefHeight() - 40);
            share.setVisible(false);

            ProgressBar progressBar = new ProgressBar();
            progressBar.setProgress(0);
            progressBar.setPrefWidth(150);
            progressBar.setLayoutX(cellContent.getLayoutX() + 25); // in the middle of thecellContent
            progressBar.setLayoutY(cellContent.getLayoutY() + cellContent.getPrefHeight() / 2);
            progressBar.setId("progressBar_" + index);   //progressbarin drubum e progressBar_id
            progressBar.setVisible(false);


            Image downlodWհiteIco = new Image(this.getClass().getResourceAsStream("/image/downloadWhite.png"));
            Image deleteGrin = new Image(this.getClass().getResourceAsStream("/image/delete_green.png"));
            Image shareGreen = new Image(this.getClass().getResourceAsStream("/image/share_green.png"));
            Image downloadGreen = new Image(this.getClass().getResourceAsStream("/image/download_green.png"));
            ImageView downloadIco = new ImageView(downlodWհiteIco);
            downloadIco.setFitHeight(36.0);
            downloadIco.setFitWidth(36.0);
            Label download = new Label();
            download.setGraphic(downloadIco);
            download.setLayoutX(cellContent.getLayoutX() + cellContent.getPrefWidth() - 114);
            download.setLayoutY(cellContent.getLayoutY() + cellContent.getPrefHeight() - 40);
            download.setPrefHeight(36.0);
            download.setPrefWidth(36.0);
            download.setVisible(false);
            Label percent = new Label("0 %");
            Text text = new Text(percent.getText());

            delete.setOnMouseEntered(event -> {
                deleteIco.setImage(deleteGrin);
            });
            delete.setOnMouseExited(event -> {
                deleteIco.setImage(deleteWհiteIco);
            });

            share.setOnMouseEntered(event -> {
                shareIco.setImage(shareGreen);
            });
            share.setOnMouseExited(event -> {
                shareIco.setImage(shateWհiteIco);

            });

            download.setOnMouseEntered(event -> {
                downloadIco.setImage(downloadGreen);
            });
            download.setOnMouseExited(event -> {
                downloadIco.setImage(downlodWհiteIco);
            });

            percent.setLayoutX(progressBar.getLayoutX() + progressBar.getPrefWidth() / 2 - text.getLayoutBounds().getWidth() / 2);
            percent.setLayoutY(progressBar.getLayoutY() - 20);
            percent.setTextFill(Paint.valueOf("#d7d7d7"));
            percent.setVisible(false);

            CheckBox checkBox = new CheckBox();
            checkBox.setLayoutX(cellContent.getLayoutX() + cellContent.getPrefWidth() - 25);
            checkBox.setLayoutY(cellContent.getLayoutY() + 5);
            checkBox.setVisible(false);
            checkBox.setId("checkBox_" + index);
            checkboxes.put(checkBox, index);

            Image viewImage = new Image(this.getClass().getResourceAsStream("/image/eye.png"));
            ImageView viewImageIco = new ImageView(viewImage);
            viewImageIco.setFitHeight(25.0);
            viewImageIco.setFitWidth(35.0);
            Label viewImg = new Label();
            viewImg.setGraphic(viewImageIco);
            viewImg.setLayoutX(cellContent.getLayoutX() + 2);
            viewImg.setVisible(false);
            viewImg.setLayoutY(cellContent.getLayoutY() + cellContent.getPrefHeight() - 31);


            int finalIndex = index;
            checkBox.setOnMouseClicked(mouseEvent -> {
                singleSelectOrCancelItem(checkBox, finalIndex, pictureData.getPicName());
            });
            AnchorPane fone = new AnchorPane();
            fone.setPrefWidth(308);
            fone.setPrefHeight(208);
            fone.setStyle("-fx-background-color:  rgba(0,0,0,0.36) #000000;-fx-cursor: hand");

            fone.setVisible(false);

            cellContent.setOnMouseEntered(mouseEvent -> {
//                imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(148,135,135,0.23), 0.0, 25.0, 0.0,  19.0);-fx-cursor: hand");
//                imageView.setStyle("-fx-effect: dropshadow(gaussian, limegreen, 50, 0, 0, 0);-fx-background-insets: 2;-fx-background-color: rgba(255, 255, 255, 0.5);");
//                imageView.setStyle("-fx-effect: innershadow(gaussian, rgba(125,133,132,0.31), 50, 0, 0, 0);-fx-background-insets: 2;-fx-background-color: rgba(255, 255, 255, 0.5);");
                if (!checkBox.isVisible()) {
                    fone.setVisible(true);
                    imageDate.setVisible(true);
                    delete.setVisible(true);
                    share.setVisible(true);
                    download.setVisible(true);
                } else {
                    viewImg.setVisible(true);
                }


            });
            cellContent.setOnMouseExited(mouseEvent -> {
                fone.setVisible(false);
                imageView.setStyle("-fx-cursor: hand");
                imageDate.setVisible(false);
                delete.setVisible(false);
                share.setVisible(false);
                download.setVisible(false);
                viewImg.setVisible(false);
            });


            fone.getChildren().addAll(imageDate);
            share.setStyle("-fx-cursor: hand");
            delete.setStyle("-fx-cursor: hand");
            download.setStyle("-fx-cursor: hand");
            cellContent.getChildren().addAll(imageView, progressBar,
                    percent, checkBox, viewImg, fone, share, download, delete);

            if (firstTime && mainStageController.flowPane.getChildren() != null && mainStageController.flowPane.getChildren().size() > 0) {

                mainStageController.flowPane.getChildren().remove(0, mainStageController.flowPane.getChildren().size());
            }
            mainStageController.flowPane.getChildren().add(cellContent);
            firstTime = false;

            fone.setOnMouseClicked(mouseEvent -> {
                SliderServiceImpl.getInstance().openSlider(pictureData.getPicName(), (
                        currentPageIndex.getValue() - 1) * 50 + mainStageController.flowPane.getChildren().indexOf(cellContent));
            });

            delete.setOnMouseClicked(mouseEvent -> {
                DeleteDialogServiceImpl.getInstance().openConfirmationDialog(pictureData.getPicName(), "cell",
                        mainStageController.flowPane.getChildren().indexOf(cellContent));
            });
            download.setOnMouseClicked(mouseEvent -> {
                downloadImage(pictureData.getPicName(), progressBar);
            });
            share.setOnMouseClicked(mouseEvent -> {
                shareImage(pictureData.getPicName());
            });
            imageView.setOnMouseClicked(mouseEvent -> {
                if (!checkBox.isVisible()) {
                    System.out.println("----");
                    SliderServiceImpl.getInstance().openSlider(pictureData.getPicName(),
                            (currentPageIndex.getValue() - 1) * 50 + mainStageController.flowPane.getChildren().indexOf(cellContent));
                } else {
                    if (checkBox.isSelected()) {
                        checkBox.setSelected(false);
                    } else {
                        checkBox.setSelected(true);
                    }
                    singleSelectOrCancelItem(checkBox, finalIndex, pictureData.getPicName());
                }
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
        selectedImage.keySet().removeIf(key -> key.equals(indexOfImageFromCell));
        mainStageController.flowPane.getChildren().remove(indexOfImageFromCell);
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
                        .build(), loadedPageNumber, "general");
            } else if (response.code() == 401) {
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
        mainStageController.selectALL_checkBox.setVisible(true);
    }

    @Override
    public void selectOrCancelItems() {
        Scene scene = mainStageController.flowPane.getScene();
        ObservableList<Node> items = mainStageController.flowPane.getChildren();
        String imageName;
        if (mainStageController.selectALL_checkBox.isSelected()) {
            for (Map.Entry<CheckBox, Integer> checkBoxIntegerEntry : checkboxes.entrySet()) {
                checkBoxIntegerEntry.getKey().setSelected(true);
                imageName = ((ImageView) scene.lookup("#" + checkBoxIntegerEntry.getValue()))
                        .getImage().impl_getUrl().split(Constant.SPLITER)[1];
                selectedImage.put(checkBoxIntegerEntry.getValue(), imageName);
            }
        } else {
            for (Map.Entry<CheckBox, Integer> checkBoxIntegerEntry : checkboxes.entrySet()) {
                checkBoxIntegerEntry.getKey().setSelected(false);
                selectedImage.clear();
            }
            cancelSelect();
        }

    }

    @Override
    public void singleSelectOrCancelItem(CheckBox checkBox, int index, String imageName) {
        if (checkBox.isSelected()) {
            selectedImage.put(index, imageName);
        } else {
            selectedImage.keySet().removeIf(key -> key.equals(index));
        }
    }

    @Override
    public void deleteSelectedImages() {
        if (selectedImage != null && selectedImage.size() > 0) {
            DeleteDialogServiceImpl.getInstance().openConfirmationDialogForBatch();
        } else {
            cancelSelect();
        }

    }

    @Override
    public void changeUserStatusAndUpdatePage() {
        mainStageController.download.setDisable(false);
        mainStageController.download.setStyle("-fx-cursor: dand;-fx-background-radius: 15; -fx-background-color: #FFFFFF;");
        mainStageController.delete.setStyle("-fx-cursor: hand; -fx-background-radius: 15; -fx-background-color: #FFFFFF;");
        mainStageController.deleteTxt.setTextFill(Paint.valueOf("#000"));
        mainStageController.isShowCheckBox.set(true);
        mainStageController.selectALL_checkBox.setSelected(false);
        mainStageController.selectALL_checkBox.setVisible(false);
        ApiConnection.getInstance().updateImagesStatusBatch(sample.dataTransferObject.request.
                ImageData.builder()
                .page(this.currentPageIndex.get())
                .imageStatus(false)
                .picNames(this.selectedImage.values())
                .build());
    }


    @Override
    public void cancelSelect() {
        mainStageController.selectALL_checkBox.setVisible(false);
        mainStageController.deleteTxt.setTextFill(Paint.valueOf("#000"));
        mainStageController.downloadTxt.setTextFill(Paint.valueOf("#000"));
        mainStageController.delete.setStyle("-fx-cursor: hand;-fx-background-radius: 15; -fx-background-color: #FFFFFF;");
        mainStageController.download.setStyle("-fx-cursor: hand;-fx-background-radius: 15; -fx-background-color: #FFFFFF;");
        mainStageController.delete.setDisable(false);
        mainStageController.download.setDisable(false);
        mainStageController.isShowCheckBox.set(true);
        selectedImage.clear();
        removeAllCheckobox();

    }

    @Override
    public void removeAllCheckobox() {
        for (CheckBox checkBox : checkboxes.keySet()) {
            checkBox.setSelected(false);
            checkBox.setVisible(false);
        }

    }

    @Override
    public void downloadSelectedImages() {
        Scene scene = mainStageController.header.getScene();
        if (selectedImage != null && selectedImage.size() > 0) {
            for (Map.Entry<Integer, String> entry : selectedImage.entrySet()) {
                downloadImage(entry.getValue(), (ProgressBar) scene.lookup("#progressBar_" + entry.getKey()));
            }
            cancelSelect();
        } else {
            cancelSelect();
        }
    }

    @Override
    public void shareImage(String picName) {
        Stage shareDialogStage = new Stage();
        FXMLLoader fxmlLoader = null;
        Parent root = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("/view/dialog/shareDialog.fxml"));
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);
        shareDialogStage.setScene(scene);
        shareDialogStage.initStyle(StageStyle.UNDECORATED);
        shareDialogStage.initModality(Modality.APPLICATION_MODAL);
        shareDialogStage.setResizable(false);
        TextField sharedImageLink = (TextField) scene.lookup("#sharedImageLink");
        sharedImageLink.setText(Constant.SERVER_ADDRESS + Constant.IMAGE_URI + picName);
        shareDialogStage.show();
    }

    @Override
    public StackPane getMainPane() {
        return mainStageController.mainPane;
    }

    @Override
    public MainStageController getMainStageController() {
        return this.mainStageController;
    }

    @Override
    public void filterClick() {
        if (filterAction.get().equals("FIRST_SHOW")) {
            mainStageController.filterPane.setVisible(true);
            mainStageController.filterImage.setImage(new Image(getClass().getResourceAsStream("/image/controls.png")));
            mainStageController.filterButton.setStyle("-fx-cursor: hand; -fx-background-color: #388E3C; -fx-background-radius: 5");
            mainStageController.filterBtn.setStyle("-fx-cursor: hand; -fx-background-color: #388E3C");
            mainStageController.filterBtn.setTextFill(Paint.valueOf("fff"));
            filterAction.setValue("SAVE_FILTER");
        } else if(filterAction.get().equals("NEW_FILTER")) {
            mainStageController.filterPane.setVisible(false);
            mainStageController.filterImage.setImage(new Image(getClass().getResourceAsStream("/image/controlsBlack.png")));
            mainStageController.filterButton.setStyle("-fx-cursor: hand; -fx-background-color: #FFF; -fx-background-radius: 5");
            mainStageController.filterBtn.setStyle("-fx-cursor: hand; -fx-background-color: #FFF");
            mainStageController.filterBtn.setTextFill(Paint.valueOf("000"));
            String month = (String) mainStageController.filterMonth.getValue();
            String requestMonth ="ALL";
            switch (month) {
                case FilterMonth.ALL:
                    requestMonth = FilterMonth.ALL;
                    break;
                case FilterMonth.JAN:
                    requestMonth = "01";
                    break;
                case FilterMonth.FEB:
                    requestMonth = "02";
                    break;
                case FilterMonth.MAR:
                    requestMonth = "03";
                    break;
                case FilterMonth.APR:
                    requestMonth = "04";
                    break;
                case FilterMonth.MAY:
                    requestMonth = "05";
                    break;
                case FilterMonth.JUNE:
                    requestMonth = "06";
                    break;
                case FilterMonth.JULY:
                    requestMonth = "07";
                    break;
                case FilterMonth.AUG:
                    requestMonth = "08";
                    break;
                case FilterMonth.SEPT:
                    requestMonth = "09";
                    break;
                case FilterMonth.OCT:
                    requestMonth = "10";
                    break;
                case FilterMonth.NOV:
                    requestMonth = "11";
                    break;
                case FilterMonth.DEC:
                    requestMonth = "12";
                    break;

            }
            filterAction.setValue("SAVE_FILTER");
            String requestYear = (String) mainStageController.filterYear.getValue();
            ApiConnection.getInstance().getFilterData(1,requestYear,requestMonth);
        }else if(filterAction.get().equals("SAVE_FILTER")){
            mainStageController.filterPane.setVisible(false);
            mainStageController.filterImage.setImage(new Image(getClass().getResourceAsStream("/image/controlsBlack.png")));
            mainStageController.filterButton.setStyle("-fx-cursor: hand; -fx-background-color: #FFF; -fx-background-radius: 5");
            mainStageController.filterBtn.setStyle("-fx-cursor: hand; -fx-background-color: #FFF");
            mainStageController.filterBtn.setTextFill(Paint.valueOf("000"));
            filterAction.setValue("FIRST_SHOW");
        }
    }

}
