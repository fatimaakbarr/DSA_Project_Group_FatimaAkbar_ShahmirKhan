// src/java/com/campusconnect/gui/AttendancePanel.java
package com.campusconnect.gui;

import com.campusconnect.CampusConnectJNI;
import com.campusconnect.utils.StyleManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AttendancePanel extends JPanel {
    private CampusConnectJNI jni;
    private JTextField studentIdField, courseCodeField;
    private JComboBox<String> statusCombo;
    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    private JTextArea statsArea;
    
    public AttendancePanel(CampusConnectJNI jni) {
        this.jni = jni;
        setLayout(new BorderLayout(20, 20));
        setBackground(StyleManager.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);
        topPanel.add(createInputPanel(), BorderLayout.NORTH);
        topPanel.add(createStatsPanel(), BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }
    
    private JPanel createInputPanel() {
        JPanel panel = StyleManager.createCard();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = StyleManager.createHeadingLabel("Attendance Management System");
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1; gbc.gridy = 1;
        
        // Student ID
        gbc.gridx = 0;
        panel.add(StyleManager.createLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        studentIdField = StyleManager.createStyledTextField();
        panel.add(studentIdField, gbc);
        
        // Course Code
        gbc.gridx = 2;
        panel.add(StyleManager.createLabel("Course:"), gbc);
        gbc.gridx = 3;
        courseCodeField = StyleManager.createStyledTextField();
        panel.add(courseCodeField, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(StyleManager.createLabel("Status:"), gbc);
        gbc.gridx = 1;
        statusCombo = StyleManager.createStyledComboBox(
            new String[]{"Present", "Absent", "Late"}
        );
        panel.add(statusCombo, gbc);
        
        // Buttons
        gbc.gridx = 2; gbc.gridy = 2;
        JButton markBtn = StyleManager.createPrimaryButton("Mark Attendance");
        markBtn.addActionListener(e -> markAttendance());
        panel.add(markBtn, gbc);
        
        gbc.gridx = 3;
        JButton viewBtn = StyleManager.createSecondaryButton("View Records");
        viewBtn.addActionListener(e -> viewRecords());
        panel.add(viewBtn, gbc);
        
        return panel;
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = StyleManager.createCard();
        panel.setLayout(new BorderLayout(10, 10));
        
        JLabel label = StyleManager.createHeadingLabel("Attendance Statistics");
        panel.add(label, BorderLayout.NORTH);
        
        statsArea = StyleManager.createStyledTextArea(4, 40);
        statsArea.setEditable(false);
        statsArea.setBackground(StyleManager.SURFACE_VARIANT);
        statsArea.setText("📊 DSA Implementation:\n" +
                         "• Hash Table (Level-1): Fast O(1) attendance record insertion and lookup\n" +
                         "• AVL Tree (Level-2): Balanced tree for sorting students by attendance percentage\n" +
                         "• Operations: Insert, Search, Sort, Percentage Calculation");
        
        panel.add(StyleManager.createStyledScrollPane(statsArea), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = StyleManager.createCard();
        panel.setLayout(new BorderLayout(10, 10));
        
        JLabel label = StyleManager.createHeadingLabel("Attendance Records");
        panel.add(label, BorderLayout.NORTH);
        
        String[] columns = {"Student ID", "Date", "Status", "Course", "Percentage"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        attendanceTable = new JTable(tableModel);
        attendanceTable.setFont(StyleManager.FONT_BODY);
        attendanceTable.setRowHeight(30);
        attendanceTable.getTableHeader().setFont(StyleManager.FONT_SUBHEADING);
        attendanceTable.getTableHeader().setBackground(StyleManager.PRIMARY_LIGHT);
        attendanceTable.getTableHeader().setForeground(Color.WHITE);
        attendanceTable.setSelectionBackground(StyleManager.PRIMARY_LIGHT);
        
        JScrollPane scrollPane = StyleManager.createStyledScrollPane(attendanceTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Action buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(StyleManager.SURFACE);
        
        JButton refreshBtn = StyleManager.createIconButton("Refresh", StyleManager.INFO);
        refreshBtn.addActionListener(e -> refreshTable());
        
        JButton analyzeBtn = StyleManager.createIconButton("Analyze", StyleManager.WARNING);
        analyzeBtn.addActionListener(e -> analyzeAttendance());
        
        btnPanel.add(refreshBtn);
        btnPanel.add(analyzeBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void markAttendance() {
        String studentId = studentIdField.getText().trim();
        String courseCode = courseCodeField.getText().trim();
        String status = (String) statusCombo.getSelectedItem();
        
        if (studentId.isEmpty() || courseCode.isEmpty()) {
            StyleManager.showWarning(this, "Please fill in all fields!");
            return;
        }
        
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        
        jni.markAttendance(studentId, date, status, courseCode);
        
        StyleManager.showSuccess(this, "Attendance marked successfully!");
        
        // Clear fields
        studentIdField.setText("");
        courseCodeField.setText("");
        
        refreshTable();
    }
    
    private void viewRecords() {
        String studentId = studentIdField.getText().trim();
        
        if (studentId.isEmpty()) {
            StyleManager.showWarning(this, "Please enter a Student ID!");
            return;
        }
        
        String records = jni.getAttendanceRecords(studentId);
        
        if (records.isEmpty()) {
            StyleManager.showWarning(this, "No records found for this student!");
            return;
        }
        
        tableModel.setRowCount(0);
        
        String[] recordArray = records.split(";");
        for (String record : recordArray) {
            String[] parts = record.split("\\|");
            if (parts.length >= 4) {
                String course = parts[3];
                double percentage = jni.getAttendancePercentage(studentId, course);
                tableModel.addRow(new Object[]{
                    parts[0], parts[1], parts[2], parts[3], 
                    String.format("%.1f%%", percentage)
                });
            }
        }
        
        // Update stats
        if (!courseCodeField.getText().trim().isEmpty()) {
            double perc = jni.getAttendancePercentage(studentId, courseCodeField.getText().trim());
            updateStats(studentId, courseCodeField.getText().trim(), perc);
        }
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        String idsStr = jni.getAllStudentIds();
        
        if (!idsStr.isEmpty()) {
            String[] ids = idsStr.split(",");
            for (String id : ids) {
                String records = jni.getAttendanceRecords(id);
                if (!records.isEmpty()) {
                    String[] recordArray = records.split(";");
                    for (String record : recordArray) {
                        String[] parts = record.split("\\|");
                        if (parts.length >= 4) {
                            double percentage = jni.getAttendancePercentage(parts[0], parts[3]);
                            tableModel.addRow(new Object[]{
                                parts[0], parts[1], parts[2], parts[3],
                                String.format("%.1f%%", percentage)
                            });
                        }
                    }
                }
            }
        }
    }
    
    private void analyzeAttendance() {
        String input = JOptionPane.showInputDialog(this, 
            "Enter minimum attendance threshold (%):", "75");
        
        if (input == null) return;
        
        try {
            double threshold = Double.parseDouble(input);
            String result = jni.getStudentsBelowThreshold(threshold);
            
            if (result.isEmpty()) {
                statsArea.setText("✅ All students have attendance above " + threshold + "%");
            } else {
                StringBuilder output = new StringBuilder();
                output.append("⚠️ Students Below ").append(threshold).append("% Attendance:\n\n");
                
                String[] students = result.split(";");
                for (String student : students) {
                    String[] parts = student.split("\\|");
                    if (parts.length >= 3) {
                        output.append("ID: ").append(parts[0])
                              .append(" | ").append(parts[1])
                              .append(" | ").append(parts[2]).append("%\n");
                    }
                }
                
                statsArea.setText(output.toString());
            }
        } catch (NumberFormatException ex) {
            StyleManager.showError(this, "Invalid threshold value!");
        }
    }
    
    private void updateStats(String studentId, String course, double percentage) {
        String status = percentage >= 75 ? "✅ Good" : 
                       percentage >= 60 ? "⚠️ Warning" : "❌ Critical";
        
        statsArea.setText(String.format(
            "Student: %s\n" +
            "Course: %s\n" +
            "Attendance: %.1f%%\n" +
            "Status: %s\n\n" +
            "Recommendation: %s",
            studentId, course, percentage, status,
            percentage < 75 ? "Attendance is below minimum requirement" : "Maintain current attendance"
        ));
    }
}