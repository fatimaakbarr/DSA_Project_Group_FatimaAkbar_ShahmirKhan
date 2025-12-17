// src/java/com/campusconnect/gui/NavigationPanel.java
package com.campusconnect.gui;

import com.campusconnect.CampusConnectJNI;
import com.campusconnect.utils.StyleManager;

import javax.swing.*;
import java.awt.*;

public class NavigationPanel extends JPanel {
    private CampusConnectJNI jni;
    private JComboBox<String> fromCombo, toCombo;
    private JTextArea resultArea;
    
    public NavigationPanel(CampusConnectJNI jni) {
        this.jni = jni;
        setLayout(new BorderLayout(20, 20));
        setBackground(StyleManager.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        add(createControlPanel(), BorderLayout.NORTH);
        add(createResultPanel(), BorderLayout.CENTER);
    }
    
    private JPanel createControlPanel() {
        JPanel panel = StyleManager.createCard();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = StyleManager.createHeadingLabel("Campus Navigation System");
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        panel.add(titleLabel, gbc);
        
        // From location
        gbc.gridwidth = 1; gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(StyleManager.createLabel("From:"), gbc);
        
        gbc.gridx = 1;
        String[] locations = getLocations();
        fromCombo = StyleManager.createStyledComboBox(locations);
        panel.add(fromCombo, gbc);
        
        // To location
        gbc.gridx = 2;
        panel.add(StyleManager.createLabel("To:"), gbc);
        
        gbc.gridx = 3;
        toCombo = StyleManager.createStyledComboBox(locations);
        panel.add(toCombo, gbc);
        
        // Buttons
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2;
        JButton findPathBtn = StyleManager.createPrimaryButton("Find Shortest Path");
        findPathBtn.addActionListener(e -> findShortestPath());
        panel.add(findPathBtn, gbc);
        
        gbc.gridx = 2; gbc.gridwidth = 2;
        JButton addLocationBtn = StyleManager.createSecondaryButton("Add Location");
        addLocationBtn.addActionListener(e -> addNewLocation());
        panel.add(addLocationBtn, gbc);
        
        return panel;
    }
    
    private JPanel createResultPanel() {
        JPanel panel = StyleManager.createCard();
        panel.setLayout(new BorderLayout(10, 10));
        
        JLabel label = StyleManager.createHeadingLabel("Route Information");
        panel.add(label, BorderLayout.NORTH);
        
        resultArea = StyleManager.createStyledTextArea(15, 40);
        resultArea.setEditable(false);
        resultArea.setText("Select start and destination to find the shortest path.\n\n" +
                          "Algorithm Used: Dijkstra's Shortest Path (Level-2 DSA)\n" +
                          "Data Structure: Graph with Adjacency List + Hash Table (Level-1 DSA)\n\n" +
                          "Available Locations:\n" + getAllLocationsInfo());
        
        JScrollPane scrollPane = StyleManager.createStyledScrollPane(resultArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void findShortestPath() {
        String from = (String) fromCombo.getSelectedItem();
        String to = (String) toCombo.getSelectedItem();
        
        if (from == null || to == null || from.equals(to)) {
            StyleManager.showWarning(this, "Please select different start and destination locations.");
            return;
        }
        
        String result = jni.findShortestPath(from, to);
        
        if (result.startsWith("ERROR")) {
            resultArea.setText("❌ " + result.substring(6));
        } else {
            String[] parts = result.split("\\|");
            String distance = parts[0].substring(9);
            String path = parts[1].substring(5);
            
            StringBuilder output = new StringBuilder();
            output.append("✅ SHORTEST PATH FOUND\n");
            output.append("═══════════════════════════════════════\n\n");
            output.append("From: ").append(from).append("\n");
            output.append("To: ").append(to).append("\n");
            output.append("Total Distance: ").append(distance).append(" meters\n\n");
            output.append("Route:\n");
            
            String[] locations = path.split(",");
            for (int i = 0; i < locations.length; i++) {
                output.append((i + 1)).append(". ").append(locations[i]);
                if (i < locations.length - 1) {
                    output.append(" → ");
                }
                output.append("\n");
            }
            
            output.append("\n📊 Algorithm: Dijkstra's Shortest Path Algorithm\n");
            output.append("⏱️ Time Complexity: O((V + E) log V)\n");
            output.append("🗂️ Space Complexity: O(V)\n");
            
            resultArea.setText(output.toString());
        }
    }
    
    private void addNewLocation() {
        JTextField nameField = StyleManager.createStyledTextField();
        JTextField xField = StyleManager.createStyledTextField();
        JTextField yField = StyleManager.createStyledTextField();
        
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.add(StyleManager.createLabel("Location Name:"));
        inputPanel.add(nameField);
        inputPanel.add(StyleManager.createLabel("X Coordinate:"));
        inputPanel.add(xField);
        inputPanel.add(StyleManager.createLabel("Y Coordinate:"));
        inputPanel.add(yField);
        
        int result = JOptionPane.showConfirmDialog(this, inputPanel, 
                    "Add New Location", JOptionPane.OK_CANCEL_OPTION, 
                    JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                double x = Double.parseDouble(xField.getText());
                double y = Double.parseDouble(yField.getText());
                
                if (name.isEmpty()) {
                    StyleManager.showWarning(this, "Location name cannot be empty!");
                    return;
                }
                
                jni.addLocation(name, x, y);
                refreshLocationComboBoxes();
                StyleManager.showSuccess(this, "Location added successfully!");
                
            } catch (NumberFormatException ex) {
                StyleManager.showError(this, "Invalid coordinates! Please enter numeric values.");
            }
        }
    }
    
    private String[] getLocations() {
        String locationsStr = jni.getAllLocations();
        if (locationsStr.isEmpty()) {
            return new String[]{"No locations available"};
        }
        return locationsStr.split(",");
    }
    
    private String getAllLocationsInfo() {
        String[] locations = getLocations();
        StringBuilder info = new StringBuilder();
        for (String loc : locations) {
            info.append("  • ").append(loc).append("\n");
        }
        return info.toString();
    }
    
    private void refreshLocationComboBoxes() {
        String[] locations = getLocations();
        fromCombo.setModel(new DefaultComboBoxModel<>(locations));
        toCombo.setModel(new DefaultComboBoxModel<>(locations));
        resultArea.setText("Select start and destination to find the shortest path.\n\n" +
                          "Available Locations:\n" + getAllLocationsInfo());
    }
}