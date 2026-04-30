package com.notepad.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


public final class AppConfig {
    private static final Preferences PREFS = Preferences.userRoot().node("com/notepad"); // use a path style
    private static final Logger mLogger = LoggerFactory.getLogger(AppConfig.class);

    private AppConfig() {}

    public static void setTheme(String theme) {
        PREFS.put("theme", theme);
        try {
            PREFS.flush();
        } catch (BackingStoreException e) {
            mLogger.info("Theme setting failed: " + e.getMessage());
        }
    }

    public static String getTheme() {
        return PREFS.get("theme", "light");
    }
}
