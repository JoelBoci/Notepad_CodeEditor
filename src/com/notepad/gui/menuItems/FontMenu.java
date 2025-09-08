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

public class FontMenu extends JDialog {

    Color selectedBlue = new Color(80, 106, 136);
    Color boneWhite = new Color(245, 245, 245);

    // Will need a reference to our GUI to make changes to the GUI from this class
    private FormatMenu source;

    private JTextField currentFontField;
    private JTextField currentFontStyleField;
    private JTextField currentFontSizeField;

    private JPanel currentColourBox;

    public FontMenu(FormatMenu source) {
        this.source = source;
        setTitle("Font Settings");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(650, 350);
        setLocationRelativeTo(source); // Launch menu at center of notepad GUI
        setResizable(false);
        setModal(true);

        setLayout(new MigLayout("insets 10, fillx",
                "[grow,fill][pref!][pref!]",
                "[][grow][]"));

        addFontMenuComponents();
    }

    private void addFontMenuComponents() {
        JPanel buttonPanel = new JPanel(new MigLayout());
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(_ -> apply());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(_ -> cancel());

        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);

        addFontChooser();
        addFontStyleChooser();
        addFontSizeChooser();
        addFontColorChooser();
        add(buttonPanel);
    }

    private void apply() {
        // Get current font type
        String fontType = currentFontField.getText();

        // Get font style
        int fontStyle = switch (currentFontStyleField.getText()) {
            case "Plain" -> Font.PLAIN;
            case "Bold" -> Font.BOLD;
            case "Italic" -> Font.ITALIC;
            default -> Font.BOLD | Font.ITALIC;
        };

        // Get font size
        int fontSize = Integer.parseInt(currentFontSizeField.getText());

        // Get font colour
        Color fontColor = currentColourBox.getBackground();

        // Create font
        Font newFont = new Font(fontType, fontStyle, fontSize);

        // Update text area font
        source.getTextArea().setFont(newFont);

        // Update text area font color
        source.getTextArea().setForeground(fontColor);

        // Dispose menu
        FontMenu.this.dispose();
    }

    private void cancel() {
        FontMenu.this.dispose();
    }

    private void addFontChooser() {
        // Font panel will display the current font and the list of fonts available to choose from
        JPanel fontPanel = new JPanel(new MigLayout("insets 0, fillx, wrap 1"));

        JLabel fontLabel = new JLabel("Font:");
        add(fontLabel, "cell 0 0");

        // Display the current font
        currentFontField = new JTextField(source.getTextArea().getFont().getFontName());
        currentFontField.setEditable(false);
        fontPanel.add(currentFontField, "growx");

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
                    // When clicked, set the currentFontField to the font name
                    currentFontField.setText(font);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // Add highlights over font name when the mouse hovers over them
                    fontNameLabel.setOpaque(true);
                    fontNameLabel.setBackground(selectedBlue);
                    fontNameLabel.setForeground(boneWhite);
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
        int currentFontStyle = source.getTextArea().getFont().getStyle();
        String currentFontStyleText = switch (currentFontStyle) {
            case Font.PLAIN -> "Plain";
            case Font.BOLD -> "Bold";
            case Font.ITALIC -> "Italic";
            default -> "Bold Italic";
        };

        currentFontStyleField = new JTextField(currentFontStyleText);
        currentFontStyleField.setEditable(false);
        fontStylePanel.add(currentFontStyleField, "growx");

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
                currentFontStyleField.setText(plainStyle.getText());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Add blue highlight when hovering
                plainStyle.setOpaque(true);
                plainStyle.setBackground(selectedBlue);
                plainStyle.setForeground(boneWhite);
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
                currentFontStyleField.setText(boldStyle.getText());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Add blue highlight when hovering
                boldStyle.setOpaque(true);
                boldStyle.setBackground(selectedBlue);
                boldStyle.setForeground(boneWhite);
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
                currentFontStyleField.setText(italicStyle.getText());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Add blue highlight when hovering
                italicStyle.setOpaque(true);
                italicStyle.setBackground(selectedBlue);
                italicStyle.setForeground(boneWhite);
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
                currentFontStyleField.setText(boldItalicStyle.getText());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Add blue highlight when hovering
                boldItalicStyle.setOpaque(true);
                boldItalicStyle.setBackground(selectedBlue);
                boldItalicStyle.setForeground(boneWhite);
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

        currentFontSizeField = new JTextField(
                Integer.toString(source.getTextArea().getFont().getSize())
        );
        currentFontSizeField.setEditable(false);
        fontSizePanel.add(currentFontSizeField, "growx");

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
                    currentFontSizeField.setText(fontSizeValueLabel.getText());
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // Add blue highlights
                    fontSizeValueLabel.setOpaque(true);
                    fontSizeValueLabel.setBackground(selectedBlue);
                    fontSizeValueLabel.setForeground(boneWhite);
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
        currentColourBox = new JPanel();
        currentColourBox.setBackground(source.getTextArea().getForeground());
        currentColourBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(currentColourBox, "cell 0 2, split 2");

        JButton chooseColourButton = new JButton("Choose Colour");
        chooseColourButton.addActionListener(_ -> {
            Color c = JColorChooser.showDialog(FontMenu.this, "Select a colour", Color.BLACK);

            // Update the color to the selected colour
            currentColourBox.setBackground(c);
        });

        add(chooseColourButton, "gapleft 8, wrap");
    }
}
