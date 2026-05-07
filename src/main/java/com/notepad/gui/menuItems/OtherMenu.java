package com.notepad.gui.menuItems;

import com.notepad.app.StatusBar;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class OtherMenu extends JMenu {

    private final JFrame mFrame;

    private static final int DISTRACTION_FONT_BOOST = 4;
    private boolean mDistractionFreeMode = false;
    private JMenuItem mDistractionMenuItem;

    private boolean mMarkdownMode = false;
    private JMenuItem mMarkdownModeMenuItem;
    private JEditorPane mMarkdownPreview;
    private JSplitPane mMarkdownSplitPane;

    private final StatusBar mStatusBar;
    private int mPreviousExtendedState = JFrame.NORMAL;
    private final JTextArea mTextArea;
    private final JScrollPane mTextScrollPane;
    private Font mPreviousFont;

    private static final Logger mLogger = LoggerFactory.getLogger(OtherMenu.class);

    public OtherMenu(JFrame frame, StatusBar statusBar, JTextArea textArea, JScrollPane textScrollPane) {
        super("Other");
        mFrame = frame;
        mStatusBar = statusBar;
        mTextArea = textArea;
        mTextScrollPane = textScrollPane;

        mTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateMarkdownPreview();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateMarkdownPreview();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateMarkdownPreview();
            }
        });

        addOtherMenu();
        bindKeys();
    }

    private void addOtherMenu() {
        mDistractionMenuItem = new JMenuItem("Enter Distraction-Free Mode");
        mDistractionMenuItem.addActionListener(_ -> setDistractionFree());
        add(mDistractionMenuItem);

        mMarkdownModeMenuItem = new JMenuItem("Enter Markdown Mode");
        mMarkdownModeMenuItem.addActionListener(_ -> toggleMarkdownMode());
        add(mMarkdownModeMenuItem);
    }

    private void setDistractionFree() {
        mDistractionFreeMode = !mDistractionFreeMode;
        mLogger.info("{} distraction-free mode", mDistractionFreeMode ? "Entering" : "Exiting");

        mFrame.getJMenuBar().setVisible(!mDistractionFreeMode);

        if (mDistractionFreeMode) {
            mPreviousExtendedState = mFrame.getExtendedState();
            mPreviousFont = mTextArea.getFont();
        }

        mFrame.setExtendedState(mDistractionFreeMode ? mPreviousExtendedState | JFrame.MAXIMIZED_BOTH : mPreviousExtendedState);

        mStatusBar.setVisible(!mDistractionFreeMode);

        mDistractionMenuItem.setText(mDistractionFreeMode ? "Exit Distraction-Free Mode" : "Enter Distraction-Free Mode");

        mTextArea.setBorder(mDistractionFreeMode ?
                BorderFactory.createEmptyBorder(20, 200, 20, 200) :
                BorderFactory.createEmptyBorder());
        mTextArea.setFont(mDistractionFreeMode ? mPreviousFont.deriveFont((float) (mPreviousFont.getSize() + DISTRACTION_FONT_BOOST)) : mPreviousFont);

        mFrame.revalidate();
        mFrame.repaint();
    }

    private void toggleMarkdownMode() {
        mMarkdownMode = !mMarkdownMode;
        mLogger.info("{} markdown mode", mMarkdownMode ? "Entering" : "Exiting");

        if (mMarkdownMode)
            enableMarkdownMode();
        else
            disableMarkdownMode();

        mMarkdownModeMenuItem.setText(mMarkdownMode ? "Exit Markdown Mode" : "Enter Markdown Mode");

        mFrame.revalidate();
        mFrame.repaint();
    }

    private void enableMarkdownMode() {
        mLogger.info("Markdown preview enabled");
        mMarkdownPreview = new JEditorPane();
        mMarkdownPreview.setContentType("text/html");
        mMarkdownPreview.setEditable(false);

        mMarkdownPreview.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                    mLogger.info("Opening markdown link: {}", e.getURL());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mFrame, "Could not open link: " + ex.getMessage());
                    mLogger.error("Could not open markdown link: {}", e.getURL(), ex);
                }
            }
        });

        mMarkdownSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mTextScrollPane, new JScrollPane(mMarkdownPreview));
        mMarkdownSplitPane.setResizeWeight(0.5);

        mFrame.remove(mTextScrollPane);
        mFrame.add(mMarkdownSplitPane, "cell 0 0, grow, push");

        updateMarkdownPreview();
    }

    private void disableMarkdownMode() {
        mLogger.info("Markdown preview disabled");
        if (mMarkdownSplitPane != null)
            mFrame.remove(mMarkdownSplitPane);

        mFrame.add(mTextScrollPane, "cell 0 0, grow, push");

        mMarkdownSplitPane = null;
        mMarkdownPreview = null;
    }

    private void updateMarkdownPreview() {
        if (!mMarkdownMode || mMarkdownPreview == null)
            return;

        List<Extension> extensions = List.of(TablesExtension.create());
        String renderedHtml = HtmlRenderer.builder()
                .extensions(extensions)
                .build()
                .render(Parser.builder()
                        .extensions(extensions)
                        .build()
                        .parse(mTextArea.getText()));

        Color bg = UIManager.getColor("TextArea.background");
        Color fg = UIManager.getColor("TextArea.foreground");
        Color border = fg.darker();
        Color headerBg = bg.darker();
        Color codeBg = bg.darker();

        mMarkdownPreview.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        mMarkdownPreview.setBackground(bg);
        mMarkdownPreview.setForeground(fg);
        mMarkdownPreview.setOpaque(true);

        // Custom CSS so that tables look nice. Support for that isn't part of commonmark as far as I'm aware :(
        String styledHtml = """
            <html>
            <head>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        padding: 10px;
                        background-color: %s;
                        color: %s;
                    }

                    h1, h2, h3, h4, h5, h6, p, li, td, th, blockquote {
                        color: %s;
                    }

                    table {
                        border-collapse: collapse;
                        width: 100%%;
                    }

                    th, td {
                        border: 1px solid %s;
                        padding: 8px;
                        text-align: left;
                    }

                    th {
                        background-color: %s;
                    }

                    code, pre {
                        background-color: %s;
                        color: %s;
                    }

                    pre {
                        padding: 10px;
                    }

                    blockquote {
                        border-left: 4px solid %s;
                        padding-left: 10px;
                    }
                </style>
            </head>
            <body>
            %s
            </body>
            </html>
            """.formatted(
                toHex(bg),
                toHex(fg),
                toHex(fg),
                toHex(border),
                toHex(headerBg),
                toHex(codeBg),
                toHex(fg),
                toHex(border),
                renderedHtml
        );

        mMarkdownPreview.setText(styledHtml);
        mMarkdownPreview.setCaretPosition(0);
    }

    private String toHex(Color color) {
        if (color == null)
            return "#FFFFFF";

        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    public void refreshMarkdownPreview() {
        updateMarkdownPreview();
    }

    private void bindKey(KeyStroke keyStroke, String actionName, Runnable action) {
        var root = mFrame.getRootPane();

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(keyStroke, actionName);

        root.getActionMap().put(actionName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    private void bindKeys() {
        bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "toggleDistractionFree", this::setDistractionFree);

        bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitDistraction",
                () -> {
                    if (mDistractionFreeMode)
                        setDistractionFree();
                });
    }
}
