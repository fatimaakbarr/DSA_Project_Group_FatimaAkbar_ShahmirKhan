// ===== StudentPanel.java =====
// src/java/com/campusconnect/gui/StudentPanel.java
package com.campusconnect.gui;

import com.campusconnect.CampusConnectJNI;
import com.campusconnect.utils.StyleManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentPanel extends JPanel {
    private CampusConnectJNI jni;
    private JTextField idField, nameField, deptField, semField, cgpaField, emailField, phoneField;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    
    public StudentPanel(CampusConnectJNI jni) {
        this.jni = jni;
        setLayout(new BorderLayout(20, 20));
        setBackground(StyleManager.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        add(createInputPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        loadAllStudents();
    }
    
    private JPanel createInputPanel() {
        JPanel panel = StyleManager.createCard();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = StyleManager.createHeadingLabel("Student Information System");
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1; gbc.gridy = 1;
        
        // Row 1
        gbc.gridx = 0; panel.add(StyleManager.createLabel("ID:"), gbc);
        gbc.gridx = 1; idField = StyleManager.createStyledTextField(); panel.add(idField, gbc);
        gbc.gridx = 2; panel.add(StyleManager.createLabel("Name:"), gbc);
        gbc.gridx = 3; nameField = StyleManager.createStyledTextField(); panel.add(nameField, gbc);
        
        // Row 2
        gbc.gridy = 2;
        gbc.gridx = 0; panel.add(StyleManager.createLabel("Department:"), gbc);
        gbc.gridx = 1; deptField = StyleManager.createStyledTextField(); panel.add(deptField, gbc);
        gbc.gridx = 2; panel.add(StyleManager.createLabel("Semester:"), gbc);
        gbc.gridx = 3; semField = StyleManager.createStyledTextField(); panel.add(semField, gbc);
        
        // Row 3
        gbc.gridy = 3;
        gbc.gridx = 0; panel.add(StyleManager.createLabel("CGPA:"), gbc);
        gbc.gridx = 1; cgpaField = StyleManager.createStyledTextField(); panel.add(cgpaField, gbc);
        gbc.gridx = 2; panel.add(StyleManager.createLabel("Email:"), gbc);
        gbc.gridx = 3; emailField = StyleManager.createStyledTextField(); panel.add(emailField, gbc);
        
        // Row 4
        gbc.gridy = 4;
        gbc.gridx = 0; panel.add(StyleManager.createLabel("Phone:"), gbc);
        gbc.gridx = 1; phoneField = StyleManager.createStyledTextField(); panel.add(phoneField, gbc);
        
        // Buttons
        gbc.gridx = 2;
        JButton addBtn = StyleManager.createPrimaryButton("Add Student");
        addBtn.addActionListener(e -> addStudent());
        panel.add(addBtn, gbc);
        
        gbc.gridx = 3;
        JButton searchBtn = StyleManager.createSecondaryButton("Search");
        searchBtn.addActionListener(e -> searchStudent());
        panel.add(searchBtn, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = StyleManager.createCard();
        panel.setLayout(new BorderLayout(10, 10));
        
        JLabel label = StyleManager.createHeadingLabel("Student Records (B-Tree Storage)");
        panel.add(label, BorderLayout.NORTH);
        
        String[] columns = {"ID", "Name", "Department", "Semester", "CGPA", "Email", "Phone"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        studentTable = new JTable(tableModel);
        studentTable.setFont(StyleManager.FONT_BODY);
        studentTable.setRowHeight(30);
        studentTable.getTableHeader().setFont(StyleManager.FONT_SUBHEADING);
        studentTable.getTableHeader().setBackground(StyleManager.PRIMARY_LIGHT);
        studentTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = StyleManager.createStyledScrollPane(studentTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Sort buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(StyleManager.SURFACE);
        
        JButton sortNameBtn = StyleManager.createIconButton("Sort by Name", StyleManager.INFO);
        sortNameBtn.addActionListener(e -> sortByName());
        
        JButton sortCGPABtn = StyleManager.createIconButton("Sort by CGPA", StyleManager.SUCCESS);
        sortCGPABtn.addActionListener(e -> sortByCGPA());
        
        JButton refreshBtn = StyleManager.createIconButton("Refresh", StyleManager.SECONDARY);
        refreshBtn.addActionListener(e -> loadAllStudents());
        
        btnPanel.add(sortNameBtn);
        btnPanel.add(sortCGPABtn);
        btnPanel.add(refreshBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void addStudent() {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String dept = deptField.getText().trim();
            int sem = Integer.parseInt(semField.getText().trim());
            double cgpa = Double.parseDouble(cgpaField.getText().trim());
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            
            if (id.isEmpty() || name.isEmpty()) {
                StyleManager.showWarning(this, "ID and Name are required!");
                return;
            }
            
            jni.addStudent(id, name, dept, sem, cgpa, email, phone);
            StyleManager.showSuccess(this, "Student added successfully!");
            clearFields();
            loadAllStudents();
            
        } catch (NumberFormatException ex) {
            StyleManager.showError(this, "Invalid number format!");
        }
    }
    
    private void searchStudent() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            StyleManager.showWarning(this, "Enter Student ID to search!");
            return;
        }
        
        String result = jni.findStudent(id);
        if (result.isEmpty()) {
            StyleManager.showWarning(this, "Student not found!");
        } else {
            String[] parts = result.split("\\|");
            idField.setText(parts[0]);
            nameField.setText(parts[1]);
            deptField.setText(parts[2]);
            semField.setText(parts[3]);
            cgpaField.setText(parts[4]);
            emailField.setText(parts[5]);
            phoneField.setText(parts[6]);
        }
    }
    
    private void loadAllStudents() {
        tableModel.setRowCount(0);
        String result = jni.getAllStudents();
        if (!result.isEmpty()) {
            String[] students = result.split(";");
            for (String student : students) {
                String[] parts = student.split("\\|");
                if (parts.length >= 7) {
                    tableModel.addRow(parts);
                }
            }
        }
    }
    
    private void sortByName() {
        tableModel.setRowCount(0);
        String result = jni.sortStudentsByName();
        if (!result.isEmpty()) {
            String[] students = result.split(";");
            for (String student : students) {
                String[] parts = student.split("\\|");
                if (parts.length >= 7) tableModel.addRow(parts);
            }
        }
    }
    
    private void sortByCGPA() {
        tableModel.setRowCount(0);
        String result = jni.sortStudentsByCGPA();
        if (!result.isEmpty()) {
            String[] students = result.split(";");
            for (String student : students) {
                String[] parts = student.split("\\|");
                if (parts.length >= 7) tableModel.addRow(parts);
            }
        }
    }
    
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        deptField.setText("");
        semField.setText("");
        cgpaField.setText("");
        emailField.setText("");
        phoneField.setText("");
    }
}
