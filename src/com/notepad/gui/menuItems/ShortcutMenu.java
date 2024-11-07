package com.notepad.gui.menuItems;

import com.notepad.gui.shortcuts.Shortcuts;

import javax.swing.*;

public class ShortcutMenu extends JMenu {

    private Shortcuts shortcuts;

    public ShortcutMenu() {
        super("Shortcuts");

        shortcuts = new Shortcuts();
        createShortcutMenu();
    }

    public void createShortcutMenu() {
        JMenuItem shortcutMenuItem = new JMenuItem("Keyboard Shortcuts");
        shortcutMenuItem.addActionListener(shortcuts.showKeyboardShortcuts());

        add(shortcutMenuItem);
    }
}
