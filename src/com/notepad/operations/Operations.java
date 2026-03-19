package com.notepad.operations;

import com.notepad.gui.CodeEditor;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.undo.UndoManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.notepad.main.Notepad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Operations {

    private static final FileNameExtensionFilter OPEN_FILE_FILTER =
            new FileNameExtensionFilter("BOC and Text Files (*.boc, *.txt)", "boc", "txt");

    private static final FileNameExtensionFilter SAVE_FILE_FILTER =
            new FileNameExtensionFilter("BOC Files (*.boc)", "boc");

    private static final Logger mLogger = LoggerFactory.getLogger(Operations.class);

    public File newFile(JFrame frame, JTextArea textArea, File currentFile) {
        String[] options = {"New Window", "This Window", "Cancel"};

        int option = JOptionPane.showOptionDialog(frame, "Where would you like to open the new note?",
                "Select One:", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        switch (option) {
            case 0 -> {
                SwingUtilities.invokeLater(Notepad::new);
                mLogger.info("New file created in new window");
                return currentFile;
            }
            case 1 -> {
                frame.setTitle("Untitled - Notepad");
                textArea.setText("");
                textArea.putClientProperty("encoding", StandardCharsets.UTF_8.displayName());
                textArea.putClientProperty("eol", "LF");
                mLogger.info("New file created in current window");
                return null;
            }
            default -> {
                mLogger.info("New file operation cancelled");
                return currentFile;
            }
        }
    }

    public void exit(JFrame frame) {
        mLogger.info("Quitting notepad...");
        frame.dispose();
    }

    public void newCodeEditor() {
        SwingUtilities.invokeLater(CodeEditor::new);
        mLogger.info("New code editor created");
    }

    public File openFile(JFrame frame, JTextArea textArea, JFileChooser fileChooser, File currentFile) {
        fileChooser.setFileFilter(OPEN_FILE_FILTER);

        if (fileChooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION)
            return currentFile;

        try {
            File selectedFile = fileChooser.getSelectedFile();
            mLogger.info("Attempting to open file '{}'", selectedFile.getName());

            Path path = selectedFile.toPath();
            Charset charset = sniffCharset(path);

            String text = Files.readString(path, charset);
            if (!text.isEmpty() && text.charAt(0) == '\uFEFF')
                text = text.substring(1);

            textArea.putClientProperty("encoding", charset.displayName());
            textArea.putClientProperty("eol", sniffEol(text));
            textArea.setText(text);

            frame.setTitle(selectedFile.getName());

            mLogger.info("Opened file: '{}'", selectedFile.getName());
            return selectedFile;
        } catch (IOException e) {
            mLogger.error("Error opening file: {}", e.getMessage(), e);
            JOptionPane.showMessageDialog(frame, "Error opening file: " + e.getMessage());
            return currentFile;
        }
    }

    public File saveFile(JFrame frame, JTextArea textArea, JFileChooser fileChooser, File currentFile) {
        if (currentFile == null)
            return saveAs(frame, textArea, fileChooser);

        try {
            mLogger.info("Attempting to save file '{}'...", currentFile.getName());

            writeTextToFile(currentFile, textArea.getText());

            textArea.putClientProperty("encoding", StandardCharsets.UTF_8.displayName());
            frame.setTitle(currentFile.getName());
            mLogger.info("File '{}' successfully saved :)", currentFile.getName());

            return currentFile;
        } catch (IOException e) {
            mLogger.error("Error saving file: {}", e.getMessage(), e);
            JOptionPane.showMessageDialog(frame, "Error saving file: " + e.getMessage());
            return currentFile;
        }
    }

    public File saveAs(JFrame frame, JTextArea textArea, JFileChooser fileChooser) {
        fileChooser.setSelectedFile(null);
        fileChooser.setFileFilter(SAVE_FILE_FILTER);

        if (fileChooser.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION)
            return null;

        try {
            File selectedFile = ensureBocExtension(fileChooser.getSelectedFile());
            String fileName = selectedFile.getName();

            mLogger.info("Attempting to save file '{}'", fileName);

            writeTextToFile(selectedFile, textArea.getText());

            textArea.putClientProperty("encoding", StandardCharsets.UTF_8.displayName());
            frame.setTitle(fileName);

            JOptionPane.showMessageDialog(frame, "Saved file " + fileName);
            mLogger.info("'{}' has been saved :)", fileName);

            return selectedFile;
        } catch (IOException e) {
            mLogger.error("Error saving file as: {}", e.getMessage(), e);
            JOptionPane.showMessageDialog(frame, "Error saving file: " + e.getMessage());
            return null;
        }
    }

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

    private static Charset sniffCharset(Path path) throws IOException {
        try (var in = Files.newInputStream(path)) {
            byte[] bom = new byte[3];
            int n = in.read(bom);

            if (n >= 3 && (bom[0] & 0xFF) == 0xEF && (bom[1] & 0xFF) == 0xBB && (bom[2] & 0xFF) == 0xBF)
                return StandardCharsets.UTF_8;

            if (n >= 2) {
                if ((bom[0] & 0xFF) == 0xFF && (bom[1] & 0xFF) == 0xFE)
                    return StandardCharsets.UTF_16LE;

                if ((bom[0] & 0xFF) == 0xFE && (bom[1] & 0xFF) == 0xFF)
                    return StandardCharsets.UTF_16BE;
            }
        }
        return StandardCharsets.UTF_8;
    }

    private static String sniffEol(String text) {
        if (text.contains("\r\n"))
            return "CRLF";

        if (text.contains("\r"))
            return "CR";

        return "LF";
    }

    private static void writeTextToFile(File file, String text) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            writer.write(text);
        }
    }

    private static File ensureBocExtension(File file) {
        if (!file.getName().toLowerCase(java.util.Locale.ROOT).endsWith(".boc"))
            return new File(file.getAbsolutePath() + ".boc");

        return file;
    }
}
