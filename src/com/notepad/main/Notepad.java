package com.notepad.main;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import java.awt.Font;
import java.awt.Image;
import java.util.Objects;

import com.formdev.flatlaf.FlatLightLaf;

import com.notepad.gui.menuItems.EditMenu;
import com.notepad.gui.menuItems.FileMenu;
import com.notepad.gui.menuItems.FormatMenu;
import com.notepad.gui.menuItems.SettingsMenu;
import com.notepad.gui.menuItems.ShortcutMenu;
import com.notepad.gui.menuItems.ViewMenu;

import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Notepad {

    private static final Logger logger = LoggerFactory.getLogger(Notepad.class);

    private final JFrame frame;
    private final JTextArea textArea;
    private final StatusBar statusBar;

    private int zoomPercent = 100;

    public Notepad() {
        setDefaultMode();

        setGlobalFont(new Font("Arial", Font.PLAIN, 14));

        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/notepad.png")));
        Image image = imageIcon.getImage();

        frame = new JFrame("Notepad");
        frame.setLayout(new MigLayout(
                "insets 0, fill", "[grow]", "[grow][]"));

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);

        statusBar = new StatusBar();
        statusBar.bindToEditor(textArea);
        statusBar.setZoomPercent(zoomPercent);
        statusBar.setEncodingDisplay("UTF-8");
        statusBar.setEolDisplay("LF");

        createMenuBar();

        frame.add(scrollPane, "cell 0 0, grow, push");
        frame.add(statusBar,  "cell 0 1, growx");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setIconImage(image);
        frame.setVisible(true);
    }

    private void createMenuBar() {
        logger.info("Creating menu bar...");

        JMenuBar menuBar = new JMenuBar();

        // Create menus
        FileMenu fileMenu = new FileMenu(frame, textArea);
        EditMenu editMenu = new EditMenu(frame, textArea);
        FormatMenu formatMenu = new FormatMenu(textArea);
        ViewMenu viewMenu = new ViewMenu(textArea, this::onZoomChanged);
        SettingsMenu settingsMenu = new SettingsMenu(frame);
        ShortcutMenu shortcutMenu = new ShortcutMenu();

        // Add menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);
        menuBar.add(viewMenu);
        menuBar.add(settingsMenu);
        menuBar.add(shortcutMenu);

        // Set the menu bar on the frame
        frame.setJMenuBar(menuBar);
    }

    private void onZoomChanged(int newZoomPercent) {
        this.zoomPercent = newZoomPercent;
        statusBar.setZoomPercent(newZoomPercent);
    }

    private void setGlobalFont(Font font) {
        logger.info("Setting global fonts...");
        UIManager.getLookAndFeelDefaults().keys().asIterator().forEachRemaining(key -> {
            if (UIManager.get(key) instanceof Font) {
                UIManager.put(key, font);
            }
        });
    }

    private void setDefaultMode() {
        try {
            logger.info("Setting default look and feel...");
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            logger.error("Couldn't set default mode: {}", e.getMessage(), e);
        }
    }
}
