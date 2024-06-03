package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import database.DatabaseConnection;

public class RegisterForm extends JFrame {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField studentIdField;
    private JTextField yearLevelField;
    private JTextField sectionField;
    private JComboBox<String> departmentComboBox;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public RegisterForm() {
        setTitle("Register Form");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(10, 2));

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField();

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();

        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdField = new JTextField();

        JLabel yearLevelLabel = new JLabel("Year Level:");
        yearLevelField = new JTextField();

        JLabel sectionLabel = new JLabel("Section:");
        sectionField = new JTextField();

        JLabel departmentLabel = new JLabel("Department:");
        String[] departments = {"HSU", "SOL", "CTM", "CTHM", "COS", "CITE", "IIHS", "IOP", "ION", "CGPP", "CHK", "CCSE", "CCIS", "IOA", "CBFS", "COL"};
        departmentComboBox = new JComboBox<>(departments);

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

        add(firstNameLabel);
        add(firstNameField);
        add(lastNameLabel);
        add(lastNameField);
        add(emailLabel);
        add(emailField);
        add(studentIdLabel);
        add(studentIdField);
        add(yearLevelLabel);
        add(yearLevelField);
        add(sectionLabel);
        add(sectionField);
        add(departmentLabel);
        add(departmentComboBox);
        add(passwordLabel);
        add(passwordField);
        add(registerButton);
        add(messageLabel);
    }

    private void registerUser() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String studentId = studentIdField.getText();
        int yearLevel = Integer.parseInt(yearLevelField.getText());
        String section = sectionField.getText();
        String department = (String) departmentComboBox.getSelectedItem();
        char[] password = passwordField.getPassword();
        String hashedPassword = hashPassword(new String(password));

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO borrowers (borrower_type, student_id, year_level, section, department, first_name, last_name, email, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "Student");
            statement.setString(2, studentId);
            statement.setInt(3, yearLevel);
            statement.setString(4, section);
            statement.setString(5, department);
            statement.setString(6, firstName);
            statement.setString(7, lastName);
            statement.setString(8, email);
            statement.setString(9, hashedPassword);
    
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

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        RegisterForm registerForm = new RegisterForm();
        registerForm.setVisible(true);
    }
}
