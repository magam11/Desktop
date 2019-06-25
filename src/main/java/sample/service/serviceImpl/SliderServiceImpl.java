package sample.service.serviceImpl;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.Constant;
import sample.connection.ApiConnection;
import sample.controller.SlideController;
import sample.service.SliderService;

import java.io.IOException;

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

    @Override
    public void openSlider(String picName, int indexOf) {
        if (indexOf == 0) {
            slideController.previousLabel.setVisible(false);
        } else {
            slideController.previousLabel.setVisible(true);
        }
        slideController.nextLabel.setVisible(true);
        Image image = new Image(Constant.SERVER_ADDRESS + Constant.IMAGE_URI + picName);
        slideController.shownImageName.setText(picName);
        slideController.shownImage.setImage(image);
        slideController.fraction.setText(indexOf+1 + "/" + MainStageServiceImpl.myImageCount.get());
        slideController.sliderContent.setVisible(true);
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
                System.out.println("dateeee "+imageDataJson.getString("createdAt"));
                String picName = imageDataJson.getString("picName");
                Image image = new Image(Constant.SERVER_ADDRESS + Constant.IMAGE_URI + picName);
                slideController.shownImage.setImage(image);
                slideController.shownImageName.setText(picName);
                String[] fractionData = slideController.fraction.getText().split("/");
                if(previous_next.equals("next")){
                    slideController.fraction.setText(Integer.parseInt(fractionData[0])+1+"/"+fractionData[1]);
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


}
