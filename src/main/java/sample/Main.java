package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.HashMap;

public class Main extends Application {

    private static  HashMap<String, Node> screens = new HashMap<>();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        primaryStage.setTitle("Log in");
        javafx.scene.image.Image logo = new Image(this.getClass().getResourceAsStream("/image/logo.png"));
        primaryStage.getIcons().add(logo);
        loadScreen("slider","/view/part/slid.fxml");
        loadScreen("recycle","/view/part/recycle.fxml");
        loadScreen("loader","/view/loader.fxml");
//        primaryStage.setResizable(false);
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

    //Add the screen to the collection
    private void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    private void loadScreen(String name, String resource) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = (Parent) myLoader.load();
            addScreen(name, loadScreen);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());

        }
    }

    public static   Node getScreen(String name) {
        return screens.get(name);
    }
}
