package com.notepad.gui;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.notepad.operations.Operations;

import net.miginfocom.swing.MigLayout;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeEditor {

    private static final Logger mLogger = LoggerFactory.getLogger(CodeEditor.class);

    private final JFrame mFrame;
    private final RSyntaxTextArea mCodeArea;
    private final Operations mOperations;

    private final UndoManager mUndoManager;

    private final JFileChooser mFileChooser;
    private File mCurrentFile;

    private static final Map<String, String> LANGUAGE_SYNTAX_MAP = Map.of(
            "Java", SyntaxConstants.SYNTAX_STYLE_JAVA,
            "C++", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS,
            "Python", SyntaxConstants.SYNTAX_STYLE_PYTHON,
            "JavaScript", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT,
            "HTML", SyntaxConstants.SYNTAX_STYLE_HTML,
            "CSS", SyntaxConstants.SYNTAX_STYLE_CSS,
            "SQL", SyntaxConstants.SYNTAX_STYLE_SQL
    );
    private String mSelectedLanguage = "Java";

    private boolean mCompiledOk = false;
    private String mLastCompiledMainClass = null;

    public CodeEditor() {
        mFrame = new JFrame("Code Editor");
        mOperations = new Operations();

        mCodeArea = new RSyntaxTextArea();
        mCodeArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA); // Default style
        mCodeArea.setCodeFoldingEnabled(true);

        RTextScrollPane scrollPane = new RTextScrollPane(mCodeArea);
        mFrame.add(scrollPane);

        showLanguageSelectionDialog();

        mFrame.setJMenuBar(createMenuBar());

        mFrame.setSize(800, 600);
        mFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.mFileChooser = new JFileChooser();
        this.mFileChooser.setFileFilter(new FileNameExtensionFilter("Java File", "java"));
        this.mFileChooser.setCurrentDirectory(new File("src/codesnippets"));

        mUndoManager = new UndoManager();
        mCodeArea.getDocument().addUndoableEditListener(e -> mUndoManager.addEdit(e.getEdit()));
    }

    private void showLanguageSelectionDialog() {
        JDialog languageDialog = new JDialog(mFrame, "Select Language", true);
        languageDialog.setLayout(new MigLayout());
        languageDialog.setResizable(false);

        JLabel languageLabel = new JLabel("Language:");

        JComboBox<String> languageDropdown = new JComboBox<>(LANGUAGE_SYNTAX_MAP.keySet().toArray(new String[0]));
        languageDropdown.setSelectedIndex(0);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(_ -> {
            String selectedLanguage = (String) languageDropdown.getSelectedItem();
            mSelectedLanguage = selectedLanguage;
            mCodeArea.setSyntaxEditingStyle(LANGUAGE_SYNTAX_MAP.get(selectedLanguage));
            mCompiledOk = false;
            mLastCompiledMainClass = null;
            mFrame.setTitle("Code Editor (" + selectedLanguage + ")");
            languageDialog.dispose();
            mFrame.setVisible(true);
            mLogger.info("Selected language -> '{}'", selectedLanguage);
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(_ -> {
            languageDialog.dispose();
            mFrame.dispose();
        });

        languageDialog.add(languageLabel, "left, split 2");
        languageDialog.add(languageDropdown, "growx, pushx, wrap");
        languageDialog.add(okButton, "split 2");
        languageDialog.add(cancelButton);

        languageDialog.pack();
        languageDialog.setVisible(true);
    }

    // Method to create the menu bar
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Create "File" menu
        JMenu fileMenu = new JMenu("File");

        JMenuItem newFile = new JMenuItem("New");
        newFile.addActionListener(_ -> mOperations.newCodeEditor());

        JMenuItem openFile = new JMenuItem("Open");
        openFile.addActionListener(_ -> mCurrentFile = mOperations.openFile(mFrame, mCodeArea, mFileChooser, mCurrentFile));

        JMenuItem saveFile = new JMenuItem("Save");
        saveFile.addActionListener(_ -> mCurrentFile = mOperations.saveFile(mFrame, mCodeArea, mFileChooser, mCurrentFile));

        JMenuItem exitApp = new JMenuItem("Exit");
        exitApp.addActionListener(_ -> mOperations.exit(mFrame));

        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.addSeparator();
        fileMenu.add(exitApp);

        // Create "Edit" menu
        JMenu editMenu = new JMenu("Edit");

        JMenuItem cut = new JMenuItem("Cut");
        cut.addActionListener(_ -> mOperations.cut(mCodeArea));

        JMenuItem copy = new JMenuItem("Copy");
        copy.addActionListener(_ -> mOperations.copy(mCodeArea));

        JMenuItem paste = new JMenuItem("Paste");
        paste.addActionListener(_ -> mOperations.paste(mCodeArea));

        JMenuItem undo = new JMenuItem("Undo");
        undo.addActionListener(_ -> mOperations.undo(mUndoManager));

        JMenuItem redo = new JMenuItem("Redo");
        redo.addActionListener(_ -> mOperations.redo(mUndoManager));

        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);

        // Create "Run" menu
        JMenu runMenu = new JMenu("Run");
        JMenuItem compile = new JMenuItem("Compile");
        JMenuItem run = new JMenuItem("Run");

        compile.addActionListener(_ -> {
            if ("Java".equals(mSelectedLanguage))
                compileCode();
            else
                JOptionPane.showMessageDialog(mFrame, "Compile is only needed for Java (for now).");
        });

        run.addActionListener(_ -> new Thread(this::runSelectedLanguage, "CodeRunner").start());

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

        mLogger.info("Code editor menu bar created");
        return menuBar;
    }

    private void runSelectedLanguage() {
        try {
            switch (mSelectedLanguage) {
                case "Java" -> {
                    if (!mCompiledOk || mLastCompiledMainClass == null) {
                        JOptionPane.showMessageDialog(mFrame, "Compile the code first.");
                        return;
                    }
                    runCode(mLastCompiledMainClass);
                }
                case "Python" -> runScript(detectPythonCommand(), ".py");
                case "JavaScript" -> runScript("node", ".js");
                default -> JOptionPane.showMessageDialog(mFrame, "Run not supported for " + mSelectedLanguage + " yet.");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mFrame, "Run error: " + e.getMessage());
        }
    }

    private void runScript(String interpreter, String extension) {
        try {
            File dir = new File("snippets");
            if (!dir.exists() && !dir.mkdirs()) {
                JOptionPane.showMessageDialog(mFrame, "Could not create snippets directory.");
                return;
            }

            // For scripts, we don't need class name rules, just write a temp file
            File sciptFile = new File(dir, "Main" + extension);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(sciptFile))) {
                writer.write(mCodeArea.getText());
            }

            ProcessBuilder pb = new ProcessBuilder(interpreter, sciptFile.getAbsolutePath());
            pb.directory(dir);
            pb.redirectErrorStream(true);

            Process process = pb.start();
            StringBuilder output = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null)
                    output.append(line).append("\n");
            }

            String msg = !output.isEmpty() ? output.toString() : "No output";
            JOptionPane.showMessageDialog(mFrame, msg, "Program Output (exit " + process.waitFor() + ")",  JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mFrame, "Could not run " + mSelectedLanguage + ".\n\n" +
                            "Make sure '" + interpreter + "' is installed and on PATH.\n\n" + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            JOptionPane.showMessageDialog(mFrame, "Run interrupted.");
        }
    }

    private String detectPythonCommand() {
        // Windows often uses "py" launcher, mac/linux often use python3
        // Will be configurable soon
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win") ? "py" : "python3";
    }

    // Method to compile the code
    private void compileCode() {
        mCompiledOk = false;
        mLastCompiledMainClass = null;

        try {
            mLogger.info("Attempting code compilation...");
            String code = mCodeArea.getText();

            String packageName = extractPackageName(code); // "" allowed
            String className = extractClassName(code);

            if (className == null) {
                JOptionPane.showMessageDialog(mFrame, "No class found in the code.");
                mLogger.info("No class found in the code");
                return;
            }

            // Where we store the generated .java
            File sourceDir = packageName.isEmpty()
                    ? new File("snippets")
                    : new File("snippets/" + packageName.replace('.', '/'));

            if (!sourceDir.exists() && !sourceDir.mkdirs()) {
                JOptionPane.showMessageDialog(mFrame, "Could not create directory: " + sourceDir.getPath());
                return;
            }

            File sourceFile = new File(sourceDir, className + ".java");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(sourceFile))) {
                writer.write(code);
            }

            // Ensure output dir exists
            File binDir = new File("bin");
            if (!binDir.exists() && !binDir.mkdirs()) {
                JOptionPane.showMessageDialog(mFrame, "Could not create output directory: " + binDir.getPath());
                return;
            }

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                JOptionPane.showMessageDialog(mFrame, "No Java compiler found. Please use a JDK (not a JRE).");
                return;
            }

            int result = compiler.run(null, null, null, "-d", binDir.getPath(), sourceFile.getPath());

            if (result == 0) {
                mCompiledOk = true;
                mLastCompiledMainClass = packageName.isEmpty() ? className : packageName + "." + className;

                JOptionPane.showMessageDialog(mFrame, "Compilation successful.");
                mLogger.info("Code compiled successfully: {}", mLastCompiledMainClass);
            } else {
                JOptionPane.showMessageDialog(mFrame, "Compilation failed.");
                mLogger.error("Compilation failed (exit code {})", result);
            }

        } catch (IOException e) {
            mLogger.error("Error while compiling code.", e);
            JOptionPane.showMessageDialog(mFrame, "Error during compilation: " + e.getMessage());
        }
    }

    private void runCode(String fullClassName) {
        try {
            mLogger.info("Attempting to run code...");

            // Run the compiled class file from the `bin` directory
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp", "bin", fullClassName);
            processBuilder.redirectErrorStream(true);

            // Start the process
            Process process = processBuilder.start();
            mLogger.info("Starting process...");

            // Capture and display the output
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                    output.append(line).append("\n");

                JOptionPane.showMessageDialog(mFrame, !output.isEmpty() ? output.toString()
                        : "No output", "Program Output", JOptionPane.INFORMATION_MESSAGE);
            }
            mLogger.info("Code successfully run...");
        } catch (IOException e) {
            mLogger.error("Error while running code.", e);
            JOptionPane.showMessageDialog(mFrame, "Error running the program: " + e.getMessage());
        }
    }

    private String extractPackageName(String code) {
        String[] lines = code.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("package ")) {
                return line.substring("package ".length(), line.length() - 1).trim();
            }
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
                theme.apply(mCodeArea);
            } else {
                mLogger.error("Theme file not found: {}", themeFile);
            }
        } catch (Exception e) {
            mLogger.error("Error while trying to find theme.", e);
        }
    }

    public void reformatCode() {
        String code = mCodeArea.getText();
        JavaParser parser = new JavaParser();
        // Parse the code
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);

        if (cu != null) {
            String formattedCode = cu.toString(); // Convert back to string with formatting
            mCodeArea.setText(formattedCode);
        } else {
            JOptionPane.showMessageDialog(mFrame, "Error parsing code.");
        }
    }
}