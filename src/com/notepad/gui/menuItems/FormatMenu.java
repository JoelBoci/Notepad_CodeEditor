package com.notepad.gui.menuItems;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

import java.awt.ComponentOrientation;

public class FormatMenu extends JMenu {
    private final JTextArea mTextArea;

    public FormatMenu(JTextArea textArea) {
        super("Format");
        this.mTextArea = textArea;
        createFormatMenu();
    }

    private void createFormatMenu() {
        JMenu alignTextMenu = new JMenu("Align");

        JMenuItem alignTextLeftMenuItem = new JMenuItem("Left");
        alignTextLeftMenuItem.addActionListener(_ -> alignTextLeft());

        JMenuItem alignTextRightMenuItem = new JMenuItem("Right");
        alignTextRightMenuItem.addActionListener(_ -> alignTextRight());

        JMenuItem fontMenuItem = new JMenuItem("Font...");
        fontMenuItem.addActionListener(_ -> fontEditor());

        alignTextMenu.add(alignTextLeftMenuItem);
        alignTextMenu.add(alignTextRightMenuItem);
        add(alignTextMenu);
        add(fontMenuItem);
    }

    private void alignTextLeft() {
        mTextArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    }

    private void alignTextRight() {
        mTextArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    }

    private void fontEditor() {
        new FontMenu(this).setVisible(true);
    }

    public JTextArea getmTextArea() {
        return mTextArea;
    }
}
