package sample.service.serviceImpl;

import sample.service.SliderService;

public class SliderServiceImpl implements SliderService {
    private static SliderServiceImpl sliderService = new SliderServiceImpl();

    private SliderServiceImpl(){}
    public static SliderServiceImpl getInstance(){
        return sliderService;
    }


}
