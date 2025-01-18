package converter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.logging.*;

public class XMLToJSONConverter extends JFrame {
    private static final Logger logger = Logger.getLogger(XMLToJSONConverter.class.getName());
    private JTextArea xmlTextArea;
    private JTextArea jsonTextArea;
    private JButton convertToJsonButton;
    private JButton convertToXmlButton;
    private JButton loadXmlButton;
    private JButton loadJsonButton;
    private JButton saveButton;
    private JLabel statusBar; // Barre de statut pour afficher des messages

    public XMLToJSONConverter() {
        super("XML to JSON Converter");
        configureLogger();
        initializeUI();
    }

    private void configureLogger() {
        try {
            FileHandler fileHandler = new FileHandler("XMLToJSONConverter.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            logger.info("Logger initialized for XMLToJSONConverter.");
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 600);

        // Créer un panneau divisé pour l'entrée/sortie
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // Panneau d'entrée pour XML
        JPanel inputPanel = new JPanel(new BorderLayout());
        xmlTextArea = new JTextArea();
        xmlTextArea.setEditable(true);
        inputPanel.add(new JLabel("XML :"), BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(xmlTextArea), BorderLayout.CENTER);

        // Panneau de sortie pour JSON
        JPanel outputPanel = new JPanel(new BorderLayout());
        jsonTextArea = new JTextArea();
        jsonTextArea.setEditable(true);
        outputPanel.add(new JLabel("JSON :"), BorderLayout.NORTH);
        outputPanel.add(new JScrollPane(jsonTextArea), BorderLayout.CENTER);

        // Ajouter les panneaux au panneau divisé
        splitPane.setLeftComponent(inputPanel);
        splitPane.setRightComponent(outputPanel);
        splitPane.setDividerLocation(400);

        // Panneau de boutons
        JPanel buttonPanel = new JPanel();
        loadXmlButton = new JButton("Load XML");
        loadJsonButton = new JButton("Load JSON");
        convertToJsonButton = new JButton("Convert to JSON");
        convertToXmlButton = new JButton("Convert to XML");
        saveButton = new JButton("Save JSON");

        buttonPanel.add(loadXmlButton);
        buttonPanel.add(loadJsonButton);
        buttonPanel.add(convertToJsonButton);
        buttonPanel.add(convertToXmlButton);
        buttonPanel.add(saveButton);

        // Barre de statut
        statusBar = new JLabel("Ready");
        statusBar.setBorder(BorderFactory.createEtchedBorder());

        // Ajouter les composants à la fenêtre
        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(statusBar, BorderLayout.NORTH);

        // Ajouter les écouteurs d'événements
        loadXmlButton.addActionListener(e -> loadXML());
        loadJsonButton.addActionListener(e -> loadJSON());
        convertToJsonButton.addActionListener(e -> convertXMLtoJSON());
        convertToXmlButton.addActionListener(e -> convertJSONtoXML());
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
                statusBar.setText("XML file loaded: " + file.getName());
                logger.info("XML file loaded successfully: " + file.getAbsolutePath());
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Error loading XML file", ex);
                JOptionPane.showMessageDialog(this, "Error loading file: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                statusBar.setText("Failed to load XML file.");
            }
        }
    }

    private void loadJSON() {
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
                jsonTextArea.setText(content.toString());
                statusBar.setText("JSON file loaded: " + file.getName());
                logger.info("JSON file loaded successfully: " + file.getAbsolutePath());
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Error loading JSON file", ex);
                JOptionPane.showMessageDialog(this, "Error loading file: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                statusBar.setText("Failed to load JSON file.");
            }
        }
    }

    private void convertXMLtoJSON() {
        try {
            String xmlContent = xmlTextArea.getText();
            if (xmlContent.trim().isEmpty()) {
                throw new IllegalArgumentException("XML input is empty.");
            }
            XMLParser parser = XMLParser.createParser(xmlContent);
            parser.document();
            jsonTextArea.setText(parser.getJsonOutput());
            statusBar.setText("XML converted to JSON successfully.");
            logger.info("XML successfully converted to JSON.");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error converting XML to JSON", ex);
            JOptionPane.showMessageDialog(this, "Error converting XML: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            statusBar.setText("Failed to convert XML to JSON.");
        }
    }

    private void convertJSONtoXML() {
        try {
            String jsonContent = jsonTextArea.getText();
            if (jsonContent.trim().isEmpty()) {
                throw new IllegalArgumentException("JSON input is empty.");
            }
            JsonParser parser = JsonParser.createParser(jsonContent);
            parser.document();
            xmlTextArea.setText(parser.getXmlOutput());
            statusBar.setText("JSON converted to XML successfully.");
            logger.info("JSON successfully converted to XML.");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error converting JSON to XML", ex);
            JOptionPane.showMessageDialog(this, "Error converting JSON: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            statusBar.setText("Failed to convert JSON to XML.");
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
                statusBar.setText("JSON file saved: " + file.getName());
                logger.info("JSON file saved successfully: " + file.getAbsolutePath());
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Error saving JSON file", ex);
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                statusBar.setText("Failed to save JSON file.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new XMLToJSONConverter().setVisible(true);
        });
    }
}