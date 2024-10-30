package com.notepad.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FontMenu extends JDialog {
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
        setSize(500, 350);
        setLocationRelativeTo(source); // Launch menu at center of notepad GUI
        setResizable(false);
        setModal(true);

        // Remove layout to give us more control on the placement of our components
        setLayout(null);

        addFontMenuComponents();
    }

    private void addFontMenuComponents() {
        JButton applyButton = new JButton("Apply");
        applyButton.setBounds(280, 275, 90, 25);
        applyButton.addActionListener(_ -> apply());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(385, 275, 90, 25);
        cancelButton.addActionListener(_ -> cancel());

        add(applyButton);
        add(cancelButton);
        addFontChooser();
        addFontStyleChooser();
        addFontSizeChooser();
        addFontColorChooser();
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
        JLabel fontLabel = new JLabel("Font:");
        fontLabel.setBounds(10, 5, 125, 15);
        add(fontLabel);

        // Font panel will display the current font and the list of fonts available to choose from
        JPanel fontPanel = new JPanel();
        fontPanel.setBounds(10, 20, 205, 160);

        // Display the current font
        currentFontField = new JTextField(source.getTextArea().getFont().getFontName());
        currentFontField.setPreferredSize(new Dimension(205, 25));
        currentFontField.setEditable(false);
        fontPanel.add(currentFontField);

        // Display the list of available fonts
        JPanel listOfFontsPanel = new JPanel();

        // Change the layout to only have one column to display each font properly
        listOfFontsPanel.setLayout(new BoxLayout(listOfFontsPanel, BoxLayout.Y_AXIS));

        // Change the background colour to white
        listOfFontsPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(listOfFontsPanel);
        scrollPane.setPreferredSize(new Dimension(205, 125));

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
                    fontNameLabel.setBackground(Color.BLUE);
                    fontNameLabel.setForeground(Color.WHITE);
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

        fontPanel.add(scrollPane);

        add(fontPanel);
    }

    private void addFontStyleChooser() {
        JLabel fontStyleLabel = new JLabel("Font Style:");
        fontStyleLabel.setBounds(220, 5, 125, 15);
        add(fontStyleLabel);

        // Will display the current font style and all available font styles
        JPanel fontStylePanel = new JPanel();
        fontStylePanel.setBounds(220, 20, 125, 160);

        // Get the current font style
        int currentFontStyle = source.getTextArea().getFont().getStyle();
        String currentFontStyleText = switch (currentFontStyle) {
            case Font.PLAIN -> "Plain";
            case Font.BOLD -> "Bold";
            case Font.ITALIC -> "Italic";
            default -> "Bold Italic";
        };

        currentFontStyleField = new JTextField(currentFontStyleText);
        currentFontStyleField.setPreferredSize(new Dimension(125, 25));
        currentFontStyleField.setEditable(false);
        fontStylePanel.add(currentFontStyleField);

        // Display the list of all font styles available
        JPanel listOfFontStylesPanel = new JPanel();

        // Make the layout have only one column (similar to the font names)
        listOfFontStylesPanel.setLayout(new BoxLayout(listOfFontStylesPanel, BoxLayout.Y_AXIS));
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
                plainStyle.setBackground(Color.BLUE);
                plainStyle.setForeground(Color.WHITE);
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
                boldStyle.setBackground(Color.BLUE);
                boldStyle.setForeground(Color.WHITE);
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
                italicStyle.setBackground(Color.BLUE);
                italicStyle.setForeground(Color.WHITE);
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
                boldItalicStyle.setBackground(Color.BLUE);
                boldItalicStyle.setForeground(Color.WHITE);
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
        scrollPane.setPreferredSize(new Dimension(125, 125));
        fontStylePanel.add(scrollPane);

        add(fontStylePanel);
    }

    private void addFontSizeChooser() {
        JLabel fontSizeLabel = new JLabel("Font Size: ");
        fontSizeLabel.setBounds(350, 5, 125, 15);
        add(fontSizeLabel);

        // Display the current font size and list of font sizes to choose from
        JPanel fontSizePanel = new JPanel();
        fontSizePanel.setBounds(350, 20, 125, 160);

        currentFontSizeField = new JTextField(
                Integer.toString(source.getTextArea().getFont().getSize())
        );
        currentFontSizeField.setPreferredSize(new Dimension(125, 25));
        currentFontSizeField.setEditable(false);
        fontSizePanel.add(currentFontSizeField);

        // Create list of font sizes to choose from
        JPanel listOfFontSizesPanel = new JPanel();
        listOfFontSizesPanel.setLayout(new BoxLayout(listOfFontSizesPanel, BoxLayout.Y_AXIS));
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
                    fontSizeValueLabel.setBackground(Color.BLUE);
                    fontSizeValueLabel.setForeground(Color.WHITE);
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
        scrollPane.setPreferredSize(new Dimension(125, 125));
        fontSizePanel.add(scrollPane);

        add(fontSizePanel);
    }

    private void addFontColorChooser() {
        // Display the current colour of the text
        currentColourBox = new JPanel();
        currentColourBox.setBounds(175, 200, 23, 23);
        currentColourBox.setBackground(source.getTextArea().getForeground());
        currentColourBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(currentColourBox);

        JButton chooseColourButton = new JButton("Choose Colour");
        chooseColourButton.setBounds(10, 200, 150, 25);
        chooseColourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color c = JColorChooser.showDialog(FontMenu.this, "Select a colour", Color.BLACK);

                // Update the color to the selected colour
                currentColourBox.setBackground(c);
            }
        });

        add(chooseColourButton);
    }
}
