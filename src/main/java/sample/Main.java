package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import sample.connection.ApiConnection;
import sample.storage.Storage;

import java.util.HashMap;

public class Main extends Application {

    private volatile static  HashMap<String, Node> screens = new HashMap<>();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Font.loadFont("/font/OpenSans-Regular.ttf", 16);
//        String token ="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIrMzc0OTExMDMzNTQiLCJwYXNzd29yZCI6IiQyYSQxMCRzTlplc0Z0TUJxWFZraUJJMGlBVjVPdVZnWG1aYjZqaHkwRGdTdDVHdTBiQ0oyRnltY1ovVyIsImlkIjoyLCJleHAiOjE1NzY4MDQ4MjksImlhdCI6MTU2MzgwNDgyOX0.p5y24EfT_LLLe6vTgbsq81XRzm5ZP7PJQBJcNuVW6VrrLRVvrek6wqfhHP6txZNG0nDFa77IMCIIWBpxwJQTmA";
//        Storage.getInstance().setToken(token);
//        Storage.getInstance().setCurrentToken(token);
        javafx.scene.image.Image logo = new Image(this.getClass().getResourceAsStream("/image/logo.png"));
        primaryStage.getIcons().add(logo);
        loadScreen("slider","/view/part/slid.fxml");
        loadScreen("recycle","/view/part/recycle.fxml");
        loadScreen("loader","/view/loader.fxml");
        primaryStage.setResizable(false);

        if(Storage.getInstance().getToken()==null || Storage.getInstance().getToken().isEmpty()){
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            primaryStage.setTitle("Log in");
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(we->{
                System.exit(0);
            });
            primaryStage.show();
        }else{
            System.out.println("token ka");
            ApiConnection.getInstance().baseData(Constant.BASE_DATA_URI,1,Storage.getInstance().getCurrentToken());
        }

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
