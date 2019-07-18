package sample.service.serviceImpl;

import sample.controller.LoaderController;

import java.util.Random;

public class LoaderServiceImpl {

    private LoaderController loaderController;

    public void initializeLoaderController(LoaderController loaderController){
        this.loaderController = loaderController;
    }

    private LoaderServiceImpl(){}
    private final static LoaderServiceImpl instance = new LoaderServiceImpl();

    public static LoaderServiceImpl getInstance(){
        return instance;
    }

    public void setPercentToProgressBar(){
        System.out.println("ashxatec");

        loaderController.indicator.setProgress(new Random().nextDouble());
    }


}
