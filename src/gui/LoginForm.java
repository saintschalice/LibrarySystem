package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.Login;

public class LoginForm extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    public static boolean isLoggedIn = false; 
    private static Runnable loginListener;

    public LoginForm() {
        setTitle("Login Form");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(3, 2));

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        messageLabel = new JLabel("");
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });

        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(messageLabel);
    }

    private void loginUser() {
        String email = emailField.getText();
        char[] password = passwordField.getPassword();
        Login login = new Login(email, new String(password));

        if (login.authenticate()) {
            isLoggedIn = true;
            messageLabel.setText("Login successful");
            if (loginListener != null) {
                loginListener.run();
            }
            dispose();
        } else {
            messageLabel.setText("Login failed");
        }
    }

    public static void addLoginListener(Runnable listener) {
        loginListener = listener;
    }

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm();
        loginForm.setVisible(true);
    }
}