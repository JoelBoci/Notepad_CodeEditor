package com.notepad.gui.menuItems;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.ComponentOrientation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class FormatMenuTest {

    private JTextArea mTextArea;
    private FormatMenu mFormatMenu;

    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            mTextArea = new JTextArea();
            mFormatMenu = new FormatMenu(mTextArea);
        });
    }

    @Test
    @DisplayName("Test 1: Should Create Format Menu With Correct Text")
    void test1_shouldCreateFormatMenuWithCorrectText() {
        assertEquals("Format", mFormatMenu.getText());
    }

    @Test
    @DisplayName("Test 2: Should Create Expected Top-Level Menu Items")
    void test2_shouldCreateExpectedTopLevelMenuItems() {
        assertEquals(2, mFormatMenu.getItemCount());

        assertInstanceOf(JMenu.class, mFormatMenu.getItem(0));
        assertEquals("Align", mFormatMenu.getItem(0).getText());

        assertEquals("Font...", mFormatMenu.getItem(1).getText());
    }

    @Test
    @DisplayName("Test 3: Should Create Expected Align Submenu Items")
    void test3_shouldCreateExpectedAlignSubmenuItems() {
        JMenu alignMenu = (JMenu) mFormatMenu.getItem(0);

        assertEquals(2, alignMenu.getItemCount());
        assertEquals("Left", alignMenu.getItem(0).getText());
        assertEquals("Right", alignMenu.getItem(1).getText());
    }

    @Test
    @DisplayName("Test 4: Left Align Should Set Text Area Left To Right")
    void test4_leftAlignShouldSetTextAreaLeftToRight() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JMenu alignMenu = (JMenu) mFormatMenu.getItem(0);
            JMenuItem leftMenuItem = alignMenu.getItem(0);

            mTextArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

            leftMenuItem.doClick();

            assertEquals(ComponentOrientation.LEFT_TO_RIGHT, mTextArea.getComponentOrientation());
        });
    }

    @Test
    @DisplayName("Test 5: Right Align Should Set Text Area Right To Left")
    void test5_rightAlignShouldSetTextAreaRightToLeft() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JMenu alignMenu = (JMenu) mFormatMenu.getItem(0);
            JMenuItem rightMenuItem = alignMenu.getItem(1);

            rightMenuItem.doClick();

            assertEquals(ComponentOrientation.RIGHT_TO_LEFT, mTextArea.getComponentOrientation());
        });
    }

    @Test
    @DisplayName("Test 6: Font Menu Item Should Have Action Listener")
    void test6_fontMenuItemShouldHaveActionListener() {
        JMenuItem fontMenuItem = mFormatMenu.getItem(1);

        assertEquals(1, fontMenuItem.getActionListeners().length);
    }
}
