// src/java/com/campusconnect/gui/MainFrame.java
package com.campusconnect.gui;

import com.campusconnect.CampusConnectJNI;
import com.campusconnect.utils.StyleManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CampusConnectJNI jni;
    private JTabbedPane tabbedPane;
    
    private NavigationPanel navigationPanel;
    private AttendancePanel attendancePanel;
    private StudentPanel studentPanel;
    private SchedulingPanel schedulingPanel;
    private ResourcePanel resourcePanel;
    
    public MainFrame() {
        jni = new CampusConnectJNI();
        initializeBackend();
        initializeUI();
    }
    
    private void initializeBackend() {
        jni.initNavigation();
        jni.initAttendance();
        jni.initStudentSystem();
        jni.initScheduling();
        jni.initResources();
        
        // Add sample data
        addSampleData();
    }
    
    private void addSampleData() {
        // Sample campus locations
        jni.addLocation("Main Gate", 0, 0);
        jni.addLocation("Library", 100, 50);
        jni.addLocation("Computer Lab", 150, 100);
        jni.addLocation("Cafeteria", 50, 150);
        jni.addLocation("Sports Complex", 200, 200);
        jni.addLocation("Admin Block", 100, 200);
        
        jni.addPath("Main Gate", "Library", 120);
        jni.addPath("Main Gate", "Cafeteria", 160);
        jni.addPath("Library", "Computer Lab", 80);
        jni.addPath("Library", "Admin Block", 110);
        jni.addPath("Computer Lab", "Sports Complex", 140);
        jni.addPath("Cafeteria", "Admin Block", 90);
        jni.addPath("Admin Block", "Sports Complex", 120);
        
        // Sample students
        jni.addStudent("S001", "Fatima Khan", "Computer Science", 3, 3.8, 
                      "fatima@campus.edu", "0300-1234567");
        jni.addStudent("S002", "Shahmir Ali", "Software Engineering", 3, 3.6,
                      "shahmir@campus.edu", "0301-7654321");
        jni.addStudent("S003", "Ayesha Ahmed", "Computer Science", 5, 3.9,
                      "ayesha@campus.edu", "0302-9876543");
        jni.addStudent("S004", "Hassan Raza", "Data Science", 1, 3.5,
                      "hassan@campus.edu", "0303-1122334");
        
        // Sample courses
        jni.addCourse("CS301", "Data Structures & Algorithms", "Dr. Ahmed", 
                     3, 45, 50, 10, "Mon-Wed 9:00-10:30", "Lab-1");
        jni.addCourse("CS302", "Database Systems", "Dr. Fatima",
                     3, 38, 45, 8, "Tue-Thu 11:00-12:30", "Lab-2");
        jni.addCourse("CS303", "Operating Systems", "Dr. Khan",
                     4, 42, 50, 9, "Mon-Wed 2:00-4:00", "Lab-3");
    }
    
    private void initializeUI() {
        setTitle("Campus Connect - Integrated Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(StyleManager.BACKGROUND);
        
        // Create header
        JPanel header = createHeader();
        mainPanel.add(header, BorderLayout.NORTH);
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(StyleManager.FONT_BODY);
        tabbedPane.setBackground(StyleManager.SURFACE);
        
        // Create and add panels
        navigationPanel = new NavigationPanel(jni);
        attendancePanel = new AttendancePanel(jni);
        studentPanel = new StudentPanel(jni);
        schedulingPanel = new SchedulingPanel(jni);
        resourcePanel = new ResourcePanel(jni);
        
        tabbedPane.addTab("🗺️ Navigation", navigationPanel);
        tabbedPane.addTab("📋 Attendance", attendancePanel);
        tabbedPane.addTab("👤 Students", studentPanel);
        tabbedPane.addTab("📅 Scheduling", schedulingPanel);
        tabbedPane.addTab("🔍 Lost & Found", resourcePanel);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(StyleManager.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Title
        JLabel titleLabel = new JLabel("Campus Connect");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Integrated Student Management & Navigation System");
        subtitleLabel.setFont(StyleManager.FONT_BODY);
        subtitleLabel.setForeground(StyleManager.PRIMARY_LIGHT);
        
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 5));
        titlePanel.setBackground(StyleManager.PRIMARY);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        header.add(titlePanel, BorderLayout.WEST);
        
        return header;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}