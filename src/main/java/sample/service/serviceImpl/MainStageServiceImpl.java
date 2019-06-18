package sample.service.serviceImpl;

import sample.service.MainStageService;

public class MainStageServiceImpl implements MainStageService {
    private static MainStageService mainStageService = new MainStageServiceImpl();

    private MainStageServiceImpl(){}

    public static MainStageService getInstance(){
        return mainStageService;
    }


}
