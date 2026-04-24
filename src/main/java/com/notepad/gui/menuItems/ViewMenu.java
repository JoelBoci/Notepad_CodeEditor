package com.notepad.gui.menuItems;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import java.awt.Font;
import java.awt.event.KeyEvent;

import java.util.function.IntConsumer;

public class ViewMenu extends JMenu {

    private final JTextArea mTextArea;
    private final IntConsumer mOnZoomChanged;
    private final Font mBaseFont;

    private int mZoomPercent = 100;
    private final int DEFAULT_ZOOM = 100;
    private final int MIN_ZOOM = 50;
    private final int MAX_ZOOM = 300;

    JMenuItem mZoomInMenuItem = new JMenuItem("Zoom In");
    JMenuItem mZoomOutMenuItem = new JMenuItem("Zoom Out");
    JMenuItem mRestoreDefaultZoomMenuItem = new JMenuItem("Restore Default Zoom");

    public ViewMenu(JTextArea textArea, IntConsumer onZoomChanged) {
        super("View");
        this.mTextArea = textArea;
        this.mOnZoomChanged = onZoomChanged;
        this.mBaseFont = textArea.getFont();
        createViewMenu();
        applyZoom(DEFAULT_ZOOM);
    }

    private void createViewMenu() {
        mZoomInMenuItem.addActionListener(_ -> applyZoom(mZoomPercent + 10));
        mZoomInMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.CTRL_DOWN_MASK));

        mZoomOutMenuItem.addActionListener(_ -> applyZoom(mZoomPercent - 10));
        mZoomOutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, KeyEvent.CTRL_DOWN_MASK));

        mRestoreDefaultZoomMenuItem.addActionListener(_ -> applyZoom(DEFAULT_ZOOM));

        add(mZoomInMenuItem);
        add(mZoomOutMenuItem);
        add(mRestoreDefaultZoomMenuItem);
    }

    private void applyZoom(int newPercent) {
        newPercent = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, newPercent));
        if (newPercent == mZoomPercent) {
            updateMenuItemStates();
            return;
        }
        mZoomPercent = newPercent;

        int newSize = Math.max(8, Math.round(mBaseFont.getSize2D() * (mZoomPercent / 100f)));
        Font scaled = mBaseFont.deriveFont((float) newSize);
        mTextArea.setFont(scaled);

        updateMenuItemStates();

        if (mOnZoomChanged != null) {
            mOnZoomChanged.accept(mZoomPercent);
        }
    }

    private void updateMenuItemStates() {
        if (mZoomInMenuItem != null)
            mZoomInMenuItem.setEnabled(mZoomPercent < MAX_ZOOM);

        if (mZoomOutMenuItem != null)
            mZoomOutMenuItem.setEnabled(mZoomPercent > MIN_ZOOM);

        if (mRestoreDefaultZoomMenuItem != null)
            mRestoreDefaultZoomMenuItem.setEnabled(mZoomPercent != 100);
    }
}
