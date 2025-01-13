package converter;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.List;

public class XMLToJSONConverter extends JFrame {
	
    private JTextArea xmlTextArea;
    private JTextArea jsonTextArea;
    private JButton validateButton;
    private JButton convertButton;

    public XMLToJSONConverter() {
        setTitle("XML to JSON Converter");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize text areas with borders and titles
        xmlTextArea = new JTextArea();
        jsonTextArea = new JTextArea();
        xmlTextArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("XML Input"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        jsonTextArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("JSON Output"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Create buttons
        validateButton = new JButton("Validate XML");
        convertButton = new JButton("Convert to JSON");
        
        // Button panel with some spacing
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonPanel.add(validateButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(convertButton);

        // Split pane for the text areas
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(xmlTextArea), new JScrollPane(jsonTextArea));
        splitPane.setDividerLocation(400);

        // Add components to frame
        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add button listeners
        validateButton.addActionListener(e -> validateXML());
        convertButton.addActionListener(e -> convertToJSON());
    }

    private void validateXML() {
        String xmlContent = xmlTextArea.getText();
        try {
            XMLParser parser = new XMLParser(new ByteArrayInputStream(xmlContent.getBytes()));
            parser.parse();
            JOptionPane.showMessageDialog(this, "XML is valid!", "Validation", 
                                        JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid XML: " + ex.getMessage(), 
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void convertToJSON() {
        String xmlContent = xmlTextArea.getText();
        try {
            XMLParser parser = new XMLParser(new ByteArrayInputStream(xmlContent.getBytes()));
            parser.parse();
            Map<String, Object> jsonObject = parser.getResult();
            String jsonString = toJsonString(jsonObject, 0);
            jsonTextArea.setText(jsonString);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error converting to JSON: " + ex.getMessage(),
                                        "Conversion Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String toJsonString(Object obj, int indent) {
        if (obj == null) {
            return "null";
        }

        StringBuilder json = new StringBuilder();
        String indentStr = "  ".repeat(indent);

        if (obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;
            json.append("{\n");
            boolean first = true;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (!first) {
                    json.append(",\n");
                }
                first = false;
                json.append(indentStr).append("  \"").append(entry.getKey()).append("\": ")
                    .append(toJsonString(entry.getValue(), indent + 1));
            }
            json.append("\n").append(indentStr).append("}");
        } else if (obj instanceof List) {
            List<Object> list = (List<Object>) obj;
            json.append("[\n");
            boolean first = true;
            for (Object item : list) {
                if (!first) {
                    json.append(",\n");
                }
                first = false;
                json.append(indentStr).append("  ").append(toJsonString(item, indent + 1));
            }
            json.append("\n").append(indentStr).append("]");
        } else if (obj instanceof String) {
            json.append("\"").append(escapeJsonString((String) obj)).append("\"");
        } else {
            json.append(obj.toString());
        }

        return json.toString();
    }

    private String escapeJsonString(String str) {
        return str.replace("\\", "\\\\")
                 .replace("\"", "\\\"")
                 .replace("\n", "\\n")
                 .replace("\r", "\\r")
                 .replace("\t", "\\t");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            XMLToJSONConverter converter = new XMLToJSONConverter();
            converter.setVisible(true);
        });
    }
}