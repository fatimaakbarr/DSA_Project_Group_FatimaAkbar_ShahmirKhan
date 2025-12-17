import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NavigatorUI extends JFrame {
    private JTextField startField, endField;
    private JButton findButton;
    private JTextArea resultArea;

    static {
        System.loadLibrary("native_impl"); // compiled C++ shared library
    }

    private native String getShortestPath(int start, int end);

    public NavigatorUI() {
        setTitle("Campus Navigator");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Start Node:"));
        startField = new JTextField(5);
        inputPanel.add(startField);

        inputPanel.add(new JLabel("End Node:"));
        endField = new JTextField(5);
        inputPanel.add(endField);

        findButton = new JButton("Find Path");
        inputPanel.add(findButton);

        add(inputPanel, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        findButton.addActionListener(e -> {
            try {
                int start = Integer.parseInt(startField.getText());
                int end = Integer.parseInt(endField.getText());
                String path = getShortestPath(start, end);
                resultArea.setText("Shortest Path: " + path);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Enter valid integers for nodes");
            }
        });
    }

    public static void main(String[] args) {
    new NavigatorUI().setVisible(true);
}

}
