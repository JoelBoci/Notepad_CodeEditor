package com.notepad.gui;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.awt.*;
import java.io.*;
import java.util.Map;

import com.notepad.gui.operations.Operations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeEditor {

    private static final Logger logger = LoggerFactory.getLogger(CodeEditor.class);

    private JFrame frame;
    private RSyntaxTextArea codeArea;
    private String className;
    private String packageName;
    private Operations operations;

    private UndoManager undoManager;

    private final JFileChooser fileChooser;
    private File currentFile;

    private static final Map<String, String> LANGUAGE_SYNTAX_MAP = Map.of(
            "Java", SyntaxConstants.SYNTAX_STYLE_JAVA,
            "C++", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS,
            "Python", SyntaxConstants.SYNTAX_STYLE_PYTHON,
            "JavaScript", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT,
            "HTML", SyntaxConstants.SYNTAX_STYLE_HTML,
            "CSS", SyntaxConstants.SYNTAX_STYLE_CSS,
            "SQL", SyntaxConstants.SYNTAX_STYLE_SQL
    );

    public CodeEditor() {
        frame = new JFrame("Code Editor");
        operations = new Operations();

        codeArea = new RSyntaxTextArea();
        codeArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA); // Default style
        codeArea.setCodeFoldingEnabled(true);

        RTextScrollPane scrollPane = new RTextScrollPane(codeArea);
        frame.add(scrollPane);

        showLanguageSelectionDialog();

        frame.setJMenuBar(createMenuBar());

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.fileChooser = new JFileChooser();
        this.fileChooser.setFileFilter(new FileNameExtensionFilter("Java File", "java"));
        this.fileChooser.setCurrentDirectory(new File("src/codesnippets"));

        undoManager = new UndoManager();
        codeArea.getDocument().addUndoableEditListener(e -> undoManager.addEdit(e.getEdit()));

        applyEditorTheme("dark.xml");
    }

    private void showLanguageSelectionDialog() {
        JDialog languageDialog = new JDialog(frame, "Select Programming Language", true);
        languageDialog.setLayout(new GridBagLayout());
        languageDialog.setSize(new Dimension(280, 125));
        languageDialog.setResizable(false);

        JComboBox<String> languageDropdown = new JComboBox<>(LANGUAGE_SYNTAX_MAP.keySet().toArray(new String[0]));
        languageDropdown.setSelectedItem("Java");

        JButton okButton = new JButton("OK");
        okButton.addActionListener(_ -> {
            String selectedLanguage = (String) languageDropdown.getSelectedItem();
            codeArea.setSyntaxEditingStyle(LANGUAGE_SYNTAX_MAP.get(selectedLanguage));
            frame.setTitle("Code Editor (" + selectedLanguage + ")");
            languageDialog.dispose();
            frame.setVisible(true);
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(_ -> {
            languageDialog.dispose();
            frame.dispose();
        });

        languageDialog.add(languageDropdown, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

        languageDialog.add(okButton, new GridBagConstraints(0, 1, 1, 1, 0.5, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        languageDialog.add(cancelButton, new GridBagConstraints(1, 1, 1, 1, 0.5, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        languageDialog.setVisible(true);
    }

    // Method to create the menu bar
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Create "File" menu
        JMenu fileMenu = new JMenu("File");

        JMenuItem newFile = new JMenuItem("New");
        newFile.addActionListener(_ -> operations.newCodeEditor());

        JMenuItem openFile = new JMenuItem("Open");
        openFile.addActionListener(_ -> operations.openFile(frame, codeArea, fileChooser, currentFile));

        JMenuItem saveFile = new JMenuItem("Save");
        saveFile.addActionListener(_ -> operations.saveFile(frame, codeArea, fileChooser, currentFile));

        JMenuItem exitApp = new JMenuItem("Exit");
        exitApp.addActionListener(_ -> operations.exit(frame));

        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.addSeparator(); // Adds a separator line
        fileMenu.add(exitApp);

        // Create "Edit" menu
        JMenu editMenu = new JMenu("Edit");

        JMenuItem cut = new JMenuItem("Cut");
        cut.addActionListener(_ -> operations.cut(codeArea));

        JMenuItem copy = new JMenuItem("Copy");
        copy.addActionListener(_ -> operations.copy(codeArea));

        JMenuItem paste = new JMenuItem("Paste");
        paste.addActionListener(_ -> operations.paste(codeArea));

        JMenuItem undo = new JMenuItem("Undo");
        undo.addActionListener(_ -> operations.undo(undoManager));

        JMenuItem redo = new JMenuItem("Redo");
        redo.addActionListener(_ -> operations.redo(undoManager));

        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);

        // Create "Run" menu
        JMenu runMenu = new JMenu("Run");
        JMenuItem compile = new JMenuItem("Compile");
        JMenuItem run = new JMenuItem("Run");

        compile.addActionListener(_ -> compileCode());
        run.addActionListener(_ -> {
            if (className != null && packageName != null)
                runCode(packageName, className);
            else
                JOptionPane.showMessageDialog(frame, "Compile the code first.");
        });

        runMenu.add(compile);
        runMenu.add(run);

        // Create "Format" menu
        JMenu formatMenu = new JMenu("Format");
        JMenuItem reformat = new JMenuItem("Reformat");
        reformat.addActionListener(_ -> reformatCode());

        formatMenu.add(reformat);

        // Add menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(runMenu);
        menuBar.add(formatMenu);

        return menuBar;
    }

    // Method to compile the code
    private void compileCode() {
        try {
            String code = codeArea.getText();
            String packageName = extractPackageName(code);
            String className = extractClassName(code);

            if (className == null || packageName == null) {
                JOptionPane.showMessageDialog(frame, "No class or package found in the code.");
                return;
            }

            this.className = className; // Store class name
            this.packageName = packageName; // Store package name

            // Create directories based on the package name
            File sourceDir = new File("src/" + packageName.replace('.', '/'));
            sourceDir.mkdirs(); // Ensure the directory structure exists

            // Create the file in the correct directory
            File sourceFile = new File(sourceDir, className + ".java");

            // Write code to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(sourceFile))) {
                writer.write(code);
            }

            // Compile the source file
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                JOptionPane.showMessageDialog(frame, "No Java compiler found. Please make sure you are using a JDK, not a JRE.");
                return;
            }

            int result = compiler.run(null, null, null, "-d", "bin", sourceFile.getPath());

            if (result == 0)
                JOptionPane.showMessageDialog(frame, "Compilation successful.");
            else
                JOptionPane.showMessageDialog(frame, "Compilation failed.");
        } catch (IOException e) {
            logger.error("Error while compiling code.", e);
            JOptionPane.showMessageDialog(frame, "Error during compilation: " + e.getMessage());
        }
    }

    private void runCode(String packageName, String className) {
        try {
            String fullClassName = packageName.isEmpty() ? className : packageName + "." + className;

            // Run the compiled class file from the `bin` directory
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp", "bin", fullClassName);
            processBuilder.redirectErrorStream(true);

            // Start the process
            Process process = processBuilder.start();

            // Capture and display the output
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                    output.append(line).append("\n");

                JOptionPane.showMessageDialog(frame, !output.isEmpty() ? output.toString() : "No output", "Program Output", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            logger.error("Error while running code.", e);
            JOptionPane.showMessageDialog(frame, "Error running the program: " + e.getMessage());
        }
    }

    private String extractPackageName(String code) {
        String[] lines = code.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("package "))
                return line.substring("package ".length(), line.length() - 1).trim();
        }
        return ""; // No package name found
    }

    private String extractClassName(String code) {
        String[] lines = code.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("public class ")) {
                int startIndex = line.indexOf("public class ") + "public class ".length();
                int endIndex = line.indexOf(' ', startIndex);
                if (endIndex == -1)
                    endIndex = line.indexOf('{', startIndex);
                return line.substring(startIndex, endIndex).trim();
            }
        }
        return null; // No class name found
    }

    public void applyEditorTheme(String themeFile) {
        try {
            InputStream themeStream = getClass().getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/" + themeFile);
            if (themeStream != null) {
                Theme theme = Theme.load(themeStream);
                theme.apply(codeArea);
            } else {
                System.err.println("Theme file not found: " + themeFile);
            }
        } catch (Exception e) {
            logger.error("Error while trying to find theme.", e);
        }
    }

    public void reformatCode() {
        String code = codeArea.getText();
        JavaParser parser = new JavaParser();
        // Parse the code
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);

        if (cu != null) {
            String formattedCode = cu.toString(); // Convert back to string with formatting
            codeArea.setText(formattedCode);
        } else {
            JOptionPane.showMessageDialog(frame, "Error parsing code.");
        }
    }
}