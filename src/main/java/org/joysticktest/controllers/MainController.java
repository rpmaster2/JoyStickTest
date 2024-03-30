package org.joysticktest.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class MainController {
    @FXML
    private Label item1;
    @FXML
    private Label item2;

    @FXML
    void MouseClickeditem1(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/joysticktest/views/testgamepad.fxml"));
            Parent secondRoot = loader.load();

            // Отобразите второй FXML в том же окне
            Scene scene = item1.getScene();
            scene.setRoot(secondRoot);
            scene.getWindow().setWidth(600);
            scene.getWindow().setHeight(500);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}