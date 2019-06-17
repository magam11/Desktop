package sample.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class CellController {

    @FXML
    public ScrollPane scrollPane;
    @FXML
    public FlowPane floxPane;
    private MainStageController mainStageController;

    public void initializeMainStageController(MainStageController mainStageController){
        this.mainStageController = mainStageController;
    }



    public void initialize(){
        floxPane.setPadding(new Insets(50,0,0,0));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    public void responsiveWidth(double stageWith) {
        floxPane.setPrefWidth(stageWith);

    }

    @FXML
    public void openSlider(MouseEvent mouseEvent) {
        mainStageController.phoneController.sclollpane.setVisible(true);
//        mainStageController.sliderController.sliderContainer.setVisible(true);


    }


//    public void responsiveWidth(double stageWith) {
//        int size = gridPane.getColumnConstraints().size();
//        if (((stageWith - (size) * 5) / 174.0) >= size + 1) {
//            int oldColumnSize = gridPane.getColumnConstraints().size();
//            ColumnConstraints columnConstraints = new ColumnConstraints(100.0);
//            columnConstraints.setHgrow(Priority.SOMETIMES);
//            columnConstraints.setMaxWidth(1.7976931348623157E308);
//            gridPane.getColumnConstraints().add(columnConstraints);
//            ObservableList<RowConstraints> rowConstraints = gridPane.getRowConstraints();
//            int rowSize = rowConstraints.size();
//            int columnSize = gridPane.getColumnConstraints().size();
//
//            for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
//
//                for (int coliumnIndex = oldColumnSize; coliumnIndex < columnSize; coliumnIndex++) {
//                    System.out.println("oldColumnSize "+oldColumnSize);
//                    System.out.println("columnSize "+columnSize);
//                    System.out.println("size "+size);
//                    //avelacnel nor column
////                    AnchorPane anchorPane = new AnchorPane();
////                    anchorPane.setPrefHeight(126.0);
////                    anchorPane.setPrefWidth(174.0);
////                    anchorPane.setStyle("-fx-background-color: #1234");
////                    GridPane.setMargin(anchorPane, new Insets(5, 5, 5, 5));
////                    anchorPane.setMaxWidth(GridPane.USE_COMPUTED_SIZE);
////                    anchorPane.setMaxHeight(GridPane.USE_COMPUTED_SIZE);
////                    GridPane.setRowIndex(anchorPane,number);
////                    GridPane.setColumnIndex(anchorPane,number);
////                    gridPane.add(anchorPane, size, rowIndex);
//                    AnchorPane childOfsecondRow = (AnchorPane) getNodeByRowColumnIndex(rowIndex + 1, coliumnIndex-oldColumnSize, gridPane);
//                    GridPane.setColumnIndex(childOfsecondRow,size);
//                    GridPane.setRowIndex(childOfsecondRow,rowIndex);
//
//
////                    gridPane.getChildren().remove(childOfsecondRow);
//
//                }
//                rowConstraints = gridPane.getRowConstraints();
////                rowSize = rowConstraints.size();
//                rowSize = 1;
//            }
//        }
//        gridPane.setPrefWidth(stageWith);
//    }
//
//
//    private Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
//        Node result = null;
//        ObservableList<Node> childrens = gridPane.getChildren();
//        for (Node node : childrens) {
//            Integer rowIndex = GridPane.getRowIndex(node);
//            if (GridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
//                result = node;
//                break;
//            }
//        }
//
//        return result;
//    }
//
//    public void responsiveWidth(double stageWith) {
//
//        int size = gridPane.getColumnConstraints().size();
//        int oldColumnSize = gridPane.getColumnConstraints().size();
//
//        if (((stageWith - (size) * 5) / 174.0) >= size + 1) {
//            System.out.println("mecanum e");
//            addColumn(oldColumnSize, size);
//        }
//        if (((stageWith - (size) * 5) / 174.0) < size) {
////            System.out.println("poqranum e");
//            deleteColumn(oldColumnSize);
//
//        }
//        gridPane.setPrefWidth(stageWith);
//    }
//
//
//
//
//    private void deleteColumn(int oldColumnSize) {
//        ObservableList<RowConstraints> rowConstraints = gridPane.getRowConstraints();
//
//        int rowSize = rowConstraints.size();
////        System.out.println("row size "+rowSize);
//        for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
//            for (int columnIndex = 0; columnIndex < oldColumnSize; columnIndex++) {
//                if(columnIndex<=oldColumnSize && columnIndex>oldColumnSize-rowIndex){
//
//                    Node node = getNodeByRowColumnIndex(rowIndex, oldColumnSize-columnIndex, gridPane);
//                    if (node != null) {
//                        System.out.println("ashxatec ify");
//                        GridPane.setRowIndex(node,rowIndex+1);
//                        GridPane.setColumnIndex(node,oldColumnSize-columnIndex-1);
//                    }
//                }
//
//
//            }
//        }
//
//
//    }
//
//    private void addColumn(int oldColumnSize, int size) {
//        ColumnConstraints columnConstraints = new ColumnConstraints(100.0);
//        columnConstraints.setHgrow(Priority.SOMETIMES);
//        columnConstraints.setMaxWidth(1.7976931348623157E308);
//        gridPane.getColumnConstraints().add(columnConstraints);
//        ObservableList<RowConstraints> rowConstraints = gridPane.getRowConstraints();
//        int rowSize = rowConstraints.size();
//        int newColumnSize = gridPane.getColumnConstraints().size();
//        for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
//            for (int columnIndex = 0; columnIndex < newColumnSize; columnIndex++) {
//                Node node = getNodeByRowColumnIndex(rowIndex + 1, oldColumnSize - columnIndex - 1, gridPane);
//                if (node != null) {
//                    AnchorPane childOfsecondRow = (AnchorPane) getNodeByRowColumnIndex(rowIndex + 1, oldColumnSize - columnIndex - 1, gridPane);
//                    if (rowIndex + 1 > GridPane.getColumnIndex(childOfsecondRow)) {
//                        GridPane.setColumnIndex(childOfsecondRow, size - GridPane.getColumnIndex(childOfsecondRow));
//                        GridPane.setRowIndex(childOfsecondRow, rowIndex);
//                    } else {
//                        GridPane.setColumnIndex(childOfsecondRow, GridPane.getColumnIndex(childOfsecondRow) - rowIndex - 1);
//                    }
//                }
//            }
//            RowConstraints rowConstraintsN = gridPane.getRowConstraints().get(gridPane.getRowConstraints().size() - 1);
//            if(rowConstraintsN==null || rowConstraintsN.getPrefHeight()==0){
//                System.out.println("jnjec row");
//                gridPane.getRowConstraints().remove(rowConstraintsN);
//            }
//        }
//    }


}
