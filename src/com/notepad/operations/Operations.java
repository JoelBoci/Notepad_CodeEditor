package com.notepad.operations;

import com.notepad.gui.CodeEditor;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.undo.UndoManager;

import com.notepad.main.Notepad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Operations {

    private static final Logger logger = LoggerFactory.getLogger(Operations.class);

    // File Operations
    public void newFile(JFrame frame, JTextArea textArea, File currentFile) {
        String[] options = {"New Window", "This Window", "Cancel"};
        String message = "Where would you like to open the new note?";
        String title = "Select One:";

        int option = JOptionPane.showOptionDialog(frame, message, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]
        );

        switch (option) {
            case 0 -> {
                SwingUtilities.invokeLater(Notepad::new);
                logger.info("New file created in new window");
            }
            case 1 -> {
                frame.setTitle("Notepad");
                textArea.setText("");
                currentFile = null;
                logger.info("New file created in current window");
            }
            default -> logger.info("New file operation cancelled by user.");
        }
    }

    public void exit(JFrame frame) {
        logger.info("Quiting notepad...");
        frame.dispose();
    }

    public void newCodeEditor() {
        SwingUtilities.invokeLater(CodeEditor::new);
        logger.info("New code editor created");
    }

    public void openFile(JFrame frame, JTextArea textArea, JFileChooser fileChooser, File currentFile) {
        int option = fileChooser.showOpenDialog(frame);
        if (option != JFileChooser.APPROVE_OPTION) return;

        try {
            // Get the selected file
            File selectedFile = fileChooser.getSelectedFile();

            logger.info("Attempting to open file '{}'", selectedFile);

            // Update the current file
            currentFile = selectedFile;

            // Update the title header
            frame.setTitle(selectedFile.getName());

            // Read the file and store the text
            BufferedReader bufferedReader = new BufferedReader(new FileReader(selectedFile));
            StringBuilder fileText = new StringBuilder();
            String readText;

            while ((readText = bufferedReader.readLine()) != null)
                fileText.append(readText).append("\n");

            // Update text area GUI
            textArea.setText(fileText.toString());
            logger.info("Opened file: '{}'", selectedFile);
        } catch (IOException e) {
            logger.error("Error opening file: {}", e.getMessage(), e);
        }
    }

    public void saveFile(JFrame frame, JTextArea textArea, JFileChooser fileChooser, File currentFile) {
        // If the current file is null then we have to perform save as functionality
        if (currentFile == null) saveAs(frame, textArea, fileChooser, currentFile);

        // If the user chooses to cancel saving the file this means that current file will still
        // be null, then we want to prevent executing the rest of the cod
        if (currentFile == null) return;

        try {
            // Write to the current file
            logger.info("Attempting to save file...");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(currentFile));
            bufferedWriter.write(textArea.getText());
            bufferedWriter.close();
            logger.info("File successfully saved :)");
        } catch (IOException e) {
            logger.error("Error saving file: {}", e.getMessage(), e);
        }
    }

    // the saveAs method creates a new text file and saves user text
    public void saveAs(JFrame frame, JTextArea textArea, JFileChooser fileChooser, File currentFile) {
        int option = fileChooser.showSaveDialog(frame);

        if (option != JFileChooser.APPROVE_OPTION) return;

        try {
            File selectedFile = fileChooser.getSelectedFile();

            // Need to append .txt to the file if it does not have the txt extension yet
            String fileName = selectedFile.getName();
            logger.info("Attempting to save file '{}'", fileName);
            if (!fileName.substring(fileName.length() - 4).equalsIgnoreCase(".txt"))
                selectedFile = new File(selectedFile.getAbsoluteFile() + ".txt");

            // Create new file
            selectedFile.createNewFile();

            // Write the user's text into the file we just created
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(selectedFile));
            bufferedWriter.write(textArea.getText());
            bufferedWriter.close();

            // Update the title header of the GUI to the saved text file name
            frame.setTitle(fileName);

            // Update the current file
            currentFile = selectedFile;

            // Show display dialog
            JOptionPane.showMessageDialog(frame, "Saved file " + fileName);
            logger.info("'{}' has been saved :)", fileName);
        } catch (Exception e) {
            logger.error("Error saving file as: {}", e.getMessage(), e);
        }
    }

    // Text operations
    public void cut(JTextArea textArea) {
        textArea.cut();
    }

    public void copy(JTextArea textArea) {
        textArea.copy();
    }

    public void paste(JTextArea textArea) {
        textArea.paste();
    }

    public void undo(UndoManager undoManager) {
        if (undoManager.canUndo())
            undoManager.undo();
    }

    public void redo(UndoManager undoManager) {
        if (undoManager.canRedo())
            undoManager.redo();
    }
}
