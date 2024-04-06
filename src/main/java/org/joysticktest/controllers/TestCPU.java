package org.joysticktest.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.concurrent.TimeUnit;

public class TestCPU {
    @FXML
    private Button BackMainMenu;

    @FXML
    private Button btTest;

    @FXML
    private Label lbResult;

    @FXML
    private Label lbResultText;

    @FXML
    void btTestClicked() {
        int fBr = 0;
        int fV = 0;
        int fto_tick = 0;
        int fto_tick_out = 0;
        int fRcpu = 0;
        int fJ_cpuScore = 0;
        String fTcpu = null;
        //
        fto_tick = (int) System.currentTimeMillis();
        //

        for(int fG = 1; fG > 0;fG++){

            fRcpu = fto_tick_out - fto_tick;

            if(fRcpu > 0) {
                fTcpu = "Very Powerful CPU!! ";
            }
            if(fRcpu >= 200) {
                fTcpu = "Very Fast ";
            }
            if(fRcpu >= 500) {
                fTcpu = "Fast ";
            }
            if(fRcpu >= 800) {
                fTcpu = "Very Slow CPU!! ";
            }
            fV++;
            if(fV > 30){
                fV = 0;
            }
            if(fV == 30){
                try {
                    TimeUnit.NANOSECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                fBr++;
                fJ_cpuScore = fto_tick_out - fto_tick;
                if(fBr == 24){
                    break;
                }

            }
            fto_tick_out = (int) System.currentTimeMillis();
        }
        lbResult.setText(Integer.toString(fJ_cpuScore));
        lbResultText.setText(fTcpu);
    }
    @FXML
    void MouseClickedBackMainMenu() {
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
