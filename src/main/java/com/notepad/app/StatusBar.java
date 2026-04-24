package com.notepad.app;

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
    private final JLabel mCaretLabel = new JLabel("Ln 1, Col 1");
    private final JLabel mCountLabel = new JLabel("Words: 0  Chars: 0");
    private final JLabel mZoomLabel  = new JLabel("Zoom: 100%");
    private final JLabel mEncLabel   = new JLabel("UTF-8");
    private final JLabel mEolLabel   = new JLabel("LF");

    private JTextComponent mEditor;

    public StatusBar() {
        setLayout(new MigLayout("insets 3 8 3 8, fillx", "[]12[]12[]12[]push[]", "[]"));
        setBorder(BorderFactory.createMatteBorder(
                1, 0, 0, 0, UIManager.getColor("Component.borderColor")));

        add(mCaretLabel, "gapright 12");
        add(mCountLabel, "gapright 12");
        add(mEncLabel, "gapright 12");
        add(mEolLabel, "gapright 12");
        add(mZoomLabel,  "alignx right");
    }

    public void bindToEditor(JTextComponent mEditor) {
        this.mEditor = mEditor;

        mEditor.addCaretListener(_ -> updateAll());
        mEditor.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateAll(); }
            public void removeUpdate(DocumentEvent e) { updateAll(); }
            public void changedUpdate(DocumentEvent e) { updateAll(); }
        });

        SwingUtilities.invokeLater(this::updateAll);
    }

    public void setEncodingDisplay(String name) {
        mEncLabel.setText(name);
    }

    public void setEolDisplay(String eol) {
        mEolLabel.setText(eol);
    }

    public void setZoomPercent(int percent) {
        mZoomLabel.setText("Zoom: " + percent + "%");
    }

    private void updateAll() {
        if (mEditor == null) return;

        try {
            int caret = mEditor.getCaretPosition();
            var root = mEditor.getDocument().getDefaultRootElement();
            int line = root.getElementIndex(caret) + 1;
            int col  = caret - root.getElement(line - 1).getStartOffset() + 1;
            mCaretLabel.setText("Ln " + line + ", Col " + col);
        } catch (Exception ignored) {}

        String text = mEditor.getText();
        int chars = text.length();
        int words = text.isBlank() ? 0 : text.trim().split("\\s+").length;
        mCountLabel.setText("Words: " + words + "  Chars: " + chars);

        Object encProp = mEditor.getClientProperty("encoding");
        if (encProp instanceof String s && !s.isBlank())
            mEncLabel.setText(s);

        Object eolProp = mEditor.getClientProperty("eol");
        if (eolProp instanceof String s2 && !s2.isBlank())
            mEolLabel.setText(s2);
    }
}
