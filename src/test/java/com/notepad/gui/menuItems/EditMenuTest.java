package com.notepad.gui.menuItems;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EditMenuTest {

    private JFrame mFrame;
    private JTextArea mTextArea;
    private EditMenu mEditMenu;

    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            mFrame = new JFrame();
            mTextArea = new JTextArea();
            mEditMenu = new EditMenu(mFrame, mTextArea);

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
    @DisplayName("Test 1: Should Create Edit Menu With Correct Text")
    void test1_shouldCreateEditMenuWithCorrectText() {
        assertEquals("Edit", mEditMenu.getText());
    }

    @Test
    @DisplayName("Test 2: Should Create Expected Menu Items In Order")
    void test2_shouldCreateExpectedMenuItemsInOrder() {
        assertEquals("Cut", mEditMenu.getItem(0).getText());
        assertEquals("Copy", mEditMenu.getItem(1).getText());
        assertEquals("Paste", mEditMenu.getItem(2).getText());
        assertEquals("Undo", mEditMenu.getItem(3).getText());
        assertEquals("Redo", mEditMenu.getItem(4).getText());
        assertEquals("Find", mEditMenu.getItem(5).getText());
        assertEquals("Find & Replace", mEditMenu.getItem(6).getText());
    }

    @Test
    @DisplayName("Test 3: Should Set Correct Keyboard Shortcuts")
    void test3_shouldSetCorrectKeyboardShortcuts() {
        assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK), mEditMenu.getItem(0).getAccelerator());
        assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), mEditMenu.getItem(1).getAccelerator());
        assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK), mEditMenu.getItem(2).getAccelerator());
        assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), mEditMenu.getItem(3).getAccelerator());
        assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK), mEditMenu.getItem(4).getAccelerator());
        assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK), mEditMenu.getItem(5).getAccelerator());
        assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK), mEditMenu.getItem(6).getAccelerator());
    }
}
