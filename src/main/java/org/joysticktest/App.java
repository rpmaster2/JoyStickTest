package org.joysticktest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/mainview.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("JoyStickTest");
        stage.getIcons().add(new Image(App.class.getResourceAsStream("images/iconJE.png")));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}