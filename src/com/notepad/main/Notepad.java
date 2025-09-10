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

    private static final Logger mLogger = LoggerFactory.getLogger(Notepad.class);

    private final JFrame mFrame;
    private final JTextArea mTextArea;
    private final StatusBar mStatusBar;

    private int mZoomPercent = 100;

    public Notepad() {
        setDefaultMode();

        setGlobalFont(new Font("Arial", Font.PLAIN, 14));

        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/notepad.png")));
        Image image = imageIcon.getImage();

        mFrame = new JFrame("Notepad");
        mFrame.setLayout(new MigLayout(
                "insets 0, fill", "[grow]", "[grow][]"));

        mTextArea = new JTextArea();
        mTextArea.setLineWrap(true);
        mTextArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(mTextArea);

        mStatusBar = new StatusBar();
        mStatusBar.bindToEditor(mTextArea);
        mStatusBar.setZoomPercent(mZoomPercent);
        mStatusBar.setEncodingDisplay("UTF-8");
        mStatusBar.setEolDisplay("LF");

        createMenuBar();

        mFrame.add(scrollPane, "cell 0 0, grow, push");
        mFrame.add(mStatusBar,  "cell 0 1, growx");
        mFrame.setSize(800, 600);
        mFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mFrame.setIconImage(image);
        mFrame.setVisible(true);
    }

    private void createMenuBar() {
        mLogger.info("Creating menu bar...");

        JMenuBar menuBar = new JMenuBar();

        // Create menus
        FileMenu fileMenu = new FileMenu(mFrame, mTextArea);
        EditMenu editMenu = new EditMenu(mFrame, mTextArea);
        FormatMenu formatMenu = new FormatMenu(mTextArea);
        ViewMenu viewMenu = new ViewMenu(mTextArea, this::onZoomChanged);
        SettingsMenu settingsMenu = new SettingsMenu(mFrame);
        ShortcutMenu shortcutMenu = new ShortcutMenu();

        // Add menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);
        menuBar.add(viewMenu);
        menuBar.add(settingsMenu);
        menuBar.add(shortcutMenu);

        // Set the menu bar on the frame
        mFrame.setJMenuBar(menuBar);
    }

    private void onZoomChanged(int newZoomPercent) {
        this.mZoomPercent = newZoomPercent;
        mStatusBar.setZoomPercent(newZoomPercent);
    }

    private void setGlobalFont(Font font) {
        mLogger.info("Setting global fonts...");
        UIManager.getLookAndFeelDefaults().keys().asIterator().forEachRemaining(key -> {
            if (UIManager.get(key) instanceof Font) {
                UIManager.put(key, font);
            }
        });
    }

    private void setDefaultMode() {
        try {
            mLogger.info("Setting default look and feel...");
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            mLogger.error("Couldn't set default mode: {}", e.getMessage(), e);
        }
    }
}
