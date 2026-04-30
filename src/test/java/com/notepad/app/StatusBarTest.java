package com.notepad.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusBarTest {

    private StatusBar mStatusBar;
    private JTextArea mTextArea;

    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            mStatusBar = new StatusBar();
            mTextArea = new JTextArea();
            mStatusBar.bindToEditor(mTextArea);
        });

        // Allows the invokeLater(this::updateAll) inside bindToEditor to run
        SwingUtilities.invokeAndWait(() -> {});
    }

    @Test
    @DisplayName("Test 1: Should Display Default Values")
    void test1_shouldDisplayDefaultValues() {
        assertEquals("Ln 1, Col 1", mStatusBar.getMCaretLabel().getText());
        assertEquals("Words: 0  Chars: 0", mStatusBar.getMCountLabel().getText());
        assertEquals("UTF-8", mStatusBar.getMEncLabel().getText());
        assertEquals("LF", mStatusBar.getMEolLabel().getText());
        assertEquals("Zoom: 100%", mStatusBar.getMZoomLabel().getText());
    }

    @Test
    @DisplayName("Test 2: Should Update Word And Character Count")
    void test2_shouldUpdateWordAndCharacterCount() throws Exception {
        SwingUtilities.invokeAndWait(() -> mTextArea.setText("hello world"));

        assertEquals("Words: 2  Chars: 11", mStatusBar.getMCountLabel().getText());
    }

    @Test
    @DisplayName("Test 3: Should Update Caret Position")
    void test3_shouldUpdateCaretPosition() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            mTextArea.setText("hello\nworld");
            mTextArea.setCaretPosition(7);
        });

        assertEquals("Ln 2, Col 2", mStatusBar.getMCaretLabel().getText());
    }

    @Test
    @DisplayName("Test 4: Should Update Encoding Display")
    void test4_shouldUpdateEncodingDisplay() {
        mStatusBar.setEncodingDisplay("UTF-16LE");

        assertEquals("UTF-16LE", mStatusBar.getMEncLabel().getText());
    }

    @Test
    @DisplayName("Test 5: Should Update EOL Display")
    void test5_shouldUpdateEolDisplay() {
        mStatusBar.setEolDisplay("CRLF");

        assertEquals("CRLF", mStatusBar.getMEolLabel().getText());
    }

    @Test
    @DisplayName("Test 6: Should Update Zoom Percent")
    void test6_shouldUpdateZoomPercent() {
        mStatusBar.setZoomPercent(150);

        assertEquals("Zoom: 150%", mStatusBar.getMZoomLabel().getText());
    }

    @Test
    @DisplayName("Test 7: Should Read Encoding And EOL From Editor Properties")
    void test7_shouldReadEncodingAndEolFromEditorProperties() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            mTextArea.putClientProperty("encoding", "UTF-16BE");
            mTextArea.putClientProperty("eol", "CRLF");
            mTextArea.setText("abc");
        });

        assertEquals("UTF-16BE", mStatusBar.getMEncLabel().getText());
        assertEquals("CRLF", mStatusBar.getMEolLabel().getText());
    }
}
