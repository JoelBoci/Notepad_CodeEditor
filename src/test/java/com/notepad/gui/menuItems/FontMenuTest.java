package com.notepad.gui.menuItems;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.Font;

import static org.junit.jupiter.api.Assertions.*;

class FontMenuTest {

    private JTextArea mTextArea;
    private FormatMenu mFormatMenu;
    private FontMenu mFontMenu;

    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            mTextArea = new JTextArea();
            mTextArea.setFont(new Font("Dialog", Font.PLAIN, 16));

            mFormatMenu = new FormatMenu(mTextArea);
            mFontMenu = new FontMenu(mFormatMenu);
        });
    }

    @Test
    @DisplayName("Test 1: Should Create Font Menu With Correct Title")
    void test1_shouldCreateFontMenuWithCorrectTitle() {
        assertEquals("Font Settings", mFontMenu.getTitle());
    }

    @Test
    @DisplayName("Test 2: Should Be Modal")
    void test2_shouldBeModal() {
        assertTrue(mFontMenu.isModal());
    }

    @Test
    @DisplayName("Test 3: Should Not Be Resizable")
    void test3_shouldNotBeResizable() {
        assertFalse(mFontMenu.isResizable());
    }

    @Test
    @DisplayName("Test 4: Should Use Dispose On Close")
    void test4_shouldUseDisposeOnClose() {
        assertEquals(FontMenu.DISPOSE_ON_CLOSE, mFontMenu.getDefaultCloseOperation());
    }

    @Test
    @DisplayName("Test 5: Should Initialise Fields From Text Area Font")
    void test5_shouldInitialiseFieldsFromTextAreaFont() {
        assertEquals(mTextArea.getFont().getFontName(), mFontMenu.getMCurrentFontField().getText());
        assertEquals("Plain", mFontMenu.getMCurrentFontStyleField().getText());
        assertEquals("16", mFontMenu.getMCurrentFontSizeField().getText());
        assertEquals(mTextArea.getForeground(), mFontMenu.getMCurrentColourBox().getBackground());
    }
}
