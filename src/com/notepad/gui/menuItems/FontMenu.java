package com.notepad.gui.menuItems;

import net.miginfocom.swing.MigLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FontMenu extends JDialog {
    Color mSelectedBlue = new Color(30, 144, 245);
    Color mBoneWhite = new Color(245, 245, 245);

    // Will need a reference to our GUI to make changes to the GUI from this class
    private final FormatMenu mSource;

    private JTextField mCurrentFontField;
    private JTextField mCurrentFontStyleField;
    private JTextField mCurrentFontSizeField;

    private JPanel mCurrentColourBox;

    private static final Logger mLogger = LoggerFactory.getLogger(FontMenu.class);

    public FontMenu(FormatMenu mSource) {
        this.mSource = mSource;
        setTitle("Font Settings");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(650, 350);
        setLocationRelativeTo(mSource); // Launch menu at center of notepad GUI
        setResizable(false);
        setModal(true);

        setLayout(new MigLayout("insets 10, fillx",
                "[grow,fill][pref!][pref!]"));

        addFontMenuComponents();
    }

    private void addFontMenuComponents() {
        JPanel buttonPanel = new JPanel(new MigLayout("fillx, ins 0"));
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(_ -> apply());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(_ -> cancel());

        buttonPanel.add(applyButton, "pushx, align right");
        buttonPanel.add(cancelButton);

        addFontChooser();
        addFontStyleChooser();
        addFontSizeChooser();
        addFontColorChooser();
        add(buttonPanel, "newline, span, growx");
    }

    private void apply() {
        // Get current font type
        String fontType = mCurrentFontField.getText();

        // Get font style
        int fontStyle = switch (mCurrentFontStyleField.getText()) {
            case "Plain" -> Font.PLAIN;
            case "Bold" -> Font.BOLD;
            case "Italic" -> Font.ITALIC;
            default -> Font.BOLD | Font.ITALIC;
        };

        int fontSize = Integer.parseInt(mCurrentFontSizeField.getText());
        Color fontColor = mCurrentColourBox.getBackground();
        Font newFont = new Font(fontType, fontStyle, fontSize);

        mSource.getMTextArea().setFont(newFont);

        mSource.getMTextArea().setForeground(fontColor);

        mLogger.info(
          "New Font Set: Font = '{}', Style = '{}', Size = '{}'", fontType, mCurrentFontStyleField.getText(), fontSize
        );

        // Dispose menu
        FontMenu.this.dispose();
    }

    private void cancel() {
        mLogger.info("Cancelling font setting operation");
        FontMenu.this.dispose();
    }

    private void addFontChooser() {
        JPanel fontPanel = new JPanel(new MigLayout("insets 0, fillx, wrap 1"));

        JLabel fontLabel = new JLabel("Font:");
        add(fontLabel, "cell 0 0");

        // Display the current font
        mCurrentFontField = new JTextField(mSource.getMTextArea().getFont().getFontName());
        mCurrentFontField.setEditable(false);
        fontPanel.add(mCurrentFontField, "growx");

        // Display the list of available fonts
        JPanel listOfFontsPanel = new JPanel(new MigLayout("insets 0, wrap 1", "[grow, fill]"));

        // Change the background colour to white
        listOfFontsPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(listOfFontsPanel);

        // Retrieve all the possible fonts
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();

        // For each font name, display them in the list of fonts panel as a label
        for (String font : fontNames) {
            JLabel fontNameLabel = new JLabel(font);

            fontNameLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // When clicked, set the mCurrentFontField to the font name
                    mCurrentFontField.setText(font);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // Add highlights over font name when the mouse hovers over them
                    fontNameLabel.setOpaque(true);
                    fontNameLabel.setBackground(mSelectedBlue);
                    fontNameLabel.setForeground(mBoneWhite);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // Remove the highlight once the mouse stops hovering over the font names
                    fontNameLabel.setBackground(null);
                    fontNameLabel.setForeground(null);
                }
            });

            // Add to panel
            listOfFontsPanel.add(fontNameLabel);
        }

        fontPanel.add(scrollPane, "grow, pushy");
        add(fontPanel, "cell 0 1, grow");
    }

    private void addFontStyleChooser() {
        // Will display the current font style and all available font styles
        JPanel fontStylePanel = new JPanel(new MigLayout("insets 0, fillx, wrap 1"));

        JLabel fontStyleLabel = new JLabel("Font Style:");
        add(fontStyleLabel, "cell 1 0");

        // Get the current font style
        int currentFontStyle = mSource.getMTextArea().getFont().getStyle();
        String currentFontStyleText = switch (currentFontStyle) {
            case Font.PLAIN -> "Plain";
            case Font.BOLD -> "Bold";
            case Font.ITALIC -> "Italic";
            default -> "Bold Italic";
        };

        mCurrentFontStyleField = new JTextField(currentFontStyleText);
        mCurrentFontStyleField.setEditable(false);
        fontStylePanel.add(mCurrentFontStyleField, "growx");

        // Display the list of all font styles available
        JPanel listOfFontStylesPanel = new JPanel(new MigLayout("insets 0, wrap 1", "[grow, fill]"));
        listOfFontStylesPanel.setBackground(Color.WHITE);

        // List of font styles:
        // Plain
        JLabel plainStyle = new JLabel("Plain");
        plainStyle.setFont(new Font("Dialog", Font.PLAIN, 12));
        plainStyle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Update the current style field
                mCurrentFontStyleField.setText(plainStyle.getText());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Add blue highlight when hovering
                plainStyle.setOpaque(true);
                plainStyle.setBackground(mSelectedBlue);
                plainStyle.setForeground(mBoneWhite);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Remove highlights
                plainStyle.setBackground(null);
                plainStyle.setForeground(null);
            }
        });

        listOfFontStylesPanel.add(plainStyle);

        // Bold:
        JLabel boldStyle = new JLabel("Bold");
        boldStyle.setFont(new Font("Dialog", Font.BOLD, 12));
        boldStyle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Update the current style field
                mCurrentFontStyleField.setText(boldStyle.getText());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Add blue highlight when hovering
                boldStyle.setOpaque(true);
                boldStyle.setBackground(mSelectedBlue);
                boldStyle.setForeground(mBoneWhite);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Remove highlights
                boldStyle.setBackground(null);
                boldStyle.setForeground(null);
            }
        });

        listOfFontStylesPanel.add(boldStyle);

        // Italic:
        JLabel italicStyle = new JLabel("Italic");
        italicStyle.setFont(new Font("Dialog", Font.ITALIC, 12));
        italicStyle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Update the current style field
                mCurrentFontStyleField.setText(italicStyle.getText());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Add blue highlight when hovering
                italicStyle.setOpaque(true);
                italicStyle.setBackground(mSelectedBlue);
                italicStyle.setForeground(mBoneWhite);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Remove highlights
                italicStyle.setBackground(null);
                italicStyle.setForeground(null);
            }
        });

        listOfFontStylesPanel.add(italicStyle);

        // Bold Italic:
        JLabel boldItalicStyle = new JLabel("Bold Italic");
        boldItalicStyle.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
        boldItalicStyle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Update the current style field
                mCurrentFontStyleField.setText(boldItalicStyle.getText());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Add blue highlight when hovering
                boldItalicStyle.setOpaque(true);
                boldItalicStyle.setBackground(mSelectedBlue);
                boldItalicStyle.setForeground(mBoneWhite);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Remove highlights
                boldItalicStyle.setBackground(null);
                boldItalicStyle.setForeground(null);
            }
        });

        listOfFontStylesPanel.add(boldItalicStyle);

        JScrollPane scrollPane = new JScrollPane(listOfFontStylesPanel);
        fontStylePanel.add(scrollPane, "grow, pushy");
        add(fontStylePanel, "cell 1 1, growy, w 100!");
    }

    private void addFontSizeChooser() {
        // Display the current font size and list of font sizes to choose from
        JPanel fontSizePanel = new JPanel(new MigLayout("insets 0, fillx, wrap 1"));

        JLabel fontSizeLabel = new JLabel("Font Size: ");
        add(fontSizeLabel, "cell 2 0");

        mCurrentFontSizeField = new JTextField(
                Integer.toString(mSource.getMTextArea().getFont().getSize())
        );
        mCurrentFontSizeField.setEditable(false);
        fontSizePanel.add(mCurrentFontSizeField, "growx");

        // Create list of font sizes to choose from
        JPanel listOfFontSizesPanel = new JPanel(new MigLayout("insets 0, wrap 1", "[grow, fill]"));
        listOfFontSizesPanel.setBackground(Color.WHITE);

        // List of available font sizes will be from 8 -> 72 with increments of 2
        for (int i = 8; i <= 72; i += 2) {
            JLabel fontSizeValueLabel = new JLabel(Integer.toString(i));
            fontSizeValueLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Update current font size field
                    mCurrentFontSizeField.setText(fontSizeValueLabel.getText());
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // Add blue highlights
                    fontSizeValueLabel.setOpaque(true);
                    fontSizeValueLabel.setBackground(mSelectedBlue);
                    fontSizeValueLabel.setForeground(mBoneWhite);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // Remove highlights
                    fontSizeValueLabel.setBackground(null);
                    fontSizeValueLabel.setForeground(null);
                }
            });

            listOfFontSizesPanel.add(fontSizeValueLabel);
        }

        JScrollPane scrollPane = new JScrollPane(listOfFontSizesPanel);
        fontSizePanel.add(scrollPane, "grow, pushy");
        add(fontSizePanel, "cell 2 1, growy, w 90!");
    }

    private void addFontColorChooser() {
        // Display the current colour of the text
        mCurrentColourBox = new JPanel();
        mCurrentColourBox.setBackground(mSource.getMTextArea().getForeground());
        mCurrentColourBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(mCurrentColourBox, "cell 0 2, split 2");

        JButton chooseColourButton = new JButton("Choose Colour");
        chooseColourButton.addActionListener(_ -> {
            Color colour = JColorChooser.showDialog(FontMenu.this, "Select a colour", Color.BLACK);

            // Update the color to the selected colour
            mCurrentColourBox.setBackground(colour);
        });

        add(chooseColourButton, "gapleft 8, wrap");
    }
}
