package com.notepad.gui.menuItems;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.KeyEvent;
import java.io.*;
import com.notepad.operations.Operations;

public class FileMenu extends JMenu {

    private final JTextArea textArea;
    private final JFrame frame;
    private final JFileChooser fileChooser;
    private File currentFile;
    private Operations operations;

    public FileMenu(JFrame frame, JTextArea textArea) {
        super("File");
        this.frame = frame;
        this.textArea = textArea;
        this.fileChooser = new JFileChooser();
        operations = new Operations();

        this.fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        this.fileChooser.setCurrentDirectory(new File("src/assets"));

        createFileMenu();
    }

    private void createFileMenu() {
        JMenu newMenu = new JMenu("New");

        JMenuItem newNotepadMenuItem = new JMenuItem("New Note");
        newNotepadMenuItem.addActionListener(_ -> operations.newFile(frame, textArea, currentFile));
        newNotepadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem newCodeEditorMenuItem = new JMenuItem("New Code Editor");
        newCodeEditorMenuItem.addActionListener(_ -> operations.newCodeEditor());

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(_ -> operations.openFile(frame, textArea, fileChooser, currentFile));
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(_ -> operations.saveFile(frame, textArea, fileChooser, currentFile));
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(_ -> operations.saveAs(frame, textArea, fileChooser, currentFile));
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(_ -> operations.exit(frame));
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