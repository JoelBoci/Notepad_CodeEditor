package com.notepad.gui.menuItems;


import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import java.awt.event.KeyEvent;

import java.io.File;

import com.notepad.app.StatusBar;
import com.notepad.operations.Operations;

public class FileMenu extends JMenu {

    private final JTextArea mTextArea;
    private final JFrame mFrame;
    private final JFileChooser mFileChooser;
    private File mCurrentFile;
    private final Operations mOperations;
    private final StatusBar mStatusBar;

    private boolean mDirty = false;
    private static final int AUTO_SAVE_INTERVAL_MS = 60000; // 1 minute

    public FileMenu(JFrame frame, JTextArea textArea, StatusBar statusBar) {
        super("File");
        mFrame = frame;
        mTextArea = textArea;
        mFileChooser = new JFileChooser();
        mOperations = new Operations();
        mStatusBar = statusBar;

        mFileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        mFileChooser.setCurrentDirectory(new File("src/assets"));

        mTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                mDirty = true;
                mStatusBar.showUnsaved();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                mDirty = true;
                mStatusBar.showUnsaved();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                mDirty = true;
                mStatusBar.showUnsaved();
            }
        });

        Timer autoSaveTimer = new Timer(AUTO_SAVE_INTERVAL_MS, _ -> {
            if (mCurrentFile != null && mDirty) {
                mStatusBar.showSaving();

                File savedFile = mOperations.saveFile(mFrame, mTextArea, mFileChooser, mCurrentFile);
                if (savedFile != null) {
                    mCurrentFile = savedFile;
                    mDirty = false;
                    mStatusBar.showSaved();
                } else {
                    mStatusBar.showUnsaved();
                }
            }
        });
        autoSaveTimer.start();

        createFileMenu();
    }

    private void createFileMenu() {
        JMenu newMenu = new JMenu("New");

        JMenuItem newNotepadMenuItem = new JMenuItem("New Note");
        newNotepadMenuItem.addActionListener(_ -> {
            mCurrentFile = mOperations.newFile(mFrame, mTextArea, mCurrentFile);
            mDirty = false;
            mStatusBar.showSaved();
        });
        newNotepadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem newCodeEditorMenuItem = new JMenuItem("New Code Editor");
        newCodeEditorMenuItem.addActionListener(_ -> mOperations.newCodeEditor());

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(_ -> {
            File openedFile = mOperations.openFile(mFrame, mTextArea, mFileChooser, mCurrentFile);

            if (openedFile != null) {
                mCurrentFile = openedFile;
                mDirty = false;
                mStatusBar.showSaved();
            }
        });
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(_ -> {
            mStatusBar.showSaving();

            File savedFile = mOperations.saveFile(mFrame, mTextArea, mFileChooser, mCurrentFile);
            if (savedFile != null) {
                mCurrentFile = savedFile;
                mDirty = false;
                mStatusBar.showSaved();
            } else {
                mStatusBar.showUnsaved();
            }
        });
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(_ -> {
            mStatusBar.showSaving();

            File savedFile = mOperations.saveAs(mFrame, mTextArea, mFileChooser);
            if (savedFile != null) {
                mCurrentFile = savedFile;
                mDirty = false;
                mStatusBar.showSaved();
            } else {
                mStatusBar.showUnsaved();
            }
        });
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(_ -> mOperations.exit(mFrame));
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));

        newMenu.add(newNotepadMenuItem);
        newMenu.add(newCodeEditorMenuItem);
        add(newMenu);
        add(openMenuItem);
        add(saveMenuItem);
        add(saveAsMenuItem);
        addSeparator();
        add(exitMenuItem);
    }
}
