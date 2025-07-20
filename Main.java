// package com.Steganography;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Main extends JFrame {
    private File selectedImage;

    public Main() {
        setTitle("Steganography Encoder/Decoder");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Text-in-Image Steganography", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        JButton encodeButton = new JButton("Encode");
        JButton decodeButton = new JButton("Decode");

        centerPanel.add(encodeButton);
        centerPanel.add(decodeButton);
        add(centerPanel, BorderLayout.CENTER);

        encodeButton.addActionListener(e -> showEncodePanel());
        decodeButton.addActionListener(e -> showDecodePanel());
    }

    private void showEncodePanel() {
        JDialog dialog = new JDialog(this, "Encode Message", true);
        dialog.setLayout(new GridLayout(7, 1, 10, 10));
        dialog.setSize(400, 300);

        JButton selectImageButton = new JButton("Select Image (.png/.jpg)");
        JTextField messageField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton encodeBtn = new JButton("Encode and Save");

        JLabel statusLabel = new JLabel("", JLabel.CENTER);

        selectImageButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int option = chooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                selectedImage = chooser.getSelectedFile();
                selectImageButton.setText("Selected: " + selectedImage.getName());
            }
        });

        encodeBtn.addActionListener(e -> {
            if (selectedImage == null || messageField.getText().isEmpty() || passwordField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(dialog, "Please select image, enter message and password.");
                return;
            }

            try {
                SteganographyEncoder.encode(selectedImage, selectedImage, messageField.getText(), new String(passwordField.getPassword()));
                JOptionPane.showMessageDialog(dialog, "Message successfully encoded and saved to:\nD:\\java project\\encoded");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Failed to encode image. Error: " + ex.getMessage());
            }
        });

        dialog.add(selectImageButton);
        dialog.add(new JLabel("Enter secret message:", JLabel.CENTER));
        dialog.add(messageField);
        dialog.add(new JLabel("Enter password:", JLabel.CENTER));
        dialog.add(passwordField);
        dialog.add(encodeBtn);
        dialog.add(statusLabel);

        dialog.setVisible(true);
    }

    private void showDecodePanel() {
        JDialog dialog = new JDialog(this, "Decode Message", true);
        dialog.setLayout(new GridLayout(5, 1, 10, 10));
        dialog.setSize(400, 250);

        JButton selectImageButton = new JButton("Select Image (.png/.jpg)");
        JPasswordField passwordField = new JPasswordField();
        JButton decodeBtn = new JButton("Decode");
        JLabel messageLabel = new JLabel("Decoded Message will appear here.", JLabel.CENTER);

        selectImageButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int option = chooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                selectedImage = chooser.getSelectedFile();
                selectImageButton.setText("Selected: " + selectedImage.getName());
            }
        });

        decodeBtn.addActionListener(e -> {
            if (selectedImage == null || passwordField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(dialog, "Select image and enter password.");
                return;
            }

            try {
                String message = SteganographyDecoder.decode(selectedImage, new String(passwordField.getPassword()));
                messageLabel.setText("Message: " + message);
            } catch (Exception ex) {
                messageLabel.setText("Error reading image.");
            }
        });

        dialog.add(selectImageButton);
        dialog.add(new JLabel("Enter password:", JLabel.CENTER));
        dialog.add(passwordField);
        dialog.add(decodeBtn);
        dialog.add(messageLabel);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}
