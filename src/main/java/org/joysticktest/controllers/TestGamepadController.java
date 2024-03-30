package org.joysticktest.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class TestGamepadController {
    @FXML
    private Button BackMainMenu;
    @FXML
    void MouseClickedBackMainMenu(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/joysticktest/views/mainview.fxml"));
            Parent Root = loader.load();

            // Отобразите второй FXML в том же окне
            Scene scene = BackMainMenu.getScene();
            scene.setRoot(Root);
            scene.getWindow().setWidth(500);
            scene.getWindow().setHeight(250);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
