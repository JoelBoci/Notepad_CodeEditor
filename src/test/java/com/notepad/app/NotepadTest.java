package com.notepad.app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class NotepadTest {

    private Notepad mNotepad;

    @BeforeEach
    void setUp() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> mNotepad = new Notepad());
    }

    @AfterEach
    void tearDown() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> {
            if (mNotepad != null && mNotepad.getMFrame() != null)
                mNotepad.getMFrame().dispose();
        });
    }

    @Test
    @DisplayName("Test 1: Should Create Frame With Correct Settings")
    void test1_shouldCreateFrameWithCorrectSettings() {
        JFrame frame = mNotepad.getMFrame();

        assertEquals("Notepad", frame.getTitle());
        assertEquals(800, frame.getWidth());
        assertEquals(600, frame.getHeight());
        assertEquals(JFrame.DISPOSE_ON_CLOSE, frame.getDefaultCloseOperation());
        assertTrue(frame.isVisible());
    }

    @Test
    @DisplayName("Test 2: Should Create Text Area With Correct Settings")
    void test2_shouldCreateTextAreaWithCorrectSettings() {
        assertNotNull(mNotepad.getMTextArea());
        assertTrue(mNotepad.getMTextArea().getLineWrap());
        assertTrue(mNotepad.getMTextArea().getWrapStyleWord());
    }

    @Test
    @DisplayName("Test 3: Should Create Status Bar")
    void test3_shouldCreateStatusBar() {
        assertNotNull(mNotepad.getMStatusBar());
    }

    @Test
    @DisplayName("Test 4: Should Create Menu Bar With Expected Menus")
    void test4_shouldCreateMenuBarWithExpectedMenus() {
        JMenuBar menuBar = mNotepad.getMFrame().getJMenuBar();

        assertNotNull(menuBar);
        assertEquals(7, menuBar.getMenuCount());

        assertEquals("File", menuBar.getMenu(0).getText());
        assertEquals("Edit", menuBar.getMenu(1).getText());
        assertEquals("Format", menuBar.getMenu(2).getText());
        assertEquals("View", menuBar.getMenu(3).getText());
        assertEquals("Settings", menuBar.getMenu(4).getText());
        assertEquals("Shortcuts", menuBar.getMenu(5).getText());
        assertEquals("Other", menuBar.getMenu(6).getText());
    }
}
