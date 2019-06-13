package sample.service;

public class SliderService {
    private static SliderService sliderService = new SliderService();

    private SliderService(){}
    public static SliderService getInstance(){
        return sliderService;
    }
}
