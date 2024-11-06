package com.notepad.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import com.formdev.flatlaf.FlatLightLaf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Notepad {

    private static final Logger logger = LoggerFactory.getLogger(CodeEditor.class);

    private JFrame frame;
    private JTextArea textArea;

    public Notepad() {
        setDefaultMode();

        setGlobalFont(new Font("Arial", Font.PLAIN, 14)); // Set your desired global font

        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/notepad.png")));
        Image image = imageIcon.getImage();

        frame = new JFrame("Notepad");
        textArea = new JTextArea();

        JScrollPane scrollPane = new JScrollPane(textArea);

        createMenuBar();

        frame.add(scrollPane);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setIconImage(image);
        frame.setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Create menus
        FileMenu fileMenu = new FileMenu(frame, textArea);
        EditMenu editMenu = new EditMenu(frame, textArea);
        FormatMenu formatMenu = new FormatMenu(textArea);
        ViewMenu viewMenu = new ViewMenu(textArea);
        SettingsMenu settingsMenu = new SettingsMenu(frame);

        // Add menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);
        menuBar.add(viewMenu);
        menuBar.add(settingsMenu);

        // Set the menu bar on the frame
        frame.setJMenuBar(menuBar);
    }

    private void setGlobalFont(Font font) {
        UIManager.getLookAndFeelDefaults().keys().asIterator().forEachRemaining(key -> {
            if (UIManager.get(key) instanceof Font) {
                UIManager.put(key, font);
            }
        });
    }

    private void setDefaultMode() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            logger.error("Couldn't set default mode: {}", e.getMessage(), e);

        }
    }
}
