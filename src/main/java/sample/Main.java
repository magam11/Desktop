package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        primaryStage.setTitle("Log in");
        javafx.scene.image.Image logo = new Image(this.getClass().getResourceAsStream("/image/logo.png"));
        primaryStage.getIcons().add(logo);

        primaryStage.setResizable(false);
        Scene scene = new Scene(root);
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
