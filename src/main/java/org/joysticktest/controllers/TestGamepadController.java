package org.joysticktest.controllers;

import com.jthemedetecor.OsThemeDetector;
import de.ralleytn.plugins.jinput.xinput.XInputEnvironmentPlugin;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.EventQueue;
import net.java.games.input.Rumbler;
import net.java.games.input.Event;
import net.java.games.input.Component;
import org.joysticktest.theme.ThemeManager;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TestGamepadController {
    private Controller[] controllers;
    private Controller[] controllerss;
    private AnimationTimer gamepadPoller;
    private Rumbler[] rumblerss;
    ControllerEnvironment env = new XInputEnvironmentPlugin();
    static {
        // Относительный путь к папке lib, предполагая, что она находится на том же уровне, что и JAR-файл
        String pathToLib = "lib";
        System.setProperty("net.java.games.input.librarypath", new File(pathToLib).getAbsolutePath());
        System.setProperty("jinput.loglevel", "OFF");
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
    private Button LT2_L2;

    @FXML
    private Button LT_L1;

    @FXML
    private Button RT2_R2;

    @FXML
    private Button RT_R1;

    @FXML
    private Button Start_Option;
    @FXML
    private Button Xbox_PS;
    @FXML
    private Button Back_Share;
    @FXML
    private Label LeftPointer;
    @FXML
    private Label RightPointer;
    @FXML
    private Label LeftValue;
    @FXML
    private Label RightValue;
    @FXML
    private Canvas LeftStick;
    private GraphicsContext gc;
    @FXML
    private Canvas RightStick;
    private GraphicsContext gc2;
    private float lastX = 0.0f; // Последнее значение оси X
    private float lastY = 0.0f; // Последнее значение оси Y
    private float lastZ = 0.0f; // Последнее значение оси Z
    private float lastRZ = 0.0f; // Последнее значение оси RZ
    @FXML
    private Button LeftMotor;
    @FXML
    private Button RightMotor;
    private boolean vibrateLeft = false;
    private boolean vibrateRight = false;

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
            Start_Option = null;
            Xbox_PS = null;
            Back_Share = null;
            LeftPointer = null;
            RightPointer = null;
            LeftStick = null;
            RightStick = null;
            gc = null;
            gc2 = null;
            LeftMotor = null;
            RightMotor = null;


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
        logger.setLevel(Level.OFF);
        gc = LeftStick.getGraphicsContext2D();
        gc2 = RightStick.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.fillOval(32.5, 32.5, 10, 10);
        gc2.setFill(Color.BLUE);
        gc2.fillOval(32.5, 32.5, 10, 10);
        System.out.println("Initializing ControllerEnvironment");
        // Получение списка доступных контроллеров
        controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        try {
            controllerss = env.getControllers();
        }
        catch (Exception e) {
            System.out.println("Controllers not available");
        }

        if (controllerss.length > 0) {
            for (Controller controller : controllerss) {
                if (controller.getType() == Controller.Type.GAMEPAD) {
                    rumblerss = controller.getRumblers();
                    if (rumblerss.length > 0) {
                        System.out.println("Rumblers initialized: " + rumblerss.length);
                        break; // Предполагаем, что используется только один геймпад
                    }
                }
            }
        }

        if (rumblerss == null) {
            System.out.println("No rumblers found for the controller.");
        }
        startPolling();
    }
    private void startPolling() {
        // Создание и запуск AnimationTimer для опроса контроллеров
        gamepadPoller = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //System.out.println("Gamepad polling " + now);
                for (Controller controller : controllers) {
                    if (controller.getType() == Controller.Type.GAMEPAD) {
                        if (controller.poll()) {
                            // Обработка событий для активных контроллеров
                            processEvents(controller);
                        }
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
            processGamepadInput(controller, comp, value);
        }
    }
    private void updateLeftStickPosition() {
        // Используйте lastX и lastY для обновления позиции стика
        // Например, отрисовка на Canvas
        double canvasCenterX = LeftStick.getWidth();
        double canvasCenterY = LeftStick.getHeight();
        double stickPosX = canvasCenterX * (lastX + 1) / 2;
        double stickPosY = canvasCenterY * (lastY + 1) / 2;
        gc.clearRect(0, 0, LeftStick.getWidth(), LeftStick.getHeight()); // Очистка Canvas
        gc.fillOval(stickPosX - 5, stickPosY - 5, 10, 10); // Рисование стика
    }
    private void updateRightStickPositionDualshock() {
        // Используйте lastX и lastY для обновления позиции стика
        // Например, отрисовка на Canvas
        double canvasCenterZ = RightStick.getWidth();
        double canvasCenterRZ = RightStick.getHeight();
        double stickPosZ = canvasCenterZ * (lastZ + 1) / 2;
        double stickPosRZ = canvasCenterRZ * (lastRZ + 1) / 2;
        gc2.clearRect(0, 0, RightStick.getWidth(), RightStick.getHeight()); // Очистка Canvas
        gc2.fillOval(stickPosZ - 5, stickPosRZ - 5, 10, 10); // Рисование стика
    }
    private void processGamepadInput(Controller controller, Component comp, float value) {
        //Создание интерфейса с общим принципом для Xbox подобных геймпадов и для Dualshock 4
        interface GeneralPrinciple {
            void apply();
        }
        GeneralPrinciple generalPrinciple = new GeneralPrinciple() {
            @Override
            public void apply() {
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
                if (comp.getIdentifier() == Component.Identifier.Axis.X) {
                    lastX = value; // Сохранение текущего значения оси X
                    updateLeftStickPosition();// Обновление позиции стика
                } else if (comp.getIdentifier() == Component.Identifier.Axis.Y) {
                    lastY = value; // Сохранение текущего значения оси Y
                    updateLeftStickPosition(); // Обновление позиции стика
                }
                if (comp.getIdentifier() == Component.Identifier.Button._4) {
                    if (value == 1f) {
                        LT_L1.setVisible(true);
                    } else if (value == 0f) {
                        LT_L1.setVisible(false);
                    }
                }
                if (comp.getIdentifier() == Component.Identifier.Button._5 ) {
                    if (value == 1f) {
                        RT_R1.setVisible(true);
                    } else if (value == 0f) {
                        RT_R1.setVisible(false);
                    }
                }
            }
        };
        //System.out.println("Processing gamepad input");
        // Логика обработки ввода с геймпада
        if (controller.getName().toLowerCase().contains("ps3")) {
            gamepadstatus.setText(controller.getName());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Unsupported gamepad (For now)");
            alert.setHeaderText(null);
            alert.setContentText("Wait a little while and support will come soon. It won't be long now.");
            alert.showAndWait();
            System.exit(0);
        }
        else{
            if (controller.getName().toLowerCase().contains("wireless controller")) {
                gamepadstatus.setText(controller.getName());
                generalPrinciple.apply();
                if (comp.getIdentifier() == Component.Identifier.Button._8) {
                    if (value == 1f) {
                        Back_Share.setVisible(true);
                    } else if (value == 0f) {
                        Back_Share.setVisible(false);
                    }
                }
                if (comp.getIdentifier() == Component.Identifier.Button._9) {
                    if (value == 1f) {
                        Start_Option.setVisible(true);
                    } else if (value == 0f) {
                        Start_Option.setVisible(false);
                    }
                }
                if (comp.getIdentifier() == Component.Identifier.Button._12) {
                    if (value == 1f) {
                        Xbox_PS.setVisible(true);
                    } else if (value == 0f) {
                        Xbox_PS.setVisible(false);
                    }
                }
                //Кнопка A на Xbox типа геймпада или X у dualshock 4
                if (comp.getIdentifier() == Component.Identifier.Button._1) {
                    if (value == 1f) {
                        A_X.setVisible(true);
                    } else if (value == 0f) {
                        A_X.setVisible(false);
                    }
                }
                //Кнопка B на Xbox типа геймпада или Circle у dualshock 4
                if (comp.getIdentifier() == Component.Identifier.Button._2) {
                    if (value == 1f) {
                        B_Circle.setVisible(true);
                    } else if (value == 0f) {
                        B_Circle.setVisible(false);
                    }
                }
                //Кнопка X на Xbox типа геймпада или Square у dualshock 4
                if (comp.getIdentifier() == Component.Identifier.Button._0) {
                    if (value == 1f) {
                        X_Square.setVisible(true);
                    } else if (value == 0f) {
                        X_Square.setVisible(false);
                    }
                }
                //Кнопка Y на Xbox типа геймпада или Triangle у dualshock 4
                if (comp.getIdentifier() == Component.Identifier.Button._3) {
                    if (value == 1f) {
                        Y_Triangle.setVisible(true);
                    } else if (value == 0f) {
                        Y_Triangle.setVisible(false);
                    }
                }

                if (comp.getIdentifier() == Component.Identifier.Axis.RX) {
                    if (value > -1f) {
                        LeftValue.setText(String.valueOf(value));
                        LT2_L2.setVisible(true);
                        LeftPointer.setVisible(true);
                        LeftValue.setVisible(true);
                    } else if (value == -1f) {
                        LT2_L2.setVisible(false);
                        LeftPointer.setVisible(false);
                        LeftValue.setVisible(false);
                    }
                }
                if (comp.getIdentifier() == Component.Identifier.Axis.RY) {
                    if (value > -1f) {
                        RightValue.setText(String.valueOf(value));
                        RT2_R2.setVisible(true);
                        RightPointer.setVisible(true);
                        RightValue.setVisible(true);
                    } else if (value == -1f) {
                        RT2_R2.setVisible(false);
                        RightPointer.setVisible(false);
                        RightValue.setVisible(false);
                    }
                }
                if (comp.getIdentifier() == Component.Identifier.Axis.Z) {
                    lastZ = value;
                    updateRightStickPositionDualshock();
                } else if (comp.getIdentifier() == Component.Identifier.Axis.RZ) {
                    lastRZ = value;
                    updateRightStickPositionDualshock();
                }

            }
            else {
                //Кнопка A на Xbox типа геймпада или X у dualshock 4
                if (comp.getIdentifier() == Component.Identifier.Button._0) {
                    if (value == 1f) {
                        A_X.setVisible(true);
                    } else if (value == 0f) {
                        A_X.setVisible(false);
                    }
                }
                //Кнопка B на Xbox типа геймпада или Circle у dualshock 4
                if (comp.getIdentifier() == Component.Identifier.Button._1) {
                    if (value == 1f) {
                        B_Circle.setVisible(true);
                    } else if (value == 0f) {
                        B_Circle.setVisible(false);
                    }
                }
                //Кнопка X на Xbox типа геймпада или Square у dualshock 4
                if (comp.getIdentifier() == Component.Identifier.Button._2) {
                    if (value == 1f) {
                        X_Square.setVisible(true);
                    } else if (value == 0f) {
                        X_Square.setVisible(false);
                    }
                }
                //Кнопка Y на Xbox типа геймпада или Triangle у dualshock 4
                if (comp.getIdentifier() == Component.Identifier.Button._3) {
                    if (value == 1f) {
                        Y_Triangle.setVisible(true);
                    } else if (value == 0f) {
                        Y_Triangle.setVisible(false);
                    }
                }
                if (comp.getIdentifier() == Component.Identifier.Axis.Z) {
                    // Обработка триггера LT
                    if (value > 0.2f) {
                        LeftValue.setText(String.valueOf(value));
                        LT2_L2.setVisible(true);
                        LeftPointer.setVisible(true);
                        LeftValue.setVisible(true);
                    } else if (value <= 0.2f) {
                        // Логика при отпускании триггера LT
                        LT2_L2.setVisible(false);
                        LeftPointer.setVisible(false);
                        LeftValue.setVisible(false);
                    }
                }
                if (comp.getIdentifier() == Component.Identifier.Axis.Z) {
                    if (value <= -0.2f) {
                        RightValue.setText(String.valueOf(value));
                        RT2_R2.setVisible(true);
                        RightPointer.setVisible(true);
                        RightValue.setVisible(true);
                    } else if (value >= -0.2f) {
                        // Логика при отпускании триггера RT
                        RT2_R2.setVisible(false);
                        RightPointer.setVisible(false);
                        RightValue.setVisible(false);
                    }
                }

                if (comp.getIdentifier() == Component.Identifier.Axis.RX) {
                    lastZ = value;
                    updateRightStickPositionDualshock();
                } else if (comp.getIdentifier() == Component.Identifier.Axis.RY) {
                    lastRZ = value;
                    updateRightStickPositionDualshock();
                }
                if (comp.getIdentifier() == Component.Identifier.Button._6) {
                    if (value == 1f) {
                        Back_Share.setVisible(true);
                    } else if (value == 0f) {
                        Back_Share.setVisible(false);
                    }
                }
                if (comp.getIdentifier() == Component.Identifier.Button._7) {
                    if (value == 1f) {
                        Start_Option.setVisible(true);
                    } else if (value == 0f) {
                        Start_Option.setVisible(false);
                    }
                }
                gamepadstatus.setText(controller.getName());
                generalPrinciple.apply();
            }
        }
    }
    @FXML
    void MouseClickedLeftMotor() {
        System.out.println("Left motor clicked");
        if (canVibrate(controllers[0])) {
            try {
                if (rumblerss != null && rumblerss.length > 0 && rumblerss[0] != null) {
                    Rumbler rumblerLeft = rumblerss[0];
                    if (!vibrateLeft) {
                        System.out.println("Activating left motor");
                        vibrateLeft = true;
                        rumblerLeft.rumble(1.0f);
                    } else {
                        System.out.println("Deactivating left motor");
                        vibrateLeft = false;
                        rumblerLeft.rumble(0.0f);
                    }
                } else {
                    System.out.println("No rumblers found or rumblers are not initialized");
                }
            } catch (Exception e) {
                System.out.println("Exception occurred while trying to activate left motor: " + e.getMessage());
            }
        }
    }

    @FXML
    void MouseClickedRightMotor() {
        if (canVibrate(controllers[0])) {
            System.out.println("Right motor clicked");
            try {
                if (rumblerss != null && rumblerss.length > 1 && rumblerss[1] != null) {
                    Rumbler rumblerRight = rumblerss[1];
                    if (!vibrateRight) {
                        System.out.println("Activating right motor");
                        vibrateRight = true;
                        rumblerRight.rumble(1.0f);
                    } else {
                        System.out.println("Deactivating right motor");
                        vibrateRight = false;
                        rumblerRight.rumble(0.0f);
                    }
                } else {
                    System.out.println("No rumblers found or rumblers are not initialized");
                }
            } catch (Exception e) {
                System.out.println("Exception occurred while trying to activate right motor: " + e.getMessage());
            }
        }
    }
    private boolean canVibrate(Controller controller) {
        String name = controller.getName().toLowerCase();
        return !(name.contains("wireless controller") || name.contains("ps3"));
    }

}
