module org.je.joysticktest {
    requires javafx.controls;
    requires javafx.fxml;
    requires jinput;
    requires com.jthemedetector;
    requires java.logging;
    requires java.desktop;
    requires org.slf4j;
    requires logback.classic;


    opens org.joysticktest to javafx.fxml;
    exports org.joysticktest;
    exports org.joysticktest.controllers;
    opens org.joysticktest.controllers to javafx.fxml;
}