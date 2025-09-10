package com.notepad.gui.menuItems;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import java.awt.Font;
import java.awt.event.KeyEvent;

import java.util.function.IntConsumer;

public class ViewMenu extends JMenu {

    private final JTextArea textArea;
    private final IntConsumer onZoomChanged;
    private final Font baseFont;

    private int zoomPercent = 100;
    private final int DEFAULT_ZOOM = 100;
    private final int MIN_ZOOM = 50;
    private final int MAX_ZOOM = 300;

    JMenuItem zoomInMenuItem = new JMenuItem("Zoom In");
    JMenuItem zoomOutMenuItem = new JMenuItem("Zoom Out");
    JMenuItem restoreDefaultZoomMenuItem = new JMenuItem("Restore Default Zoom");

    public ViewMenu(JTextArea textArea, IntConsumer onZoomChanged) {
        super("View");
        this.textArea = textArea;
        this.onZoomChanged = onZoomChanged;
        this.baseFont = textArea.getFont();
        createViewMenu();
        applyZoom(DEFAULT_ZOOM);
    }

    private void createViewMenu() {
        zoomInMenuItem.addActionListener(_ -> applyZoom(zoomPercent + 10));
        zoomInMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.CTRL_DOWN_MASK));

        zoomOutMenuItem.addActionListener(_ -> applyZoom(zoomPercent - 10));
        zoomOutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, KeyEvent.CTRL_DOWN_MASK));

        restoreDefaultZoomMenuItem.addActionListener(_ -> applyZoom(DEFAULT_ZOOM));

        add(zoomInMenuItem);
        add(zoomOutMenuItem);
        add(restoreDefaultZoomMenuItem);
    }

    private void applyZoom(int newPercent) {
        newPercent = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, newPercent));
        if (newPercent == zoomPercent) {
            updateMenuItemStates();
            return;
        }
        zoomPercent = newPercent;

        int newSize = Math.max(8, Math.round(baseFont.getSize2D() * (zoomPercent / 100f)));
        Font scaled = baseFont.deriveFont((float) newSize);
        textArea.setFont(scaled);

        updateMenuItemStates();

        if (onZoomChanged != null) {
            onZoomChanged.accept(zoomPercent);
        }
    }

    private void updateMenuItemStates() {
        if (zoomInMenuItem != null)
            zoomInMenuItem.setEnabled(zoomPercent < MAX_ZOOM);

        if (zoomOutMenuItem != null)
            zoomOutMenuItem.setEnabled(zoomPercent > MIN_ZOOM);

        if (restoreDefaultZoomMenuItem != null)
            restoreDefaultZoomMenuItem.setEnabled(zoomPercent != 100);
    }
}
