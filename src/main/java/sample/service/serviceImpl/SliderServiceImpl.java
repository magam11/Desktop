package sample.service.serviceImpl;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.util.Duration;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.Constant;
import sample.connection.ApiConnection;
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


    @Override
    public void openSlider(String picName, int indexOf) {
        if (indexOf == 0) {
            slideController.previousLabel.setVisible(false);
        } else {
            slideController.previousLabel.setVisible(true);
        }
        slideController.sliderProgressBar.setVisible(false);
        slideController.nextLabel.setVisible(true);
        Image image = new Image(Constant.SERVER_ADDRESS + Constant.IMAGE_URI + picName);
        slideController.shownImageName.setText(picName);
        slideController.shownImage.setImage(image);
        slideController.fraction.setText(indexOf+1 + "/" + MainStageServiceImpl.myImageCount.get());
        slideController.sliderContent.setVisible(true);
        slideController.sliderPercent.setVisible(false);
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
            slideController.previousLabel.setVisible(true);
            slideController.nextLabel.setVisible(true);
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
                    slideController.nextLabel.setVisible(false);
                    slideController.previousLabel.setVisible(true);
                }else {
                    slideController.nextLabel.setVisible(true);
                    slideController.previousLabel.setVisible(false);
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
}
