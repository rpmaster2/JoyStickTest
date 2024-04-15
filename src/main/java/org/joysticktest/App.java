package org.joysticktest;

import com.jthemedetecor.OsThemeDetector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.joysticktest.theme.ThemeManager;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ThemeManager themeManager = new ThemeManager();
        final OsThemeDetector detector = OsThemeDetector.getDetector();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/mainview.fxml"));
        Parent newRoot = fxmlLoader.load();
        themeManager.applyTheme(newRoot, detector.isDark());
        Scene scene = new Scene(newRoot);
        stage.setTitle("JoyStickTest");
        stage.getIcons().add(new Image(App.class.getResourceAsStream("images/iconJE.png")));
        stage.setResizable(false);
        stage.setScene(scene);
        scene.getWindow().setWidth(500);
        scene.getWindow().setHeight(250);
        stage.show();
    }

    public static void main(String[] args) {
        Logger logger = (Logger) LoggerFactory.getLogger("org.slf4j.impl.StaticLoggerBinder");
        logger.setLevel(Level.ERROR);
        launch();
    }
}