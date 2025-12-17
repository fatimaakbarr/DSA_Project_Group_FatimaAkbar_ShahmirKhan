// ===== ResourcePanel.java =====
// src/java/com/campusconnect/gui/ResourcePanel.java
package com.campusconnect.gui;

import com.campusconnect.CampusConnectJNI;
import com.campusconnect.utils.StyleManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResourcePanel extends JPanel {
    private CampusConnectJNI jni;
    private JTextField idField, nameField, descField, categoryField, locationField, reportedByField, searchField;
    private JComboBox<String> statusCombo, searchTypeCombo;
    private JTable resourceTable;
    private DefaultTableModel tableModel;
    
    public ResourcePanel(CampusConnectJNI jni) {
        this.jni = jni;
        setLayout(new BorderLayout(20, 20));
        setBackground(StyleManager.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        add(createInputPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }
    
    private JPanel createInputPanel() {
        JPanel panel = StyleManager.createCard();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = StyleManager.createHeadingLabel("Lost & Found Resource Tracker (Trie + KMP)");
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1; gbc.gridy = 1;
        
        gbc.gridx = 0; panel.add(StyleManager.createLabel("ID:"), gbc);
        gbc.gridx = 1; idField = StyleManager.createStyledTextField(); panel.add(idField, gbc);
        gbc.gridx = 2; panel.add(StyleManager.createLabel("Item Name:"), gbc);
        gbc.gridx = 3; nameField = StyleManager.createStyledTextField(); panel.add(nameField, gbc);
        
        gbc.gridy = 2;
        gbc.gridx = 0; panel.add(StyleManager.createLabel("Description:"), gbc);
        gbc.gridx = 1; descField = StyleManager.createStyledTextField(); panel.add(descField, gbc);
        gbc.gridx = 2; panel.add(StyleManager.createLabel("Category:"), gbc);
        gbc.gridx = 3; categoryField = StyleManager.createStyledTextField(); panel.add(categoryField, gbc);
        
        gbc.gridy = 3;
        gbc.gridx = 0; panel.add(StyleManager.createLabel("Location:"), gbc);
        gbc.gridx = 1; locationField = StyleManager.createStyledTextField(); panel.add(locationField, gbc);
        gbc.gridx = 2; panel.add(StyleManager.createLabel("Status:"), gbc);
        gbc.gridx = 3; statusCombo = StyleManager.createStyledComboBox(new String[]{"Lost", "Found", "Available"}); panel.add(statusCombo, gbc);
        
        gbc.gridy = 4;
        gbc.gridx = 0; panel.add(StyleManager.createLabel("Reported By:"), gbc);
        gbc.gridx = 1; reportedByField = StyleManager.createStyledTextField(); panel.add(reportedByField, gbc);
        
        gbc.gridx = 2;
        JButton addBtn = StyleManager.createPrimaryButton("Add Item");
        addBtn.addActionListener(e -> addResource());
        panel.add(addBtn, gbc);
        
        // Search panel
        gbc.gridy = 5; gbc.gridx = 0;
        panel.add(StyleManager.createLabel("Search:"), gbc);
        gbc.gridx = 1; searchField = StyleManager.createStyledTextField(); panel.add(searchField, gbc);
        gbc.gridx = 2;
        searchTypeCombo = StyleManager.createStyledComboBox(new String[]{"By Name", "By Prefix", "By Description"});
        panel.add(searchTypeCombo, gbc);
        gbc.gridx = 3;
        JButton searchBtn = StyleManager.createSecondaryButton("Search");
        searchBtn.addActionListener(e -> searchResource());
        panel.add(searchBtn, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = StyleManager.createCard();
        panel.setLayout(new BorderLayout(10, 10));
        
        JLabel label = StyleManager.createHeadingLabel("Resource Records");
        panel.add(label, BorderLayout.NORTH);
        
        String[] columns = {"ID", "Name", "Description", "Category", "Location", "Status", "Reported By", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        resourceTable = new JTable(tableModel);
        resourceTable.setFont(StyleManager.FONT_BODY);
        resourceTable.setRowHeight(30);
        resourceTable.getTableHeader().setFont(StyleManager.FONT_SUBHEADING);
        resourceTable.getTableHeader().setBackground(StyleManager.PRIMARY_LIGHT);
        resourceTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = StyleManager.createStyledScrollPane(resourceTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void addResource() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String desc = descField.getText().trim();
        String category = categoryField.getText().trim();
        String location = locationField.getText().trim();
        String status = (String) statusCombo.getSelectedItem();
        String reportedBy = reportedByField.getText().trim();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        
        if (id.isEmpty() || name.isEmpty()) {
            StyleManager.showWarning(this, "ID and Name are required!");
            return;
        }
        
        jni.addResource(id, name, desc, category, location, status, reportedBy, date);
        StyleManager.showSuccess(this, "Resource added successfully!");
        clearFields();
    }
    
    private void searchResource() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            StyleManager.showWarning(this, "Enter search term!");
            return;
        }
        
        tableModel.setRowCount(0);
        String result = "";
        String type = (String) searchTypeCombo.getSelectedItem();
        
        if (type.equals("By Name")) {
            result = jni.searchResource(query);
        } else if (type.equals("By Prefix")) {
            result = jni.searchResourceByPrefix(query);
        } else {
            result = jni.searchResourceByDescription(query);
        }
        
        if (!result.isEmpty()) {
            String[] items = result.split(";");
            for (String item : items) {
                String[] parts = item.split("\\|");
                if (parts.length >= 8) tableModel.addRow(parts);
            }
        } else {
            StyleManager.showWarning(this, "No items found!");
        }
    }
    
    private void clearFields() {
        idField.setText(""); nameField.setText(""); descField.setText("");
        categoryField.setText(""); locationField.setText(""); reportedByField.setText("");
    }
}