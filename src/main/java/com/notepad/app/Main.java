package com.notepad.app;

import com.notepad.config.AppConfig;
import com.notepad.config.ThemeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.SwingUtilities;

public class Main {

    private static final Logger mLogger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        mLogger.info("Starting Notepad...");
        System.out.println("Loaded theme on startup = " + AppConfig.getTheme());
        SwingUtilities.invokeLater(() -> {
            ThemeManager.apply(AppConfig.getTheme());
            new Notepad();
        });
    }
}
