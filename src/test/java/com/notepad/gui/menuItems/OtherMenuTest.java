package com.notepad.gui.menuItems;

import com.notepad.app.StatusBar;
import net.miginfocom.swing.MigLayout;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OtherMenuTest {

    private JFrame mFrame;
    private JTextArea mTextArea;
    private OtherMenu mOtherMenu;
    private StatusBar mStatusBar;
    private JScrollPane mTextScrollPane;

    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            mFrame = new JFrame();
            mFrame.setLayout(new MigLayout("insets 0, fill", "[grow]", "[grow][]"));
            mTextArea = new JTextArea();
            mTextScrollPane = new JScrollPane(mTextArea);
            mStatusBar = new StatusBar();
            mOtherMenu = new OtherMenu(mFrame, mStatusBar, mTextArea, mTextScrollPane);

            JMenuBar menuBar = new JMenuBar();
            menuBar.add(mOtherMenu);
            mFrame.setJMenuBar(menuBar);

            mFrame.add(mTextScrollPane);
            mFrame.add(mStatusBar);
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
    void test1_shouldCreateOtherMenuWithCorrectText() {
        assertEquals("Other", mOtherMenu.getText());
    }

    @Test
    @DisplayName("Test 2: Should Create Expected Menu Items In Order")
    void test2_shouldCreateExpectedMenuItemsInOrder() {
        assertEquals("Enter Distraction-Free Mode", mOtherMenu.getItem(0).getText());
        assertEquals("Enter Markdown Mode", mOtherMenu.getItem(1).getText());
    }

    @Test
    @DisplayName("Test 3: Should Enter Distraction Free Mode")
    void test3_shouldEnterDistractionFreeMode() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> {
            JMenuItem distractionItem = mOtherMenu.getItem(0);
            distractionItem.doClick();

            assertEquals("Exit Distraction-Free Mode", distractionItem.getText());
            assertFalse(mStatusBar.isVisible());
            assertFalse(mFrame.getJMenuBar().isVisible());
        });
    }

    @Test
    @DisplayName("Test 4: Should Enter Markdown Mode")
    void test4_shouldEnterMarkdownMode() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> {
            JMenuItem markdownItem = mOtherMenu.getItem(1);
            markdownItem.doClick();

            assertEquals("Exit Markdown Mode", markdownItem.getText());
            assertTrue(Arrays.stream(mFrame.getContentPane().getComponents())
                    .anyMatch(component -> component instanceof JSplitPane));
        });
    }

    @Test
    @DisplayName("Test 5: Should Exit Markdown Mode")
    void test5_shouldExitMarkdownMode() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JMenuItem markdownItem = mOtherMenu.getItem(1);
            markdownItem.doClick(); // enter
            markdownItem.doClick(); // exit

            assertEquals("Enter Markdown Mode", markdownItem.getText());
            assertFalse(Arrays.stream(mFrame.getContentPane().getComponents())
                    .anyMatch(component -> component instanceof JSplitPane));
        });
    }

    @Test
    @DisplayName("Test 6: Should Preserve Text When Toggling Markdown Mode")
    void test6_shouldPreserveTextWhenTogglingMarkdownMode() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            String markdown = "# Test Heading\n\n- Item one\n- Item two";
            mTextArea.setText(markdown);

            JMenuItem markdownItem = mOtherMenu.getItem(1);
            markdownItem.doClick(); // enter
            markdownItem.doClick(); // exit

            assertEquals(markdown, mTextArea.getText());
        });
    }
}
