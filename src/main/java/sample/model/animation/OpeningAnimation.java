package sample.model.animation;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.AnchorPane;
import sample.service.serviceImpl.MainStageServiceImpl;

public class OpeningAnimation extends AnimationTimer {

    private  static final OpeningAnimation instance = new OpeningAnimation();

    private OpeningAnimation(){}


    public static OpeningAnimation getInstance() {
        return instance;
    }

    private DoubleProperty opacity = MainStageServiceImpl.getInstance().getPaginationPageOpacity();
    private AnchorPane pageNumbersPane = MainStageServiceImpl.getInstance().getNumbersPane();

    boolean firstTime = true;
    @Override
    public void handle(long now) {

        doHandle();
    }

    private void doHandle() {
        if(opacity.getValue() <1){
            pageNumbersPane.setVisible(true);
            opacity.setValue(opacity.get() + 0.02);
        }
        if (opacity.getValue() >=1) {
             ClosingAnimation.getInstance().start();
//            opacity.setValue(opacity.get() - 0.0055);
            stop();
            firstTime = false;
        }
    }
}