package org.example;

import utils.DatabaseHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class UserTableManager {


    public static void main(String[] args) {
        UserTableManager manager = new UserTableManager();
        manager.resetUsersTable();
    }

    public void resetUsersTable() {
        try (Connection conn = DatabaseHelper.getInstance().getDbConnection()) {
            Statement stmt = conn.createStatement();

            // Drop the users table if it exists
            String dropTableSQL = "DROP TABLE IF EXISTS users";
            stmt.executeUpdate(dropTableSQL);
            System.out.println("Dropped table `users` if it existed.");

            // Create a new users table
            String createTableSQL = """
                    CREATE TABLE users (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(15) NOT NULL UNIQUE,
                        password VARCHAR(64) NOT NULL
                    )
                    """;
            stmt.executeUpdate(createTableSQL);
            System.out.println("Created table `users`.");

            // Insert sample users with hashed passwords
            String adminPasswordHash = hashPassword("admin123");
            String user1PasswordHash = hashPassword("password1");
            String user2PasswordHash = hashPassword("password2");
            String user3PasswordHash = hashPassword("password3");

            String insertUsersSQL =
                    "INSERT INTO users (username, password) VALUES " +
                            "('admin', '" + adminPasswordHash + "')," +
                            "('user1', '" + user1PasswordHash + "')," +
                            "('user2', '" + user2PasswordHash + "')," +
                            "('user3', '" + user3PasswordHash + "');";
            stmt.executeUpdate(insertUsersSQL);
            System.out.println("Inserted sample users into `users` table.");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private String hashPassword(String password) throws Exception {
        // Encrypt the password using SHA-256
        java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes("UTF-8"));

        // Convert byte array to hex string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}