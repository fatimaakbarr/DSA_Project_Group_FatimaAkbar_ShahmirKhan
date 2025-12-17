// src/java/com/campusconnect/Main.java
package com.campusconnect;

import com.campusconnect.gui.MainFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and show GUI
        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            } catch (UnsatisfiedLinkError e) {
                JOptionPane.showMessageDialog(null,
                    "Failed to load native library!\n\n" +
                    "Please ensure:\n" +
                    "1. C++ code is compiled\n" +
                    "2. JNI library is in the correct location\n" +
                    "3. java.library.path is set correctly\n\n" +
                    "Error: " + e.getMessage(),
                    "Library Load Error",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                System.exit(1);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                    "Application Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}
