package org.example.ui;

import org.example.core.helpers.LoginHelper;
import org.example.core.interfaces.Auth;

import javax.swing.*;
import java.awt.*;

import static utils.MyUtils.isValidEmail;
import static utils.MyUtils.isValidUsername;

public class LoginPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Input Panel for username and password
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 1, 0));

        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Add Panels to Main Panel
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add Main Panel to Frame
        add(mainPanel);

        // Action Listeners
        loginButton.addActionListener(e -> authenticateUser());
        registerButton.addActionListener(e -> openRegisterPage());
    }

    private void openRegisterPage() {
        JOptionPane.showMessageDialog(this, "Register Page Opening...");
        RegisterPage registerPage = new RegisterPage();
        registerPage.setVisible(true);
        this.dispose();
    }

    private void authenticateUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and Password cannot be empty!");
            return;
        }

        if (!isValidUsername(username)) {
            JOptionPane.showMessageDialog(this, "Invalid Username! Must be alphanumeric, 3-15 characters.");
            return;
        }


        try {
            LoginHelper
                    .getInstance()
                    .attachLoginListener(new Auth() {
                @Override
                public void onLoginSuccess(String data) {
                    HomePage homePage = new HomePage();
                    homePage.setVisible(true);
                    LoginPage.this.dispose();
                }

                @Override
                public void onLoginFailed(String error) {
                    JOptionPane.showMessageDialog(LoginPage.this, error);
                }
            }).performLogin(username, password);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }



}