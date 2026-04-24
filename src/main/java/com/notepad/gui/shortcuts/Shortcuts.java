package com.notepad.gui.shortcuts;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Shortcuts {

    public ActionListener showKeyboardShortcuts() {
        return _ -> {
            // Define shortcut data: each row contains a description and a shortcut
            String[][] shortcutData = {
                    {"New Note", "Ctrl + N"},
                    {"Open", "Ctrl + O"},
                    {"Save", "Ctrl + S"},
                    {"Save As", "Ctrl + Shift + S"},
                    {"Exit", "Ctrl + Q"},
                    {"Cut", "Ctrl + X"},
                    {"Copy", "Ctrl + C"},
                    {"Paste", "Ctrl + V"},
                    {"Undo", "Ctrl + Z"},
                    {"Redo", "Ctrl + Y"},
                    {"Find", "Ctrl + F"},
                    {"Find & Replace", "Ctrl + R"},
                    {"Zoom In", "Ctrl + ="},
                    {"Zoom Out", "Ctrl + -"},
                    {"Distraction-Free Mode", "F11"}
            };

            // Column headers for the table
            String[] columnNames = {"Function", "Shortcut"};

            // Create a JTable with the shortcuts data
            JTable shortcutsTable = new JTable(shortcutData, columnNames);
            shortcutsTable.setEnabled(false);
            shortcutsTable.setPreferredScrollableViewportSize(new Dimension(400, 300));

            // Wrap the table in a JScrollPane for better UI
            JScrollPane scrollPane = new JScrollPane(shortcutsTable);

            // Create the dialog to display the shortcuts table
            JDialog dialog = new JDialog();
            dialog.setTitle("Keyboard Shortcuts");
            dialog.setLayout(new MigLayout());
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setResizable(false);
            dialog.add(scrollPane);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        };
    }
}