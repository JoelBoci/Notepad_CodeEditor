package com.notepad.gui.menuItems;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.function.IntConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ViewMenuTest {

    private JFrame mFrame;
    private JTextArea mTextArea;
    private ViewMenu mViewMenu;
    private IntConsumer mOnZoomChanged;
    private int[] mLatestZoom;

    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            mFrame = new JFrame();
            mTextArea = new JTextArea();
            mTextArea.setFont(new Font("Monospaced", Font.PLAIN, 20));

            mLatestZoom = new int[1];
            mOnZoomChanged = zoom -> mLatestZoom[0] = zoom;

            mViewMenu = new ViewMenu(mTextArea, mOnZoomChanged);

            mFrame.add(mTextArea);
            mFrame.pack();
        });
    }

    @AfterEach
    void tearDown() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            mFrame = new JFrame();
            mTextArea = new JTextArea();

            // Makes font size assertions predictable
            mTextArea.setFont(new Font("Arial", Font.PLAIN, 20));

            mViewMenu = new ViewMenu(mTextArea, mOnZoomChanged);

            mFrame.add(mTextArea);
            mFrame.pack();
        });
    }

    @Test
    @DisplayName("Test 1: Should Create View Menu With Correct Text")
    void test1_shouldCreateViewMenuWithCorrectText() {
        assertEquals("View", mViewMenu.getText());
    }

    @Test
    @DisplayName("Test 2: Should Create Expected Menu Items In Order")
    void test2_shouldCreateExpectedMenuItemsInOrder() {
        assertEquals("Zoom In", mViewMenu.getItem(0).getText());
        assertEquals("Zoom Out", mViewMenu.getItem(1).getText());
        assertEquals("Restore Default Zoom", mViewMenu.getItem(2).getText());
    }

    @Test
    @DisplayName("Test 3: Zoom In Should Increase Font Size")
    void test3_zoomInShouldIncreaseFontSize() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> {
            assertEquals(20, mTextArea.getFont().getSize());

            mViewMenu.getItem(0).doClick();
            assertEquals(22, mTextArea.getFont().getSize());

            mViewMenu.getItem(1).doClick();
            assertEquals(20, mTextArea.getFont().getSize());
        });
    }

    @Test
    @DisplayName("Test 4: Zoom Out Should Decrease Font Size")
    void test4_zoomOutShouldDecreaseFontSize() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            mViewMenu.getItem(1).doClick();

            assertEquals(18, mTextArea.getFont().getSize());
        });
    }

    @Test
    @DisplayName("Test 5: Restore Default Zoom Should Reset Font Size")
    void test5_restoreDefaultZoomShouldResetFontSize() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            mViewMenu.getItem(0).doClick();
            mViewMenu.getItem(0).doClick();

            assertEquals(24, mTextArea.getFont().getSize());

            mViewMenu.getItem(2).doClick();

            assertEquals(20, mTextArea.getFont().getSize());
        });
    }

    @Test
    @DisplayName("Test 6: Should Set Correct Keyboard Shortcuts")
    void test6_shouldSetCorrectKeyboardShortcuts() {
        assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.CTRL_DOWN_MASK), mViewMenu.getItem(0).getAccelerator());
        assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, KeyEvent.CTRL_DOWN_MASK), mViewMenu.getItem(1).getAccelerator());
    }

    @Test
    @DisplayName("Test 7: Zoom In Should Stop At 300 Percent")
    void test7_zoomInShouldStopAtMaximumZoom() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            for (int i = 0; i < 50; i++)
                mViewMenu.getItem(0).doClick();

            assertEquals(60, mTextArea.getFont().getSize());
            assertFalse(mViewMenu.getItem(0).isEnabled());
        });
    }

    @Test
    @DisplayName("Test 8: Zoom Out Should Stop At 50 Percent")
    void test8_zoomOutShouldStopAtMinimumZoom() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            for (int i = 0; i < 50; i++)
                mViewMenu.getItem(1).doClick();

            assertEquals(10, mTextArea.getFont().getSize());
            assertFalse(mViewMenu.getItem(1).isEnabled());
        });
    }

    @Test
    @DisplayName("Test 9: Zoom In Should Notify Callback")
    void test9_zoomInShouldNotifyCallback() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            mViewMenu.getItem(0).doClick();

            assertEquals(110, mLatestZoom[0]);
        });
    }
}
