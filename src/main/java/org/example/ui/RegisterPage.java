package org.example.ui;

import utils.DatabaseHelper;
import utils.MyUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.regex.Pattern;

import static utils.MyUtils.isValidEmail;
import static utils.MyUtils.isValidUsername;

public class RegisterPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;

    public RegisterPage() {
        setTitle("Register");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username
        add(new JLabel("Username:"), configureGbc(gbc, 0, 0));
        usernameField = new JTextField(15);
        add(usernameField, configureGbc(gbc, 1, 0));

        // Email
        add(new JLabel("Email:"), configureGbc(gbc, 0, 1));
        emailField = new JTextField(15);
        add(emailField, configureGbc(gbc, 1, 1));

        // Password
        add(new JLabel("Password:"), configureGbc(gbc, 0, 2));
        passwordField = new JPasswordField(15);
        add(passwordField, configureGbc(gbc, 1, 2));

        // Register Button
        JButton registerButton = new JButton("Register");
        add(registerButton, configureGbc(gbc, 0, 3, 2, 1));
        registerButton.addActionListener(e -> registerUser());

        // Back to Login Button
        JButton backButton = new JButton("Back to Login");
        add(backButton, configureGbc(gbc, 0, 4, 2, 1));
        backButton.addActionListener(e -> goToLoginPage());
    }

    private GridBagConstraints configureGbc(GridBagConstraints gbc, int x, int y) {
        return configureGbc(gbc, x, y, 1, 1);
    }

    private GridBagConstraints configureGbc(GridBagConstraints gbc, int x, int y, int width, int height) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.anchor = GridBagConstraints.CENTER;
        return gbc;
    }

    private void goToLoginPage() {
        this.dispose();
        new LoginPage().setVisible(true);
    }

    private void registerUser() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        if (!isValidUsername(username) || !isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid input! Check username or email format.");
            return;
        }

        try (Connection conn = DatabaseHelper.getInstance().getDbConnection()) {
            String checkQuery = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, username);

            if (checkStmt.executeQuery().next()) {
                JOptionPane.showMessageDialog(this, "Username already exists!");
                return;
            }

            String insertQuery = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, username);
            stmt.setString(2, MyUtils.hashPassword(password));
            stmt.setString(3, email);

            if (stmt.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                goToLoginPage();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

}