package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.*;

public class progressi extends Application {

    static double ii = 0;

    // launch the application 
    public void start(Stage stage) throws Exception {
        stage.setTitle("creating progressIndicator");




        BorderPane anchorPane = new BorderPane();
//        anchorPane.setStyle("-fx-alignment: center");



        StackPane r = new StackPane();


//        r.setStyle("-fx-alignment: center");



        ProgressIndicator pb = new ProgressIndicator();
        pb.setStyle("-fx-progress-color: #388e3c;");
        pb.setMaxHeight(300);
        pb.setMaxWidth(300);
        anchorPane.setCenter(pb);
        r.getChildren().add(anchorPane);
        Scene sc = new Scene(r, 200, 200);

        stage.setScene(sc);
        stage.show();
    }

    public static void main(String args[]) {
        launch(args);
    }
} 