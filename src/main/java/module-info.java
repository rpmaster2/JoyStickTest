module org.je.joysticktest {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.joysticktest to javafx.fxml;
    exports org.joysticktest;
    exports org.joysticktest.controllers;
    opens org.joysticktest.controllers to javafx.fxml;
}