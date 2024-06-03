package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import database.DatabaseConnection;

public class RegisterForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public RegisterForm() {
        setTitle("Register Form");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        messageLabel = new JLabel("");
        JButton registerButton = new JButton("Register");

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(registerButton);
        add(messageLabel);
    }

    private void registerUser() {
        String username = usernameField.getText();
        char[] password = passwordField.getPassword();
    
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Users (username, password, user_type) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, new String(password));
            statement.setString(3, "User"); // or "Admin" for administrators
    
            int rowsAffected = statement.executeUpdate();
    
            if (rowsAffected > 0) {
                messageLabel.setText("Registration successful");
            } else {
                messageLabel.setText("Registration failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RegisterForm RegisterForm = new RegisterForm();
        RegisterForm.setVisible(true);
    }
}