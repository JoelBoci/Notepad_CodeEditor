package com.notepad.gui.menuItems;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FileMenuTest {

    private JFrame mFrame;
    private JTextArea mTextArea;
    private FileMenu mFileMenu;

    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            mFrame = new JFrame();
            mTextArea = new JTextArea();
            mFileMenu = new FileMenu(mFrame, mTextArea);

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
    @DisplayName("Test 1: Should Create File Menu With Correct Text")
    void test1_shouldCreateFileMenuWithCorrectText() {
        assertEquals("File", mFileMenu.getText());
    }

    @Test
    @DisplayName("Test 2: Should Create Expected Menu Items In Order")
    void test2_shouldCreateExpectedMenuItemsInOrder() {
        assertEquals("New", mFileMenu.getItem(0).getText());
        assertEquals("Open", mFileMenu.getItem(1).getText());
        assertEquals("Save", mFileMenu.getItem(2).getText());
        assertEquals("Save As", mFileMenu.getItem(3).getText());
        assertNull(mFileMenu.getItem(4));
        assertEquals("Exit", mFileMenu.getItem(5).getText());
    }

    @Test
    @DisplayName("Test 3: Should Create Expected New Submenu Items")
    void test3_shouldCreateExpectedNewSubmenuItems() {
        JMenu newMenu = (JMenu) mFileMenu.getItem(0);

        assertEquals("New", newMenu.getText());
        assertEquals(2, newMenu.getItemCount());

        assertEquals("New Note", newMenu.getItem(0).getText());
        assertEquals("New Code Editor", newMenu.getItem(1).getText());
    }
}
