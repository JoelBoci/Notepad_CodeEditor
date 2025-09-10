package com.notepad.gui.menuItems;

import com.notepad.gui.shortcuts.Shortcuts;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class ShortcutMenu extends JMenu {

    private final Shortcuts mShortcuts;

    public ShortcutMenu() {
        super("Shortcuts");

        mShortcuts = new Shortcuts();
        createShortcutMenu();
    }

    public void createShortcutMenu() {
        JMenuItem shortcutMenuItem = new JMenuItem("Keyboard Shortcuts");
        shortcutMenuItem.addActionListener(mShortcuts.showKeyboardShortcuts());

        add(shortcutMenuItem);
    }
}
