//package sample.controller;
//
//import javafx.fxml.FXML;
//import javafx.geometry.Insets;
//import javafx.scene.control.ScrollPane;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.FlowPane;
//
//public class CellController {
//    @FXML
//    public AnchorPane cellContent;
//    @FXML
//    public ScrollPane scrollPane;
//    @FXML
//    public FlowPane flowPane;
//    @FXML
//    public AnchorPane filterPane;
//    @FXML
//    public AnchorPane pageNumbersPane;
//    private MainStageController mainStageController;
//
//    public void initializeMainStageController(MainStageController mainStageController){
//        this.mainStageController = mainStageController;
//    }
//
//    //services
//    CellService cellService = CellServiceImpl.getInstance();
//
//
//    public void initialize(){
//        cellService.initilizeCellController(this);
//        flowPane.setPadding(new Insets(50,0,0,0));
//        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//    }
//
//    public void responsiveWidthForCe(double stageWith) {
//        flowPane.setPrefWidth(stageWith-10);
//
//    }
//
//    @FXML
//    public void openSlider(MouseEvent mouseEvent) {
//        mainStageController.slideController.sliderContent.setVisible(true);
//    }
//
//}
