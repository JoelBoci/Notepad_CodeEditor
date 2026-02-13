package com.notepad.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.SwingUtilities;

public class Main {

    private static final Logger mLogger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        mLogger.info("Starting Notepad...");
        SwingUtilities.invokeLater(Notepad::new);
    }
}
