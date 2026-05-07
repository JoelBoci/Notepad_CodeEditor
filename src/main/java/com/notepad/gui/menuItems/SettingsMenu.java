package com.notepad.gui.menuItems;

import com.notepad.config.AppConfig;


import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import com.notepad.config.ThemeManager;

public class SettingsMenu extends JMenu {

    private final JFrame mFrame;
    private final OtherMenu mOtherMenu;


    public SettingsMenu(JFrame frame, OtherMenu otherMenu) {
        super("Settings");
        mFrame = frame;
        mOtherMenu = otherMenu;
        addSettingsMenu();
    }

    private void addSettingsMenu() {
        JMenu themeMenu = new JMenu("Themes");

        JMenuItem lightModeMenuItem = new JMenuItem("Light");
        lightModeMenuItem.addActionListener(_ -> applyTheme("light"));

        JMenuItem darkModeMenuItem = new JMenuItem("Dark");
        darkModeMenuItem.addActionListener(_ -> applyTheme("dark"));

        JMenuItem darculaModeMenuItem = new JMenuItem("Darcula");
        darculaModeMenuItem.addActionListener(_ -> applyTheme("darcula"));

        JMenuItem macLightModeMenuItem = new JMenuItem("Mac Light");
        macLightModeMenuItem.addActionListener(_ -> applyTheme("macLight"));

        JMenuItem macDarkModeMenuItem = new JMenuItem("Mac Dark");
        macDarkModeMenuItem.addActionListener(_ -> applyTheme("macDark"));

        themeMenu.add(lightModeMenuItem);
        themeMenu.add(darkModeMenuItem);
        themeMenu.add(darculaModeMenuItem);
        themeMenu.add(macLightModeMenuItem);
        themeMenu.add(macDarkModeMenuItem);
        add(themeMenu);
    }

    private void applyTheme(String theme) {
        ThemeManager.apply(theme);
        AppConfig.setTheme(theme);
        mOtherMenu.refreshMarkdownPreview();
        SwingUtilities.updateComponentTreeUI(mFrame);
    }
}
