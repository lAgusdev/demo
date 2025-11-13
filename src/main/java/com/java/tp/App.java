package com.java.tp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException; 
import jakarta.xml.bind.*;

/**
 * @author Franco Di Meglio
 * @author Manuel Juárez
 * @author Gaspar Puente Villaroel
 * @author Agustin Nodar
 * @version 1.0
 */

/** main */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("mainMenu"), 1080, 720);
        Image icon = new Image(getClass().getResourceAsStream("/com/java/tp/img/icon.png"));
        stage.getIcons().add(icon);
        stage.setTitle("Agencia de Viajes");
        stage.setMaximized(true);
        scene.getStylesheets().add(App.class.getResource("/com/java/tp/styles/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}