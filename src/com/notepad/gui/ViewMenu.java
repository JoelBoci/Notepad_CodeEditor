package com.notepad.gui;

import javax.swing.*;
import java.awt.*;

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

        JMenuItem zoomOutMenuItem = new JMenuItem("Zoom Out");
        zoomOutMenuItem.addActionListener(_ -> zoomOut());

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
