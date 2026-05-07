package com.notepad.gui.menuItems;

import com.notepad.app.StatusBar;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SettingsMenuTest {

    private JFrame mFrame;
    private JTextArea mTextArea;
    private SettingsMenu mSettingsMenu;

    private OtherMenu mOtherMenu;
    private StatusBar mStatusBar;
    private JScrollPane mScrollPane;

    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            mFrame = new JFrame();
            mTextArea = new JTextArea();

            mStatusBar = new StatusBar();
            mScrollPane = new JScrollPane();
            mOtherMenu = new OtherMenu(mFrame, mStatusBar, mTextArea, mScrollPane);
            mSettingsMenu = new SettingsMenu(mFrame, mOtherMenu);

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
    @DisplayName("Test 1: Should Create Settings Menu With Correct Text")
    void test1_shouldCreateEditMenuWithCorrectText() {
        assertEquals("Settings", mSettingsMenu.getText());
    }

    @Test
    @DisplayName("Test 2: Should Create Expected Menu Items In Order")
    void test2_shouldCreateExpectedMenuItemsInOrder() {
        assertEquals("Themes", mSettingsMenu.getItem(0).getText());
    }

    @Test
    @DisplayName("Test 3: Should Create Expected New Submenu Items")
    void test3_shouldCreateExpectedNewSubmenuItems() {
        JMenu settingsMenu = (JMenu) mSettingsMenu.getItem(0);

        assertEquals("Themes", settingsMenu.getText());
        assertEquals(5, settingsMenu.getItemCount());

        assertEquals("Light", settingsMenu.getItem(0).getText());
        assertEquals("Dark", settingsMenu.getItem(1).getText());
        assertEquals("Darcula", settingsMenu.getItem(2).getText());
        assertEquals("Mac Light", settingsMenu.getItem(3).getText());
        assertEquals("Mac Dark", settingsMenu.getItem(4).getText());
    }
}
