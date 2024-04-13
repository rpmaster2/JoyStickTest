package org.joysticktest.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class MainController {
    @FXML
    private Label item1;
    @FXML
    private Label item2;

    @FXML
    void MouseClickeditem1() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/joysticktest/views/testgamepad.fxml"));
            Parent secondRoot = loader.load();
            Scene scene = item1.getScene();
            scene.setRoot(secondRoot);
            scene.getWindow().setWidth(600);
            scene.getWindow().setHeight(500);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void MouseClickeditem2() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/joysticktest/views/testcpu.fxml"));
            Parent secondRoot = loader.load();
            Scene scene = item2.getScene();
            scene.setRoot(secondRoot);
            scene.getWindow().setWidth(520);
            scene.getWindow().setHeight(370);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}