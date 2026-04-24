package com.notepad.gui.menuItems;


import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.KeyEvent;

import java.io.File;

import com.notepad.operations.Operations;

public class FileMenu extends JMenu {

    private final JTextArea mTextArea;
    private final JFrame mFrame;
    private final JFileChooser mFileChooser;
    private File mCurrentFile;
    private final Operations mOperations;

    public FileMenu(JFrame frame, JTextArea textArea) {
        super("File");
        this.mFrame = frame;
        this.mTextArea = textArea;
        this.mFileChooser = new JFileChooser();
        mOperations = new Operations();

        this.mFileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        this.mFileChooser.setCurrentDirectory(new File("src/assets"));

        createFileMenu();
    }

    private void createFileMenu() {
        JMenu newMenu = new JMenu("New");

        JMenuItem newNotepadMenuItem = new JMenuItem("New Note");
        newNotepadMenuItem.addActionListener(_ -> mCurrentFile = mOperations.newFile(mFrame, mTextArea, mCurrentFile));
        newNotepadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem newCodeEditorMenuItem = new JMenuItem("New Code Editor");
        newCodeEditorMenuItem.addActionListener(_ -> mOperations.newCodeEditor());

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(_ -> mCurrentFile = mOperations.openFile(mFrame, mTextArea, mFileChooser, mCurrentFile));
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(_ -> mCurrentFile = mOperations.saveFile(mFrame, mTextArea, mFileChooser, mCurrentFile));
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(_ -> mCurrentFile = mOperations.saveAs(mFrame, mTextArea, mFileChooser));
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
