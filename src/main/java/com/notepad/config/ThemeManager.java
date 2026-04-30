package com.notepad.config;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

public final class ThemeManager {
    private ThemeManager() {}

    public static void apply(String themeKey) {
        if (themeKey == null) {
            FlatLightLaf.setup();
            return;
        }

        switch (themeKey) {
            case "light" -> FlatLightLaf.setup();
            case "dark" -> FlatDarkLaf.setup();
            case "darcula" -> FlatDarculaLaf.setup();
            case "macLight" -> FlatMacLightLaf.setup();
            case "macDark" -> FlatMacDarkLaf.setup();
            default -> FlatLightLaf.setup();
        }
    }
}
