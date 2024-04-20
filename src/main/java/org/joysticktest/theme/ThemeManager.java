package org.joysticktest.theme;

import javafx.scene.Parent;


public class ThemeManager {
    static String DARK_STYLE = "-fx-base: #000000";
    static String LIGHT_STYLE = "-fx-base: #d7d7d7";

    public void applyTheme(Parent root, boolean isDark) {
        String style = isDark ? DARK_STYLE : LIGHT_STYLE;
        root.setStyle(style);
    }
}
