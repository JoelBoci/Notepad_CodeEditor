package com.notepad.gui.menuItems;

import com.notepad.operations.Operations;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.undo.UndoManager;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class EditMenu extends JMenu {

    private final JTextArea mTextArea;
    private final JFrame mFrame;
    private final Operations mOperations;

    private int mLastSearchIndex = 0;

    // Provides support for undo and redo operations
    private final UndoManager mUndoManager;

    private static final Logger mLogger = LoggerFactory.getLogger(EditMenu.class);

    public EditMenu(JFrame frame, JTextArea textArea) {
        super("Edit");
        this.mFrame = frame;
        this.mTextArea = textArea;

        mOperations = new Operations();
        mUndoManager = new UndoManager();
        textArea.getDocument().addUndoableEditListener(e -> mUndoManager.addEdit(e.getEdit()));
        createEditMenu();
    }

    private void createEditMenu() {
        JMenuItem cutMenuItem = new JMenuItem("Cut");
        cutMenuItem.addActionListener(_ -> mOperations.cut(mTextArea));
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem copyMenuItem = new JMenuItem("Copy");
        copyMenuItem.addActionListener(_ -> mOperations.copy(mTextArea));
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem pasteMenuItem = new JMenuItem("Paste");
        pasteMenuItem.addActionListener(_ -> mOperations.paste(mTextArea));
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem undoMenuItem = new JMenuItem("Undo");
        undoMenuItem.addActionListener(_ -> mOperations.undo(mUndoManager));
        undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem redoMenuItem = new JMenuItem("Redo");
        redoMenuItem.addActionListener(_ -> mOperations.redo(mUndoManager));
        redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem findMenuItem = new JMenuItem("Find");
        findMenuItem.addActionListener(_ -> showFindDialog());
        findMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem findAndReplaceMenuItem = new JMenuItem("Find & Replace");
        findAndReplaceMenuItem.addActionListener(_ -> showFindAndReplaceDialog());
        findAndReplaceMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));

        add(cutMenuItem);
        add(copyMenuItem);
        add(pasteMenuItem);
        add(undoMenuItem);
        add(redoMenuItem);
        add(findMenuItem);
        add(findAndReplaceMenuItem);
    }

    private void showFindDialog() {
        JDialog findDialog = new JDialog(mFrame, "Find");
        findDialog.setLayout(new MigLayout());
        findDialog.setResizable(false);

        JLabel findLabel = new JLabel("Find:");
        JTextField findField = new JTextField(20);
        JCheckBox caseSensitiveCheckbox = new JCheckBox("Case Sensitive");

        // Regex checkbox
        JCheckBox regexCheckbox = new JCheckBox("Regex Mode");

        JButton findButton = new JButton("Find Next");
        findButton.addActionListener(_ -> {
            if (regexCheckbox.isSelected())
                findTextRegex(findField.getText(), caseSensitiveCheckbox.isSelected());
            else
                findText(findField.getText(), caseSensitiveCheckbox.isSelected());
        });

        findDialog.add(findLabel, "left, split 2");
        findDialog.add(findField, "pushx, growx, wrap");
        findDialog.add(caseSensitiveCheckbox, "split 2");
        findDialog.add(regexCheckbox, "wrap");
        findDialog.add(findButton);

        findDialog.pack();
        findDialog.setVisible(true);
    }

    private void showFindAndReplaceDialog() {
        JDialog findReplaceDialog = new JDialog(mFrame, "Find and Replace", true);
        findReplaceDialog.setLayout(new MigLayout());
        findReplaceDialog.setResizable(false);

        // Find label
        JLabel findLabel = new JLabel("Find:");
        findReplaceDialog.add(findLabel, "left, split 2, sg 1");

        // Find text field
        JTextField findField = new JTextField(15);
        findReplaceDialog.add(findField, "pushx, growx, wrap");

        // Replace label
        JLabel replaceLabel = new JLabel("Replace:");
        findReplaceDialog.add(replaceLabel, "left, split 2, sg 1");

        // Replace text field
        JTextField replaceField = new JTextField(15);
        findReplaceDialog.add(replaceField, "pushx, growx, wrap");

        // Case sensitivity checkbox
        JCheckBox caseSensitiveCheckbox = new JCheckBox("Case Sensitive");
        findReplaceDialog.add(caseSensitiveCheckbox, "wrap");

        // Regex checkbox
        JCheckBox regexCheckbox = new JCheckBox("Regex Mode");
        findReplaceDialog.add(regexCheckbox, "wrap");

        JPanel buttonPanel = new JPanel(new MigLayout());

        // Buttons for find, replace, and replace all
        JButton findButton = new JButton("Find Next");
        buttonPanel.add(findButton);

        JButton replaceButton = new JButton("Replace");
        buttonPanel.add(replaceButton);

        JButton replaceAllButton = new JButton("Replace All");
        buttonPanel.add(replaceAllButton);

        findButton.addActionListener(_ -> {
            if (regexCheckbox.isSelected())
                findTextRegex(findField.getText(), caseSensitiveCheckbox.isSelected());
            else
                findText(findField.getText(), caseSensitiveCheckbox.isSelected());
        });

        replaceButton.addActionListener(_ -> {
            if (regexCheckbox.isSelected())
                replaceTextRegex(findField.getText(), replaceField.getText(), caseSensitiveCheckbox.isSelected());
            else
                replaceText(findField.getText(), replaceField.getText(), caseSensitiveCheckbox.isSelected());
        });

        replaceAllButton.addActionListener(_ -> {
            if (regexCheckbox.isSelected())
                replaceAllTextRegex(findField.getText(), replaceField.getText(), caseSensitiveCheckbox.isSelected());
            else
                replaceAllText(findField.getText(), replaceField.getText(), caseSensitiveCheckbox.isSelected());
        });
    
        findReplaceDialog.add(buttonPanel);
        findReplaceDialog.pack();
        findReplaceDialog.setLocationRelativeTo(mFrame);
        findReplaceDialog.setVisible(true);
    }

    private void findText(String text, boolean caseSensitive) {
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(mFrame, "Please enter text to find.");
            return;
        }

        String content = mTextArea.getText();
        if (!caseSensitive) {
            content = content.toLowerCase();
            text = text.toLowerCase();
        }

        int index = content.indexOf(text, mLastSearchIndex);
        if (index == -1) {
            mLastSearchIndex = 0; // Reset search index
            JOptionPane.showMessageDialog(mFrame, "Text not found.");
        } else {
            mTextArea.setCaretPosition(index);
            mTextArea.select(index, index + text.length());
            mLastSearchIndex = index + text.length(); // Update search index
        }
    }

    private void findTextRegex(String text, boolean caseSensitive) {
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(mFrame, "Please enter text to find.");
            return;
        }

        try {
            Matcher matcher = compilePattern(text, caseSensitive).matcher(mTextArea.getText());

            if (!matcher.find(mLastSearchIndex)) {
                mLastSearchIndex = 0;
                JOptionPane.showMessageDialog(mFrame, "Text not found.");
                return;
            }

            int matchStart = matcher.start();
            int matchEnd = matcher.end();

            mTextArea.select(matchStart, matchEnd);
            mTextArea.setCaretPosition(matchStart);
            mTextArea.moveCaretPosition(matchEnd);

            mLastSearchIndex = matchEnd;
        } catch (PatternSyntaxException e) {
            showRegexError(e);
        }
    }

    private void replaceText(String findText, String replaceText, boolean caseSensitive) {
        String searchContent = caseSensitive ? mTextArea.getText() : mTextArea.getText().toLowerCase();
        String searchText = caseSensitive ? findText : findText.toLowerCase();
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(mFrame, "Please enter text to find.");
            return;
        }

        int index = searchContent.indexOf(searchText, mLastSearchIndex);
        if (index == -1) {
            mLastSearchIndex = 0; // Reset search index
            JOptionPane.showMessageDialog(mFrame, "Text not found.");
        } else {
            // Replace the found text
            mTextArea.replaceRange(replaceText, index, index + findText.length());
            mLastSearchIndex = index + replaceText.length();
            mTextArea.setCaretPosition(mLastSearchIndex);
            mTextArea.select(mLastSearchIndex - replaceText.length(), mLastSearchIndex);
        }
    }

    private void replaceTextRegex(String findText, String replaceText, boolean caseSensitive) {
        if (findText.isEmpty()) {
            JOptionPane.showMessageDialog(mFrame, "Please enter text to find.");
            return;
        }

        try {
            Matcher matcher = compilePattern(findText, caseSensitive).matcher(mTextArea.getText());

            if (!matcher.find(mLastSearchIndex)) {
                mLastSearchIndex = 0;
                JOptionPane.showMessageDialog(mFrame, "Text not found.");
                return;
            }

            int start = matcher.start();
            int end = matcher.end();

            String replacedText = compilePattern(findText, caseSensitive)
                    .matcher(mTextArea.getText().substring(start, end))
                    .replaceFirst(replaceText);

            mTextArea.replaceRange(replacedText, start, end);

            int newEnd = start + replacedText.length();
            mTextArea.setCaretPosition(start);
            mTextArea.moveCaretPosition(newEnd);
            mLastSearchIndex = newEnd;
        } catch (PatternSyntaxException e) {
            showRegexError(e);
        }
    }

    private void replaceAllText(String findText, String replaceText, boolean caseSensitive) {
        String content = mTextArea.getText();
        String searchContent = caseSensitive ? content : content.toLowerCase();
        String searchText = caseSensitive ? findText : findText.toLowerCase();

        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(mFrame, "Please enter text to find.");
            return;
        }

        int count = 0;
        int index = searchContent.indexOf(searchText);
        while (index != -1) {
            // Replace the found text
            mTextArea.replaceRange(replaceText, index, index + findText.length());

            // Update content for subsequent search
            content = mTextArea.getText();
            searchContent = caseSensitive ? content : content.toLowerCase();

            // Move the search index forward to avoid replacing the same content repeatedly
            index = searchContent.indexOf(searchText, index + replaceText.length());
            count++;
        }

        if (count > 0)
            JOptionPane.showMessageDialog(mFrame, count + " occurrence(s) replaced.");
        else
            JOptionPane.showMessageDialog(mFrame, "No occurrences found.");
    }

    private void replaceAllTextRegex(String findText, String replaceText, boolean caseSensitive) {
        if (findText.isEmpty()) {
            JOptionPane.showMessageDialog(mFrame, "Please enter text to find.");
            return;
        }

        try {
            Matcher matcher = compilePattern(findText, caseSensitive).matcher(mTextArea.getText());
            StringBuilder result = new StringBuilder();
            int count = 0;

            while (matcher.find()) {
                matcher.appendReplacement(result, replaceText);
                count++;
            }
            matcher.appendTail(result);

            if (count == 0) {
                JOptionPane.showMessageDialog(mFrame, "No occurrences found.");
                return;
            }

            mTextArea.setText(result.toString());
            mLastSearchIndex = 0;
            JOptionPane.showMessageDialog(mFrame, count + " occurrence(s) replaced.");
        } catch (PatternSyntaxException e) {
            showRegexError(e);
        }
    }

    private Pattern compilePattern(String text, boolean caseSensitive) throws PatternSyntaxException {
        return caseSensitive ? Pattern.compile(text) : Pattern.compile(text, Pattern.CASE_INSENSITIVE);
    }

    private void showRegexError(PatternSyntaxException e) {
        JOptionPane.showMessageDialog(
                mFrame,
                "Invalid regular expression:\n" + e.getDescription(),
                "Regex Error",
                JOptionPane.ERROR_MESSAGE
        );
        mLogger.error("Invalid regex: {}", e.getMessage(), e);
    }
}
