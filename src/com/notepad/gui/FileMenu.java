package com.notepad.gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import com.notepad.gui.operations.Operations;

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

        JMenuItem newCodeEditorMenuItem = new JMenuItem("New Code Editor");
        newCodeEditorMenuItem.addActionListener(_ -> operations.newCodeEditor());

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(_ -> operations.openFile(frame, textArea, fileChooser, currentFile));

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(_ -> operations.saveFile(frame, textArea, fileChooser, currentFile));

        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(_ -> operations.saveAs(frame, textArea, fileChooser, currentFile));

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(_ -> operations.exit(frame));

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