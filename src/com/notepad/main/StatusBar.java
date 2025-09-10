package com.notepad.main;

import net.miginfocom.swing.MigLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

public class StatusBar extends JPanel {
    private final JLabel caretLabel = new JLabel("Ln 1, Col 1");
    private final JLabel countLabel = new JLabel("Words: 0  Chars: 0");
    private final JLabel zoomLabel  = new JLabel("Zoom: 100%");
    private final JLabel encLabel   = new JLabel("UTF-8");
    private final JLabel eolLabel   = new JLabel("LF");

    private JTextComponent editor;

    public StatusBar() {
        setLayout(new MigLayout("insets 3 8 3 8, fillx", "[]12[]12[]12[]push[]", "[]"));
        setBorder(BorderFactory.createMatteBorder(
                1, 0, 0, 0, UIManager.getColor("Component.borderColor")));

        add(caretLabel, "gapright 12");
        add(countLabel, "gapright 12");
        add(encLabel, "gapright 12");
        add(eolLabel, "gapright 12");
        add(zoomLabel,  "alignx right");
    }

    /** Bind once to your editor (JTextArea/JTextPane). */
    public void bindToEditor(JTextComponent editor) {
        this.editor = editor;

        editor.addCaretListener(_ -> updateAll());
        editor.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateAll(); }
            public void removeUpdate(DocumentEvent e) { updateAll(); }
            public void changedUpdate(DocumentEvent e) { updateAll(); }
        });

        SwingUtilities.invokeLater(this::updateAll);
    }

    public void setEncodingDisplay(String name) {
        encLabel.setText(name);
    }

    public void setEolDisplay(String eol) {
        eolLabel.setText(eol);
    }

    public void setZoomPercent(int percent) {
        zoomLabel.setText("Zoom: " + percent + "%");
    }

    private void updateAll() {
        if (editor == null) return;

        try {
            int caret = editor.getCaretPosition();
            var root = editor.getDocument().getDefaultRootElement();
            int line = root.getElementIndex(caret) + 1;
            int col  = caret - root.getElement(line - 1).getStartOffset() + 1;
            caretLabel.setText("Ln " + line + ", Col " + col);
        } catch (Exception ignored) {}

        String text = editor.getText();
        int chars = text.length();
        int words = text.isBlank() ? 0 : text.trim().split("\\s+").length;
        countLabel.setText("Words: " + words + "  Chars: " + chars);

        Object encProp = editor.getClientProperty("encoding");
        if (encProp instanceof String s && !s.isBlank())
            encLabel.setText(s);

        Object eolProp = editor.getClientProperty("eol");
        if (eolProp instanceof String s2 && !s2.isBlank())
            eolLabel.setText(s2);
    }
}
