package com.notepad.gui.menuItems;

import com.notepad.operations.Operations;
import net.miginfocom.swing.MigLayout;

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

public class EditMenu extends JMenu {

    private JTextArea textArea;
    private JFrame frame;
    private Operations operations;

    private int lastSearchIndex = 0;

    // Provides support for undo and redo operations
    private final UndoManager undoManager;

    public EditMenu(JFrame frame, JTextArea textArea) {
        super("Edit");
        this.frame = frame;
        this.textArea = textArea;

        operations = new Operations();
        undoManager = new UndoManager();
        textArea.getDocument().addUndoableEditListener(e -> undoManager.addEdit(e.getEdit()));
        createEditMenu();
    }

    private void createEditMenu() {
        JMenuItem cutMenuItem = new JMenuItem("Cut");
        cutMenuItem.addActionListener(_ -> operations.cut(textArea));
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem copyMenuItem = new JMenuItem("Copy");
        copyMenuItem.addActionListener(_ -> operations.copy(textArea));
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem pasteMenuItem = new JMenuItem("Paste");
        pasteMenuItem.addActionListener(_ -> operations.paste(textArea));
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem undoMenuItem = new JMenuItem("Undo");
        undoMenuItem.addActionListener(_ -> operations.undo(undoManager));
        undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem redoMenuItem = new JMenuItem("Redo");
        redoMenuItem.addActionListener(_ -> operations.redo(undoManager));
        redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem findMenuItem = new JMenuItem("Find");
        findMenuItem.addActionListener(_ -> showFindDialog());
        findMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem findAndReplaceMenuItem = new JMenuItem("Find & Replace");
        findAndReplaceMenuItem.addActionListener(_ -> showFindAndReplaceDialog());
        findAndReplaceMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK));

        add(cutMenuItem);
        add(copyMenuItem);
        add(pasteMenuItem);
        add(undoMenuItem);
        add(redoMenuItem);
        add(findMenuItem);
        add(findAndReplaceMenuItem);
    }

    private void showFindDialog() {
        JDialog findDialog = new JDialog(frame, "Find");
        findDialog.setLayout(new MigLayout());
        findDialog.setResizable(false);

        JLabel findLabel = new JLabel("Find:");
        JTextField findField = new JTextField(20);
        JCheckBox caseSensitiveCheckbox = new JCheckBox("Case Sensitive");

        JButton findButton = new JButton("Find Next");
        findButton.addActionListener(_ -> findText(findField.getText(), caseSensitiveCheckbox.isSelected()));

        findDialog.add(findLabel, "left, split 2");
        findDialog.add(findField, "pushx, growx, wrap");
        findDialog.add(caseSensitiveCheckbox, "split 2");
        findDialog.add(findButton);

        findDialog.pack();
        findDialog.setVisible(true);
    }

    private void showFindAndReplaceDialog() {
        JDialog findReplaceDialog = new JDialog(frame, "Find and Replace", true);
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

        JPanel buttonPanel = new JPanel(new MigLayout());

        // Buttons for find, replace, and replace all
        JButton findButton = new JButton("Find Next");
        buttonPanel.add(findButton);

        JButton replaceButton = new JButton("Replace");
        buttonPanel.add(replaceButton);

        JButton replaceAllButton = new JButton("Replace All");
        buttonPanel.add(replaceAllButton);

        findButton.addActionListener(_ -> findText(findField.getText(), caseSensitiveCheckbox.isSelected()));
        replaceButton.addActionListener(_ -> replaceText(findField.getText(), replaceField.getText(), caseSensitiveCheckbox.isSelected()));
        replaceAllButton.addActionListener(_ -> replaceAllText(findField.getText(), replaceField.getText(), caseSensitiveCheckbox.isSelected()));

        findReplaceDialog.add(buttonPanel);
        findReplaceDialog.pack();
        findReplaceDialog.setLocationRelativeTo(frame);
        findReplaceDialog.setVisible(true);
    }

    private void findText(String text, boolean caseSensitive) {
        String content = textArea.getText();

        if (!caseSensitive) {
            content = content.toLowerCase();
            text = text.toLowerCase();
        }

        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter text to find.");
            return;
        }

        int index = content.indexOf(text, lastSearchIndex);
        if (index == -1) {
            lastSearchIndex = 0; // Reset search index
            JOptionPane.showMessageDialog(frame, "Text not found.");
        } else {
            textArea.setCaretPosition(index);
            textArea.select(index, index + text.length());
            lastSearchIndex = index + text.length(); // Update search index
        }
    }

    private void replaceText(String findText, String replaceText, boolean caseSensitive) {
        String content = textArea.getText();
        String searchContent = caseSensitive ? content : content.toLowerCase();
        String searchText = caseSensitive ? findText : findText.toLowerCase();

        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter text to find.");
            return;
        }

        int index = searchContent.indexOf(searchText, lastSearchIndex);
        if (index == -1) {
            lastSearchIndex = 0; // Reset search index
            JOptionPane.showMessageDialog(frame, "Text not found.");
        } else {
            // Replace the found text
            textArea.replaceRange(replaceText, index, index + findText.length());
            lastSearchIndex = index + replaceText.length();
            textArea.setCaretPosition(lastSearchIndex);
            textArea.select(lastSearchIndex - replaceText.length(), lastSearchIndex);
        }
    }

    private void replaceAllText(String findText, String replaceText, boolean caseSensitive) {
        String content = textArea.getText();
        String searchContent = caseSensitive ? content : content.toLowerCase();
        String searchText = caseSensitive ? findText : findText.toLowerCase();

        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter text to find.");
            return;
        }

        int count = 0;
        int index = searchContent.indexOf(searchText);
        while (index != -1) {
            // Replace the found text
            textArea.replaceRange(replaceText, index, index + findText.length());

            // Update content for subsequent search
            content = textArea.getText();
            searchContent = caseSensitive ? content : content.toLowerCase();

            // Move the search index forward to avoid replacing the same content repeatedly
            index = searchContent.indexOf(searchText, index + replaceText.length());
            count++;
        }

        if (count > 0)
            JOptionPane.showMessageDialog(frame, count + " occurrence(s) replaced.");
        else
            JOptionPane.showMessageDialog(frame, "No occurrences found.");
    }
}
