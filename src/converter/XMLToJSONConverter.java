package converter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class XMLToJSONConverter extends JFrame {
    private JTextArea xmlTextArea;
    private JTextArea jsonTextArea;
    private JButton convertButton;
    private JButton loadButton;
    private JButton saveButton;

    public XMLToJSONConverter() {
        super("XML to JSON Converter");
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 600);

        // Create split pane for input/output
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        xmlTextArea = new JTextArea();
        inputPanel.add(new JLabel("XML Input:"), BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(xmlTextArea), BorderLayout.CENTER);
        
        // Output panel
        JPanel outputPanel = new JPanel(new BorderLayout());
        jsonTextArea = new JTextArea();
        jsonTextArea.setEditable(false);
        outputPanel.add(new JLabel("JSON Output:"), BorderLayout.NORTH);
        outputPanel.add(new JScrollPane(jsonTextArea), BorderLayout.CENTER);

        // Add panels to split pane
        splitPane.setLeftComponent(inputPanel);
        splitPane.setRightComponent(outputPanel);
        splitPane.setDividerLocation(400);

        // Button panel
        JPanel buttonPanel = new JPanel();
        loadButton = new JButton("Load XML");
        convertButton = new JButton("Convert");
        saveButton = new JButton("Save JSON");
        
        buttonPanel.add(loadButton);
        buttonPanel.add(convertButton);
        buttonPanel.add(saveButton);

        // Add components to frame
        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add button listeners
        loadButton.addActionListener(e -> loadXML());
        convertButton.addActionListener(e -> convertXMLtoJSON());
        saveButton.addActionListener(e -> saveJSON());
    }

    private void loadXML() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                reader.close();
                xmlTextArea.setText(content.toString());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void convertXMLtoJSON() {
        try {
            String xmlContent = xmlTextArea.getText();
            XMLParser parser = XMLParser.createParser(xmlContent);
            parser.document();
            jsonTextArea.setText(parser.getJsonOutput());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error converting XML: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveJSON() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".json")) {
                    file = new File(file.getPath() + ".json");
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(jsonTextArea.getText());
                writer.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new XMLToJSONConverter().setVisible(true);
        });
    }
}