
// ===== SchedulingPanel.java =====
// src/java/com/campusconnect/gui/SchedulingPanel.java
package com.campusconnect.gui;

import com.campusconnect.CampusConnectJNI;
import com.campusconnect.utils.StyleManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SchedulingPanel extends JPanel {
    private CampusConnectJNI jni;
    private JTextField codeField, nameField, instructorField, creditsField, enrolledField, capacityField, priorityField, timeField, roomField;
    private JTable courseTable;
    private DefaultTableModel tableModel;
    
    public SchedulingPanel(CampusConnectJNI jni) {
        this.jni = jni;
        setLayout(new BorderLayout(20, 20));
        setBackground(StyleManager.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        add(createInputPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        loadAllCourses();
    }
    
    private JPanel createInputPanel() {
        JPanel panel = StyleManager.createCard();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = StyleManager.createHeadingLabel("Course Scheduling System (Priority Queue)");
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1; gbc.gridy = 1;
        
        // Row 1
        gbc.gridx = 0; panel.add(StyleManager.createLabel("Code:"), gbc);
        gbc.gridx = 1; codeField = StyleManager.createStyledTextField(); panel.add(codeField, gbc);
        gbc.gridx = 2; panel.add(StyleManager.createLabel("Name:"), gbc);
        gbc.gridx = 3; nameField = StyleManager.createStyledTextField(); panel.add(nameField, gbc);
        
        // Row 2
        gbc.gridy = 2;
        gbc.gridx = 0; panel.add(StyleManager.createLabel("Instructor:"), gbc);
        gbc.gridx = 1; instructorField = StyleManager.createStyledTextField(); panel.add(instructorField, gbc);
        gbc.gridx = 2; panel.add(StyleManager.createLabel("Credits:"), gbc);
        gbc.gridx = 3; creditsField = StyleManager.createStyledTextField(); panel.add(creditsField, gbc);
        
        // Row 3
        gbc.gridy = 3;
        gbc.gridx = 0; panel.add(StyleManager.createLabel("Enrolled:"), gbc);
        gbc.gridx = 1; enrolledField = StyleManager.createStyledTextField(); panel.add(enrolledField, gbc);
        gbc.gridx = 2; panel.add(StyleManager.createLabel("Capacity:"), gbc);
        gbc.gridx = 3; capacityField = StyleManager.createStyledTextField(); panel.add(capacityField, gbc);
        
        // Row 4
        gbc.gridy = 4;
        gbc.gridx = 0; panel.add(StyleManager.createLabel("Priority:"), gbc);
        gbc.gridx = 1; priorityField = StyleManager.createStyledTextField(); panel.add(priorityField, gbc);
        gbc.gridx = 2; panel.add(StyleManager.createLabel("Time Slot:"), gbc);
        gbc.gridx = 3; timeField = StyleManager.createStyledTextField(); panel.add(timeField, gbc);
        
        // Row 5
        gbc.gridy = 5;
        gbc.gridx = 0; panel.add(StyleManager.createLabel("Room:"), gbc);
        gbc.gridx = 1; roomField = StyleManager.createStyledTextField(); panel.add(roomField, gbc);
        
        gbc.gridx = 2;
        JButton addBtn = StyleManager.createPrimaryButton("Add Course");
        addBtn.addActionListener(e -> addCourse());
        panel.add(addBtn, gbc);
        
        gbc.gridx = 3;
        JButton refreshBtn = StyleManager.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> loadAllCourses());
        panel.add(refreshBtn, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = StyleManager.createCard();
        panel.setLayout(new BorderLayout(10, 10));
        
        JLabel label = StyleManager.createHeadingLabel("Course Schedule (Max-Heap by Priority)");
        panel.add(label, BorderLayout.NORTH);
        
        String[] columns = {"Code", "Name", "Instructor", "Credits", "Enrolled", "Capacity", "Priority", "Time", "Room"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        courseTable = new JTable(tableModel);
        courseTable.setFont(StyleManager.FONT_BODY);
        courseTable.setRowHeight(30);
        courseTable.getTableHeader().setFont(StyleManager.FONT_SUBHEADING);
        courseTable.getTableHeader().setBackground(StyleManager.PRIMARY_LIGHT);
        courseTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = StyleManager.createStyledScrollPane(courseTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void addCourse() {
        try {
            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            String instructor = instructorField.getText().trim();
            int credits = Integer.parseInt(creditsField.getText().trim());
            int enrolled = Integer.parseInt(enrolledField.getText().trim());
            int capacity = Integer.parseInt(capacityField.getText().trim());
            int priority = Integer.parseInt(priorityField.getText().trim());
            String time = timeField.getText().trim();
            String room = roomField.getText().trim();
            
            if (code.isEmpty() || name.isEmpty()) {
                StyleManager.showWarning(this, "Code and Name are required!");
                return;
            }
            
            jni.addCourse(code, name, instructor, credits, enrolled, capacity, priority, time, room);
            StyleManager.showSuccess(this, "Course added successfully!");
            clearFields();
            loadAllCourses();
            
        } catch (NumberFormatException ex) {
            StyleManager.showError(this, "Invalid number format!");
        }
    }
    
    private void loadAllCourses() {
        tableModel.setRowCount(0);
        String result = jni.getAllCourses();
        if (!result.isEmpty()) {
            String[] courses = result.split(";");
            for (String course : courses) {
                String[] parts = course.split("\\|");
                if (parts.length >= 9) tableModel.addRow(parts);
            }
        }
    }
    
    private void clearFields() {
        codeField.setText(""); nameField.setText(""); instructorField.setText("");
        creditsField.setText(""); enrolledField.setText(""); capacityField.setText("");
        priorityField.setText(""); timeField.setText(""); roomField.setText("");
    }
}
