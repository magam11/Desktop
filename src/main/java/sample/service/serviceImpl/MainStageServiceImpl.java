package sample.service.serviceImpl;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import sample.Constant;
import sample.Size;
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
import java.util.List;

public class MainStageServiceImpl implements MainStageService {
    private static MainStageService mainStageService = new MainStageServiceImpl();
    public static IntegerProperty myImageCount;

    private DoubleProperty phoneNumberLenth;

    private MainStageServiceImpl() {
    }

    public static MainStageService getInstance() {
        return mainStageService;
    }

    //local variables
    private MainStageController mainStageController;


    @Override
    public void responsivWidth(double stageWith) {
        mainStageController.imageCountInto.setLayoutX(28.0);
        mainStageController.memoryProgressBar.setLayoutX(81);
        mainStageController.memoryHint.setLayoutX(28.0);
//        +++layoutX="14.0"
        mainStageController.floxPane.setPrefWidth(stageWith-10);
        mainStageController.slideController.responsiveWidth(stageWith);
        if (stageWith <= 1000.0) {
            mainStageController.memoryProgressBar.setPrefWidth(Size.WIDTH_COEFFICENT_FOR_PROGRESS_BAR_WIDTH * stageWith);
            mainStageController.fraction.setLayoutX(Size.WIDTH_COEFFICENT_FOR_PROGRESS_BAR_WIDTH * stageWith + mainStageController.memoryProgressBar.getLayoutX() + 7);
        }
        mainStageController.logoCamerImageVeiw.setLayoutX(Size.WIDTH_COEFFICENT_FOR_CAMERA_LOGO_CAMERA * stageWith);
        mainStageController.share.setLayoutX(stageWith - 299);
        mainStageController.delete.setLayoutX(stageWith - 202);
        mainStageController.download.setLayoutX(stageWith - 105);
        mainStageController.logOut_btn.setLayoutX(stageWith-130);
        mainStageController.recycle_btn.setLayoutX(stageWith-227);
        mainStageController.scrollPane.setPrefWidth(stageWith-40);
        mainStageController.cell_containerAnchorPane.setPrefWidth(stageWith);
        mainStageController.cell_containerAnchorPane.setLayoutX(20);
        mainStageController.filterPane.setPrefWidth(stageWith);
        mainStageController.pageNumbersPane.setPrefWidth(stageWith);
        mainStageController.headerRow1.setPrefWidth(stageWith);
        mainStageController.phoneNumber.setLayoutX(mainStageController.recycle_btn.getLayoutX()-phoneNumberLenth.getValue()-8);
    }

    @Override
    public void initializeMainStageController(MainStageController mainStageController) {
        this.mainStageController = mainStageController;
    }


    @Override
    public void loadMainStageData(BaseUserData baseUserData) {
        int totoalPageCount = baseUserData.getTotoalPageCount();
        double celnterX = mainStageController.pageNumbersPane.getWidth()/2;
//        double layaoutXOfFirstNumber = mainStageController.pageNumbersPane.getWidth()/2;
        double flayaoutXOfFirstNumber = celnterX -(totoalPageCount-1)*10;
        System.out.println("--------- pagecount "+totoalPageCount);
        for (int i = 0; i < totoalPageCount; i++) {
            Label label = new Label(String.valueOf(i+1));
            label.setLayoutX(flayaoutXOfFirstNumber +i*10);
            label.setLayoutY(mainStageController.pageNumbersPane.getLayoutY()+mainStageController.pageNumbersPane.getHeight()/2);
//            label.setFont(Font.font());
            mainStageController.pageNumbersPane.getChildren().add(label);


        }
        mainStageController.fraction.setText(baseUserData.getFruction());
        myImageCount = new SimpleIntegerProperty(Integer.parseInt(baseUserData.getFruction().split("/")[0]));
        int imagesConunt = baseUserData.getPicturesData() == null ? 0 : baseUserData.getPicturesData().size();
        mainStageController.imageCountInto.setText("Storage (" + imagesConunt + ")");
        mainStageController.phoneNumber.setText(baseUserData.getPhoneNumber());
        phoneNumberLenth = new SimpleDoubleProperty(mainStageController.phoneNumber.getLayoutBounds().getWidth());
        mainStageController.phoneNumber.setLayoutX(mainStageController.recycle_btn.getLayoutX()-phoneNumberLenth.getValue()-8);
        mainStageController.memoryProgressBar.setProgress(Double.valueOf(imagesConunt) / 500.0);
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
        ((Stage)mainStageController.headerRow1.getScene().getWindow()).close();
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Log in");
        javafx.scene.image.Image logo = new Image(this.getClass().getResourceAsStream("/image/logo.png"));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(logo);


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(we->{
            System.exit(0);
        });
        primaryStage.show();
    }


    @Override
    public void drawImagesInMainStage(List<ImageData> picturesData, String currentToken) {
        System.out.println("length "+picturesData.size());
        for (ImageData pictureData : picturesData) {

            AnchorPane cellContent = new AnchorPane();
            cellContent.setPrefWidth(200.0);
            cellContent.setPrefHeight(177.0);
            FlowPane.setMargin(cellContent, new Insets(5.0, 5.0, 5.0, 5.0));
            cellContent.setId(pictureData.getPicName());
            cellContent.setStyle("-fx-cursor: hand");

            Image image = new Image(Constant.SERVER_ADDRESS + Constant.IMAGE_URI + pictureData.getPicName());
            ImageView imageView = new ImageView(image);
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


            cellContent.getChildren().addAll(imageView, imageDate, delete, share, progressBar, percent, download);
            mainStageController.floxPane.getChildren().add(cellContent);
            delete.setOnMouseClicked(mouseEvent -> {
                DeleteDialogServiceImpl.getInstance().openConfirpationDialog(pictureData.getPicName(), "cell", mainStageController.floxPane.getChildren().indexOf(cellContent));
            });
            download.setOnMouseClicked(mouseEvent -> {
                downloadImage(pictureData.getPicName(), progressBar);
            });
            imageView.setOnMouseClicked(mouseEvent -> {
                SliderServiceImpl.getInstance().openSlider(pictureData.getPicName(), mainStageController.floxPane.getChildren().indexOf(cellContent));
            });

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
        mainStageController.floxPane.getChildren().remove(indexOfImageFromCell);
    }
}
