package com.notepad.gui.menuItems;

import com.notepad.main.StatusBar;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class OtherMenu extends JMenu {
    private static final int DISTRACTION_FONT_BOOST = 4;

    private final JFrame mFrame;
    private boolean mDistractionFreeMode = false;
    private JMenuItem mDistractionMenuItem;
    private final StatusBar mStatusBar;
    private int mPreviousExtendedState = JFrame.NORMAL;
    private final JTextArea mTextArea;
    private Font mPreviousFont;

    public OtherMenu(JFrame frame, StatusBar statusBar, JTextArea textArea) {
        super("Other");
        this.mFrame = frame;
        this.mStatusBar = statusBar;
        this.mTextArea = textArea;
        addOtherMenu();
        bindKeys();
    }

    private void addOtherMenu() {
        mDistractionMenuItem = new JMenuItem("Enter Distraction-Free Mode");
        mDistractionMenuItem.addActionListener(_ -> setDistractionFree());
        add(mDistractionMenuItem);
    }

    private void setDistractionFree() {
        mDistractionFreeMode = !mDistractionFreeMode;

        mFrame.getJMenuBar().setVisible(!mDistractionFreeMode);

        if (mDistractionFreeMode) {
            mPreviousExtendedState = mFrame.getExtendedState();
            mPreviousFont = mTextArea.getFont();
        }

        mFrame.setExtendedState(mDistractionFreeMode ? mPreviousExtendedState | JFrame.MAXIMIZED_BOTH : mPreviousExtendedState);

        mStatusBar.setVisible(!mDistractionFreeMode);

        mDistractionMenuItem.setText(mDistractionFreeMode ? "Exit Distraction-Free Mode" : "Enter Distraction-Free Mode");

        mTextArea.setBorder(mDistractionFreeMode ?
                BorderFactory.createEmptyBorder(20, 200, 20, 200) :
                BorderFactory.createEmptyBorder());

        mTextArea.setFont(mDistractionFreeMode ? mPreviousFont.deriveFont((float) (mPreviousFont.getSize() + DISTRACTION_FONT_BOOST)) : mPreviousFont
        );

        mFrame.revalidate();
        mFrame.repaint();
    }

    private void bindKey(KeyStroke keyStroke, String actionName, Runnable action) {
        var root = mFrame.getRootPane();

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(keyStroke, actionName);

        root.getActionMap().put(actionName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    private void bindKeys() {
        bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "toggleDistractionFree", this::setDistractionFree);

        bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitDistraction",
                () -> {
                    if (mDistractionFreeMode)
                        setDistractionFree();
                });
    }
}
