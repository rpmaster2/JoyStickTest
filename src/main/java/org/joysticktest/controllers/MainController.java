package org.joysticktest.controllers;

import com.jthemedetecor.OsThemeDetector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import org.joysticktest.theme.ThemeManager;


public class MainController {
    @FXML
    private Label item1;
    @FXML
    private Label item2;

    @FXML
    void MouseClickeditem1() {
        try {
            item2 = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/joysticktest/views/testgamepad.fxml"));
            Parent secondRoot = loader.load();
            ThemeManager themeManager = new ThemeManager();
            OsThemeDetector detector = OsThemeDetector.getDetector();
            Scene scene = item1.getScene();
            scene.setRoot(secondRoot);
            themeManager.applyTheme(secondRoot, detector.isDark());
            scene.getWindow().setWidth(620);
            scene.getWindow().setHeight(550);
            item1 = null;
            System.gc();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void MouseClickeditem2() {
        try {
            item1 = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/joysticktest/views/testcpu.fxml"));
            Parent secondRoot = loader.load();
            ThemeManager themeManager = new ThemeManager();
            OsThemeDetector detector = OsThemeDetector.getDetector();
            themeManager.applyTheme(secondRoot, detector.isDark());
            Scene scene = item2.getScene();
            scene.setRoot(secondRoot);
            scene.getWindow().setWidth(520);
            scene.getWindow().setHeight(370);
            item2 = null;
            System.gc();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}