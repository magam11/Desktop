package sample.util;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.HashMap;

public class Main extends Application {

    private static  HashMap<String, Node> screens = new HashMap<>();

    @Override
    public void start(Stage primaryStage) throws Exception{

        StackPane anchorPane = new StackPane();
        anchorPane.setPrefWidth(500);
        anchorPane.setPrefHeight(500);
        ProgressIndicator progressBar = new ProgressIndicator();
        anchorPane.getChildren().add(progressBar);

        Scene scene = new Scene(anchorPane);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(we->{
            System.exit(0);
        });
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


}
