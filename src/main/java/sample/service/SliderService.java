package sample.service;

import okhttp3.Response;
import sample.controller.SlideController;

public interface SliderService {
    void initializeSliderController(SlideController slideController);

    void openSlider(String picName, int indexOf);

    void openNextImage();

    void showpPreviousNextImageData(Response response,String previous_next);

    void openPreviousImage();

}
