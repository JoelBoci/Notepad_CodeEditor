package com.notepad.gui.menuItems;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import java.awt.Font;
import java.awt.event.KeyEvent;

public class ViewMenu extends JMenu {

    private final JTextArea textArea;

    public ViewMenu(JTextArea textArea) {
        super("View");
        this.textArea = textArea;
        createViewMenu();
    }

    private void createViewMenu() {
        JMenuItem zoomInMenuItem = new JMenuItem("Zoom In");
        zoomInMenuItem.addActionListener(_ -> zoomIn());
        zoomInMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem zoomOutMenuItem = new JMenuItem("Zoom Out");
        zoomOutMenuItem.addActionListener(_ -> zoomOut());
        zoomOutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));

        JMenuItem restoreDefaultZoomMenuItem = new JMenuItem("Restore Default Zoom");
        restoreDefaultZoomMenuItem.addActionListener(_ -> defaultZoom());

        add(zoomInMenuItem);
        add(zoomOutMenuItem);
        add(restoreDefaultZoomMenuItem);
    }

    private void zoomIn() {
        Font currentFont = textArea.getFont();
        textArea.setFont(new Font(
                currentFont.getName(),
                currentFont.getStyle(),
                currentFont.getSize() + 1
        ));
    }

    private void zoomOut() {
        Font currentFont = textArea.getFont();
        textArea.setFont(new Font(
                currentFont.getName(),
                currentFont.getStyle(),
                currentFont.getSize() - 1
        ));
    }

    private void defaultZoom() {
        Font currentFont = textArea.getFont();
        textArea.setFont(new Font(
                currentFont.getName(),
                currentFont.getStyle(),
                14
        ));
    }
}
