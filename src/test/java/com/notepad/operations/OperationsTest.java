package com.notepad.operations;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.undo.UndoManager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class OperationsTest {

    private static JFrame mFrame;
    private static JTextArea mTextArea;
    private static Operations mOperations;

    private static UndoManager mUndoManager;

    @BeforeAll
    static void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            mFrame = new JFrame();
            mTextArea = new JTextArea();
            mOperations = new Operations();
            mUndoManager = new UndoManager();
            mTextArea.getDocument().addUndoableEditListener(e -> mUndoManager.addEdit(e.getEdit()));

            mFrame.add(mTextArea);
            mFrame.pack();
        });
    }

    @AfterEach
    void tearDown() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            if (mFrame != null)
                mFrame.dispose();
        });
    }

    @Test
    @DisplayName("Test 1: Cut and Paste Operations")
    void test1_cutAndPasteOperations() {
        mTextArea.setText("test");
        assertEquals("test", mTextArea.getText());

        mTextArea.selectAll();
        assertEquals("test", mTextArea.getSelectedText());

        mOperations.cut(mTextArea);
        assertEquals("", mTextArea.getText());

        mOperations.paste(mTextArea);
        assertEquals("test", mTextArea.getText());
    }

    @Test
    @DisplayName("Test 2: Copy and Paste Operations")
    void test2_copyAndPasteOperations() {
        mTextArea.setText("test");
        assertEquals("test", mTextArea.getText());

        mTextArea.select(2, 4);
        assertEquals("st", mTextArea.getSelectedText());

        mOperations.copy(mTextArea);
        assertEquals("test", mTextArea.getText());

        mTextArea.setCaretPosition(mTextArea.getText().length());

        mOperations.paste(mTextArea);

        assertEquals("testst", mTextArea.getText());
    }

    @Test
    @DisplayName("Test 3: Undo and Redo Operation")
    void test3_undoAndRedoOperations() {
        mTextArea.setText("te");
        assertEquals("te", mTextArea.getText());

        mTextArea.append("st");
        assertEquals("test", mTextArea.getText());

        mOperations.undo(mUndoManager);
        assertEquals("te", mTextArea.getText());

        mOperations.redo(mUndoManager);
        assertEquals("test", mTextArea.getText());
    }

    @Test
    @DisplayName("Test 4: New File Should Clear Current Window When This Window Is Selected")
    void test4_newFileShouldClearCurrentWindowWhenThisWindowIsSelected() {
        File currentFile = new File("existing.boc");

        mFrame.setTitle("existing.boc");
        mTextArea.setText("test");

        File result = mOperations.handleNewFileOption(mFrame, mTextArea, currentFile, 1);

        assertNull(result);
        assertEquals("Untitled - Notepad", mFrame.getTitle());
        assertEquals("", mTextArea.getText());
        assertEquals("UTF-8", mTextArea.getClientProperty("encoding"));
        assertEquals("LF", mTextArea.getClientProperty("eol"));
    }

    @Test
    @DisplayName("Test 5: New File Should Keep Current File When Cancelled")
    void test5_newFileShouldKeepCurrentFileWhenCancelled() {
        File currentFile = new File("existing.boc");
        mTextArea.setText("test");
        assertEquals("test", mTextArea.getText());

        File result = mOperations.handleNewFileOption(mFrame, mTextArea, currentFile, 2);

        assertSame(currentFile, result);
        assertEquals("test", mTextArea.getText());
    }

    @Test
    @DisplayName("Test 6: Open File Should Replace Content In Current File")
    void test6_openFileShouldReplaceContentInCurrentFile() throws IOException {
        File currentFile = new File("existing.boc");

        Path tempFile = Files.createTempFile("opened-file", ".boc");
        Files.writeString(tempFile, "new file content", StandardCharsets.UTF_8);

        mTextArea.setText("test");
        assertEquals("test", mTextArea.getText());

        JFileChooser fileChooser = new TestFileChooser(
                tempFile.toFile(),
                JFileChooser.APPROVE_OPTION
        );

        File openedFile = mOperations.openFile(mFrame, mTextArea, fileChooser, currentFile);

        assertEquals(tempFile.toFile(), openedFile);
        assertEquals("new file content", mTextArea.getText());
        assertEquals(tempFile.getFileName().toString(), mFrame.getTitle());
        assertEquals("UTF-8", mTextArea.getClientProperty("encoding"));
        assertEquals("LF", mTextArea.getClientProperty("eol"));
    }

    @Test
    @DisplayName("Test 7: Open File Should Keep Current File When Chooser Is Cancelled")
    void test7_openFileShouldKeepCurrentFileWhenChooserIsCancelled() {
        File currentFile = new File("existing.boc");

        mTextArea.setText("original text");

        JFileChooser fileChooser = new TestFileChooser(
                null,
                JFileChooser.CANCEL_OPTION
        );

        File result = mOperations.openFile(mFrame, mTextArea, fileChooser, currentFile);

        assertEquals(currentFile, result);
        assertEquals("original text", mTextArea.getText());
    }

    private static class TestFileChooser extends JFileChooser {

        private final File mSelectedFile;
        private final int mResult;

        TestFileChooser(File selectedFile, int result) {
            this.mSelectedFile = selectedFile;
            this.mResult = result;
        }

        @Override
        public int showOpenDialog(java.awt.Component parent) {
            return mResult;
        }

        @Override
        public File getSelectedFile() {
            return mSelectedFile;
        }
    }
}
