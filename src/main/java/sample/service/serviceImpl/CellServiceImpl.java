package sample.service.serviceImpl;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import sample.Constant;
import sample.controller.CellController;
import sample.dataTransferObject.response.ImageData;
import sample.service.CellService;
import sample.service.DeleteDialogService;
import sample.util.Helper;


import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CellServiceImpl implements CellService {
    private static CellService instance = new CellServiceImpl();

    private CellServiceImpl() {
    }

    public static CellService getInstance() {
        return instance;
    }

    private CellController cellController;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:MM");

    @Override
    public void initilizeCellController(CellController cellController) {
        this.cellController = cellController;
    }

    @Override
    public void drawImagesInMainStage(List<ImageData> picturesData, String currentToken) {
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
            cellController.floxPane.getChildren().add(cellContent);
            delete.setOnMouseClicked(mouseEvent->{
                DeleteDialogServiceImpl.getInstance().openConfirpationDialog(pictureData.getPicName(),"cell",cellController.floxPane.getChildren().indexOf(cellContent));
            });
            download.setOnMouseClicked(mouseEvent -> {
                downloadImage(pictureData.getPicName(),progressBar);
            });

        }
    }


    /**
     * Download image from server
     *
     * @param picName
     *
     */
    @Override
    public void downloadImage(String picName, ProgressBar progressBar) {
        progressBar.setProgress(0);
        progressBar.setVisible(true);

        try {
            URL url = new URL(Constant.SERVER_ADDRESS+Constant.IMAGE_URI+picName);
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
                Platform.runLater(()->{
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
        cellController.floxPane.getChildren().remove(indexOfImageFromCell);
    }
}
