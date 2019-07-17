package sample.service.serviceImpl;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.Constant;
import sample.Main;
import sample.connection.ApiConnection;
import sample.controller.MainStageController;
import sample.controller.SlideController;
import sample.service.SliderService;
import sample.util.Helper;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class SliderServiceImpl implements SliderService {
    private static SliderServiceImpl sliderService = new SliderServiceImpl();
    private MainStageController mainStageController;

    private SlideController slideController;

    private SliderServiceImpl() {
    }

    public static SliderServiceImpl getInstance() {
        return sliderService;
    }

    @Override
    public void initializeSliderController(SlideController slideController) {
        this.slideController = slideController;
    }

    public BooleanProperty isIncrementFructionNumbering = new SimpleBooleanProperty(false);
//    private double menuBarWidth =  slideController.menuBar.getWidth();

    @Override
    public void openSlider(String picName, int indexOf) {
        Stage mainStage = (Stage) mainStageController.header.getScene().getWindow();
        AnchorPane slider =(AnchorPane) Main.getScreen("slider");
        slider.setPrefWidth(mainStage.getWidth());
        slider.setPrefHeight(mainStage.getHeight());

        //                                                                  entaka e popoxman
        slideController.menuBar.setLayoutX((mainStage.getWidth()-slideController.menuBar.getPrefWidth())/2);
        slideController.rightContent.setLayoutX(mainStage.getWidth()-60);
        slideController.scrollPane.setPrefWidth(mainStage.getWidth()-120);
        slideController.shownImage_container.setPrefWidth(mainStage.getWidth()-210);
        slideController.shownImage.setFitWidth(mainStage.getWidth()-120);
        slideController.previousButton.setLayoutY(mainStage.getHeight()/2-30);
        slideController.nextButton.setLayoutY(mainStage.getHeight()/2-30);

        slideController.scrollPane.setPrefHeight(mainStage.getHeight()-120);
        slideController.shownImage_container.setPrefHeight(mainStage.getHeight()-105);
        slideController.shownImage.setFitHeight(mainStage.getHeight()-120);


        mainStageController.mainPane.getChildren().add(1,slider);
        if (indexOf == 0) {
            slideController.previousButton.setVisible(false);
        } else {
            slideController.previousButton.setVisible(true);
        }
        slideController.sliderProgressBar.setVisible(false);
        slideController.nextButton.setVisible(true);
        Image image = new Image(Constant.SERVER_ADDRESS + Constant.IMAGE_URI + picName);
        slideController.shownImageName.setText(picName);
        slideController.shownImage.setImage(image);
        slideController.fraction.setText(indexOf+1 + "/" + MainStageServiceImpl.myImageCount.get());
        slideController.sliderContent.setVisible(true);
        slideController.sliderPercent.setVisible(false);

        mainStage.widthProperty().addListener((observable, oldValue, newValue) -> {

            slider.setPrefWidth(newValue.doubleValue());
            slideController.menuBar.setLayoutX((newValue.doubleValue()-slideController.menuBar.getPrefWidth())/2);
            slideController.rightContent.setLayoutX(newValue.doubleValue()-60);
            slideController.scrollPane.setPrefWidth(newValue.doubleValue()-120);
            slideController.shownImage_container.setPrefWidth(newValue.doubleValue()-210);
            slideController.shownImage.setFitWidth(newValue.doubleValue()-120);
        });
        mainStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            slider.setPrefHeight(newValue.doubleValue());
            slideController.previousButton.setLayoutY(newValue.doubleValue()/2-30);
            slideController.nextButton.setLayoutY(newValue.doubleValue()/2-30);
//            slideController.nextLabel.setLayoutY(newValue.doubleValue()/2);
//            slideController.previousLabel.setLayoutY(newValue.doubleValue()/2);
            slideController.scrollPane.setPrefHeight(newValue.doubleValue()-120);
            slideController.shownImage_container.setPrefHeight(newValue.doubleValue()-105);
            slideController.shownImage.setFitHeight(newValue.doubleValue()-120);


        });
//        slideController.sliderPercent.widthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//
//            }
//        });
    }

    @Override
    public void openNextImage() {
        Label shownImageName = slideController.shownImageName;
        ApiConnection.getInstance().getNextPreviousImage(Constant.NEXT_IMAGE, shownImageName.getText(),"next");
    }
    @Override
    public void openPreviousImage() {
        Label shownImageName = slideController.shownImageName;
        ApiConnection.getInstance().getNextPreviousImage(Constant.PREVIOUS_IMAGE, shownImageName.getText(),"previous");

    }


    @Override
    public void showpPreviousNextImageData(Response response,String previous_next) {
        if (response.code() == 200) {
            JSONObject responseJson = null;
            try {
                responseJson = new JSONObject(new String(response.body().bytes()));
            } catch (IOException e) {
                // TODO something
                System.out.println("json pars error");
            }
            JSONArray picturesData = responseJson.getJSONArray("picturesData");
            slideController.previousButton.setVisible(true);
            slideController.nextButton.setVisible(true);
            if (picturesData != null && picturesData.length() > 0) {
                JSONObject imageDataJson = picturesData.getJSONObject(0);
                String picName = imageDataJson.getString("picName");
                Image image = new Image(Constant.SERVER_ADDRESS + Constant.IMAGE_URI + picName);
                slideController.shownImage.setImage(image);
                slideController.shownImageName.setText(picName);
                String[] fractionData = slideController.fraction.getText().split("/");
                if(previous_next.equals("next")){
                    slideController.fraction.setText(Integer.parseInt(fractionData[0])+1+"/"+fractionData[1]);
                    //երբ նկար է ջնջվել է պետք է համարը չփոխվի
                    if(isIncrementFructionNumbering.get()){
                        slideController.fraction.setText(Integer.parseInt(fractionData[0])+"/"+fractionData[1]);
                    }
                }else {
                    slideController.fraction.setText(Integer.parseInt(fractionData[0])-1+"/"+fractionData[1]);
                }
            } else {
                if(previous_next.equals("next")) {
                    slideController.nextButton.setVisible(false);
                    slideController.previousButton.setVisible(true);
                }else {
                    slideController.nextButton.setVisible(true);
                    slideController.previousButton.setVisible(false);
                }
            }
        } else {
            // TODO something
        }
    }

    @Override
    public boolean isShowedLastImage(){
        String[] fructionData = slideController.fraction.getText().split("/");
        return fructionData[0].equals(fructionData[1]);
    }

    @Override
    public void closeSlidePage() {
        slideController.sliderContent.setVisible(false);
    }


    @Override
    public void downloadImage() {
        slideController.sliderPercent.setText("0 %");
        slideController.sliderPercent.setVisible(true);
        slideController.sliderProgressBar.setProgress(0);
        slideController.sliderProgressBar.setVisible(true);
        try {
            URL url = new URL(Constant.SERVER_ADDRESS + Constant.IMAGE_URI + slideController.shownImageName.getText());
            URLConnection urlConnection = url.openConnection();
            double pictureSize = new Double(urlConnection.getHeaderField("pictureSize") + "D");
            urlConnection.connect();
            String dirPath = Helper.getInstance().decideDirectionPath();
            InputStream inputStream = urlConnection.getInputStream();
            int bufferSize = 256;
            byte[] buffer = new byte[bufferSize];

            BufferedOutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(dirPath + slideController.shownImageName.getText()));
            int len = 0;
            double downloadedSize = 0;
            while ((len = inputStream.read(buffer, 0, bufferSize)) > -1) {
                outputStream.write(buffer, 0, len);
                downloadedSize += slideController.sliderProgressBar.getProgress() + len;
                double v = (downloadedSize) / pictureSize;
                slideController.sliderProgressBar.setProgress(v);
                slideController.sliderPercent.setText((int) v*100+"%");
            }
            outputStream.flush();
            outputStream.close();
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));
            pauseTransition.play();
            pauseTransition.setOnFinished(event -> {
                Platform.runLater(() -> {
                    slideController.sliderProgressBar.setVisible(false);
                    slideController.sliderPercent.setVisible(false);
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateFruction(int numerator) {
        slideController.fraction.setText(numerator+"/"+slideController.fraction.getText().split("/")[1]);
    }

    @Override
    public void initializeMainStageController(MainStageController mainStageController) {
        this.mainStageController = mainStageController;
    }
}
