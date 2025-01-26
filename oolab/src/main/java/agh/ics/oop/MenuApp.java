package agh.ics.oop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuApp extends Application {
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("menu.fxml"));
        VBox viewRoot = loader.load();
        Scene scene = new Scene(viewRoot);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Darwin world");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }
}
