package sample.service.serviceImpl;

import sample.Size;
import sample.controller.MainStageController;
import sample.service.MainStageService;

public class MainStageServiceImpl implements MainStageService {
    private static MainStageService mainStageService = new MainStageServiceImpl();

    private MainStageServiceImpl() {
    }

    public static MainStageService getInstance() {
        return mainStageService;
    }

    //local variables
    private MainStageController mainStageController;


    @Override
    public void responsivWidth(double stageWith) {
        mainStageController.cellController.responsiveWidth(stageWith);
        mainStageController.slideController.responsiveWidth(stageWith);
        if (stageWith <= 1000.0) {
            mainStageController.memoryProgressBar.setPrefWidth(Size.WIDTH_COEFFICENT_FOR_PROGRESS_BAR_WIDTH * stageWith);
            mainStageController.fraction.setLayoutX(Size.WIDTH_COEFFICENT_FOR_PROGRESS_BAR_WIDTH * stageWith + mainStageController.memoryProgressBar.getLayoutX() + 7);
        }
        mainStageController.logoCamerImageVeiw.setLayoutX(Size.WIDTH_COEFFICENT_FOR_CAMERA_LOGO_CAMERA * stageWith);
        mainStageController.phoneNumber.setLayoutX(stageWith - 210);
        mainStageController.logOutLabel.setLayoutX(stageWith - 73);
        mainStageController.share.setLayoutX(stageWith - 299);
        mainStageController.delete.setLayoutX(stageWith - 202);
        mainStageController.download.setLayoutX(stageWith - 105);
        mainStageController.cellController.scrollPane.setPrefWidth(stageWith);
    }

    @Override
    public void initializeMainStageController(MainStageController mainStageController) {
        this.mainStageController = mainStageController;
    }
}
