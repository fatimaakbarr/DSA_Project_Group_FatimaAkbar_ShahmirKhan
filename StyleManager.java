// src/java/com/campusconnect/utils/StyleManager.java
package com.campusconnect.utils;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class StyleManager {
    
    // Color Palette - Modern Campus Theme
    public static final Color PRIMARY = new Color(79, 70, 229);        // Indigo
    public static final Color PRIMARY_DARK = new Color(67, 56, 202);
    public static final Color PRIMARY_LIGHT = new Color(129, 140, 248);
    
    public static final Color SECONDARY = new Color(16, 185, 129);     // Emerald
    public static final Color SECONDARY_DARK = new Color(5, 150, 105);
    
    public static final Color BACKGROUND = new Color(249, 250, 251);   // Light Gray
    public static final Color SURFACE = Color.WHITE;
    public static final Color SURFACE_VARIANT = new Color(243, 244, 246);
    
    public static final Color TEXT_PRIMARY = new Color(17, 24, 39);
    public static final Color TEXT_SECONDARY = new Color(107, 114, 128);
    public static final Color TEXT_DISABLED = new Color(209, 213, 219);
    
    public static final Color SUCCESS = new Color(34, 197, 94);
    public static final Color WARNING = new Color(251, 146, 60);
    public static final Color ERROR = new Color(239, 68, 68);
    public static final Color INFO = new Color(59, 130, 246);
    
    public static final Color BORDER = new Color(229, 231, 235);
    public static final Color DIVIDER = new Color(243, 244, 246);
    
    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUBHEADING = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);
    
    // Styling Methods
    
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 40));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_DARK);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY);
            }
        });
        
        return button;
    }
    
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setForeground(PRIMARY);
        button.setBackground(SURFACE);
        button.setBorder(new LineBorder(PRIMARY, 2));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 40));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(SURFACE_VARIANT);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SURFACE);
            }
        });
        
        return button;
    }
    
    public static JButton createIconButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 36));
        
        Color hoverColor = color.darker();
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    public static JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(FONT_BODY);
        field.setForeground(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setPreferredSize(new Dimension(250, 40));
        return field;
    }
    
    public static JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(FONT_BODY);
        combo.setForeground(TEXT_PRIMARY);
        combo.setBackground(SURFACE);
        combo.setBorder(new LineBorder(BORDER, 1));
        combo.setPreferredSize(new Dimension(250, 40));
        return combo;
    }
    
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_BODY);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    public static JLabel createHeadingLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_HEADING);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    public static JPanel createCard() {
        JPanel panel = new JPanel();
        panel.setBackground(SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        return panel;
    }
    
    public static JScrollPane createStyledScrollPane(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(new LineBorder(BORDER, 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }
    
    public static JTextArea createStyledTextArea(int rows, int cols) {
        JTextArea textArea = new JTextArea(rows, cols);
        textArea.setFont(FONT_BODY);
        textArea.setForeground(TEXT_PRIMARY);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return textArea;
    }
    
    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showWarning(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Warning", 
            JOptionPane.WARNING_MESSAGE);
    }
    
    public static Border createTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
            new LineBorder(BORDER, 1), title
        );
        border.setTitleFont(FONT_SUBHEADING);
        border.setTitleColor(TEXT_PRIMARY);
        return border;
    }
}