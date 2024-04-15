package org.joysticktest.controllers;

import com.jthemedetecor.OsThemeDetector;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import net.java.games.input.*;
import org.joysticktest.theme.ThemeManager;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TestGamepadController {
    private Controller[] controllers;
    private AnimationTimer gamepadPoller;

    static {
        // Относительный путь к папке lib, предполагая, что она находится на том же уровне, что и JAR-файл
        String pathToLib = "./lib";
        System.setProperty("net.java.games.input.librarypath", new File(pathToLib).getAbsolutePath());
    }
    @FXML
    private Button BackMainMenu;
    @FXML
    private Button DPad_DOWN;
    @FXML
    private Button DPad_LEFT;
    @FXML
    private Button DPad_RIGHT;
    @FXML
    private Button DPad_UP;
    @FXML
    private Button A_X;

    @FXML
    private Button B_Circle;
    @FXML
    private Button X_Square;

    @FXML
    private Button Y_Triangle;

    @FXML
    private Label gamepadstatus;
    @FXML
    void MouseClickedBackMainMenu() {
        try {
            // Остановка AnimationTimer
            if (gamepadPoller != null) {
                gamepadPoller.stop();
                gamepadPoller = null; // Удаление ссылки на AnimationTimer
            }

            // Очистка ссылок на элементы управления
            DPad_UP = null;
            DPad_DOWN = null;
            DPad_LEFT = null;
            DPad_RIGHT = null;
            A_X = null;
            B_Circle = null;
            X_Square = null;
            Y_Triangle = null;
            gamepadstatus = null;

            // Загрузка новой сцены
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/joysticktest/views/mainview.fxml"));
            Parent Root = loader.load();
            ThemeManager themeManager = new ThemeManager();
            OsThemeDetector detector = OsThemeDetector.getDetector();
            themeManager.applyTheme(Root, detector.isDark());
            Scene scene = BackMainMenu.getScene();


            // Установка новой сцены
            scene.setRoot(Root);
            scene.getWindow().setWidth(500);
            scene.getWindow().setHeight(250);

            // Предложение сборщику мусора выполнить очистку
            BackMainMenu = null;
            System.gc();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        Logger logger = Logger.getLogger("net.java.games.input.ControllerEnvironment");
        logger.setLevel(Level.SEVERE);
        System.out.println("Initializing ControllerEnvironment");
        // Получение списка доступных контроллеров
        controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        // Запуск опроса контроллеров
        startPolling();
    }
    private void startPolling() {
        // Создание и запуск AnimationTimer для опроса контроллеров
        gamepadPoller = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //System.out.println("Gamepad polling " + now);
                for (Controller controller : controllers) {
                    if (controller.poll()) {
                        // Обработка событий для активных контроллеров
                        gamepadstatus.setText(controller.getName());
                        processEvents(controller);
                    }
                }
                // Проверка на подключение новых контроллеров
                checkForNewControllers();
            }

        };
        gamepadPoller.start();
    }
    private void checkForNewControllers() {
        //System.out.printf("Checking for new controllers\n");
        // Обновление списка контроллеров
        controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
    }

    private void processEvents(Controller controller) {
        //System.out.println("Processing events");
        EventQueue queue = controller.getEventQueue();
        Event event = new Event();
        while (queue.getNextEvent(event)) {
            Component comp = event.getComponent();
            float value = event.getValue();
            gamepadstatus.setText(controller.getName());
            // Обработка значений с помощью метода processGamepadInput
            processGamepadInput(comp, value);
        }
    }
    private void processGamepadInput(Component comp, float value) {
        //System.out.println("Processing gamepad input");
        // Логика обработки ввода с геймпада
        if (comp.getIdentifier() == Component.Identifier.Axis.POV) {
            System.out.println("POV Value: " + value);

            // Логика для определения направления D-Pad
            // Например, если значение увеличивается или уменьшается на определенную величину
            if (value == 0.25f) { // Вверх
                DPad_UP.setVisible(true);
            } else if (value == 0.5f) { // Вправо
                DPad_RIGHT.setVisible(true);
            } else if (value == 0.75f) { // Вниз
                DPad_DOWN.setVisible(true);
            } else if (value == 1f) { // Влево
                DPad_LEFT.setVisible(true);
            } else if (value == 0.0f) { // Центральное положение
                // Скрыть все кнопки D-Pad
                DPad_UP.setVisible(false);
                DPad_RIGHT.setVisible(false);
                DPad_DOWN.setVisible(false);
                DPad_LEFT.setVisible(false);
            }
        }
        if (comp.getIdentifier() == Component.Identifier.Button._1) {
            if (value == 1f){
                A_X.setVisible(true);
            }
            else if (value == 0f){
                A_X.setVisible(false);
            }
        }
        if (comp.getIdentifier() == Component.Identifier.Button._2) {
            if (value == 1f){
                B_Circle.setVisible(true);
            }
            else if (value == 0f){
                B_Circle.setVisible(false);
            }
        }
        if (comp.getIdentifier() == Component.Identifier.Button._0) {
            if (value == 1f){
                X_Square.setVisible(true);
            }
            else if (value == 0f){
                X_Square.setVisible(false);
            }
        }
        if (comp.getIdentifier() == Component.Identifier.Button._3) {
            if (value == 1f){
                Y_Triangle.setVisible(true);
            }
            else if (value == 0f){
                Y_Triangle.setVisible(false);
            }
        }

    }

}
