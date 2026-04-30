package com.notepad.gui.menuItems;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShortcutMenuTest {

    private JFrame mFrame;
    private JTextArea mTextArea;
    private ShortcutMenu mShortcutMenu;

    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            mFrame = new JFrame();
            mTextArea = new JTextArea();
            mShortcutMenu = new ShortcutMenu();

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
    @DisplayName("Test 1: Should Create Shortcut Menu With Correct Text")
    void test1_shouldCreateFileMenuWithCorrectText() {
        assertEquals("Shortcuts", mShortcutMenu.getText());
    }

    @Test
    @DisplayName("Test 2: Should Create Expected Menu Items In Order")
    void test2_shouldCreateExpectedMenuItemsInOrder() {
        assertEquals("Keyboard Shortcuts", mShortcutMenu.getItem(0).getText());
    }

    @Test
    @DisplayName("Test 3: Keyboard Shortcuts Menu Item Should Have Action Listener")
    void test3_keyboardShortcutsMenuItemShouldHaveActionListener() {
        assertEquals(1, mShortcutMenu.getItem(0).getActionListeners().length);
    }
}
