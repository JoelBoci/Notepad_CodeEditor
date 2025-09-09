package com.notepad.gui.menuItems;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import com.notepad.gui.CodeEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SettingsMenu extends JMenu {

    private static final Logger logger = LoggerFactory.getLogger(SettingsMenu.class);

    private JFrame frame;

    public SettingsMenu(JFrame frame) {
        super("Settings");
        this.frame = frame;
        addSettingsMenu();
    }

    private void addSettingsMenu() {
        JMenu themeMenu = new JMenu("Themes");

        JMenuItem lightModeMenuItem = new JMenuItem("Light");
        lightModeMenuItem.addActionListener(_ -> setLightMode());

        JMenuItem darkModeMenuItem = new JMenuItem("Dark");
        darkModeMenuItem.addActionListener(_ -> setDarkMode());

        JMenuItem darculaModeMenuItem = new JMenuItem("Darcula");
        darculaModeMenuItem.addActionListener(_ -> setDarculaMode());

        JMenuItem macLightModeMenuItem = new JMenuItem("Mac Light");
        macLightModeMenuItem.addActionListener(_ -> setMacLightMode());

        JMenuItem macDarkModeMenuItem = new JMenuItem("Mac Dark");
        macDarkModeMenuItem.addActionListener(_ -> setMacDarkMode());

        themeMenu.add(lightModeMenuItem);
        themeMenu.add(darkModeMenuItem);
        themeMenu.add(darculaModeMenuItem);
        themeMenu.add(macLightModeMenuItem);
        themeMenu.add(macDarkModeMenuItem);
        add(themeMenu);
    }

    private void setLightMode() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            logger.error("Couldn't set light mode: {}", e.getMessage(), e);
        }
    }

    private void setDarkMode() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            logger.error("Couldn't set dark mode: {}", e.getMessage(), e);
        }
    }

    private void setDarculaMode() {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            logger.error("Couldn't set darcula mode: {}", e.getMessage(), e);
        }
    }

    private void setMacLightMode() {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            logger.error("Couldn't set mac light mode: {}", e.getMessage(), e);
        }
    }

    private void setMacDarkMode() {
        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            logger.error("Couldn't set mac dark mode: {}", e.getMessage(), e);
        }
    }
}
