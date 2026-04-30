package com.notepad.gui.menuItems;

import com.notepad.app.StatusBar;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OtherMenuTest {

    private JFrame mFrame;
    private JTextArea mTextArea;
    private OtherMenu mOtherMenu;
    private StatusBar mStatusBar;

    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            mFrame = new JFrame();
            mTextArea = new JTextArea();
            mStatusBar = new StatusBar();
            mOtherMenu = new OtherMenu(mFrame, mStatusBar, mTextArea);

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
    @DisplayName("Test 1: Should Create Other Menu With Correct Text")
    void test1_shouldCreateEditMenuWithCorrectText() {
        assertEquals("Other", mOtherMenu.getText());
    }

    @Test
    @DisplayName("Test 2: Should Create Expected Menu Items In Order")
    void test2_shouldCreateExpectedMenuItemsInOrder() {
        assertEquals("Enter Distraction-Free Mode", mOtherMenu.getItem(0).getText());
    }
}
