package sample.model.animation;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.AnchorPane;
import sample.service.serviceImpl.MainStageServiceImpl;

public class ClosingAnimation extends AnimationTimer {


    private static final ClosingAnimation instance = new ClosingAnimation();

    private ClosingAnimation(){}

    public static ClosingAnimation getInstance(){
        return instance;
    }

    private DoubleProperty opacity = MainStageServiceImpl.getInstance().getPaginationPageOpacity();
    private AnchorPane pageNumbersPane = MainStageServiceImpl.getInstance().getNumbersPane();

    @Override
    public void handle(long now) {
        doHandle();
    }

    private void doHandle() {
        opacity.setValue(opacity.get() - 0.005);
        if (opacity.getValue() <= 0) {
            pageNumbersPane.setVisible(false);
            stop();
        }
    }
}