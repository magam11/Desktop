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
    public void start(Stage s) throws Exception {
        s.setTitle("creating progressIndicator");

        ProgressIndicator pb = new ProgressIndicator();
        pb.setStyle("-fx-progress-color: red;");
        StackPane r = new StackPane();
        r.getChildren().add(pb);
        Scene sc = new Scene(r, 200, 200);
        s.setScene(sc);
        s.show();
    }

    public static void main(String args[]) {
        launch(args);
    }
} 