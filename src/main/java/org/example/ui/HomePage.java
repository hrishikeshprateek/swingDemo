package org.example.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class HomePage extends JFrame {

    private JTable usersTable;
    private DefaultTableModel tableModel;

    public HomePage() {
        setTitle("User List");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] columnNames = {"ID", "Username", "Password", "Email", "Edit", "Delete"};
        tableModel = new DefaultTableModel(null, columnNames);
        usersTable = new JTable(tableModel);

        // Set Renderers
        usersTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        usersTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());

        // Set Editors
        usersTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));
        usersTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(usersTable);
        add(scrollPane);

        // Load data from DB
        loadDataFromDB();

        setVisible(true);
    }

    // Button Renderer to display buttons in table cells
    private class ButtonRenderer implements TableCellRenderer {
        private JButton button;

        public ButtonRenderer() {
            button = new JButton();
            button.setFocusable(false); // Prevent focus behavior
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            button.setText(value == null ? "" : value.toString());
            return button;
        }
    }

    // Button Editor for making buttons interactive
    private class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JButton button;
        private String label;
        private JTable table;

        public ButtonEditor(JCheckBox checkBox) {
            button = new JButton();
            button.setOpaque(true);  // Make the button opaque
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped(); // Stop editing on button press
                    if (label.equals("Edit")) {
                        editUser();
                    } else if (label.equals("Delete")) {
                        deleteUser();
                    }
                }
            });
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            this.table = table;
            return button;
        }

        private void editUser() {
            int row = usersTable.getSelectedRow();
            int userId = (int) usersTable.getValueAt(row, 0);
            String username = (String) usersTable.getValueAt(row, 1);
            String email = (String) usersTable.getValueAt(row, 3);

            // Open an Edit dialog or form
            String newUsername = JOptionPane.showInputDialog(table, "Edit Username", username);
            String newEmail = JOptionPane.showInputDialog(table, "Edit Email", email);

            if (newUsername != null && newEmail != null) {
                updateUserInDB(userId, newUsername, newEmail);
            }
        }

        private void deleteUser() {
            int row = usersTable.getSelectedRow();
            int userId = (int) usersTable.getValueAt(row, 0);
            String username = (String) usersTable.getValueAt(row, 1);

            int confirm = JOptionPane.showConfirmDialog(table, "Are you sure you want to delete " + username + "?", "Delete User", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteUserFromDB(userId);
            }
        }
    }

    private void loadDataFromDB() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "root", "123456789");

            String query = "SELECT * FROM users";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Clear existing rows in the table
            tableModel.setRowCount(0);

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password"); // Avoid displaying real password
                String email = rs.getString("email");

                // Add data to table model
                tableModel.addRow(new Object[]{id, username, password, email, "Edit", "Delete"});
            }

            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data from database: " + e.getMessage());
        }
    }

    private void updateUserInDB(int userId, String newUsername, String newEmail) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "root", "123456789");

            String query = "UPDATE users SET username = ?, email = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, newUsername);
            stmt.setString(2, newEmail);
            stmt.setInt(3, userId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "User updated successfully!");
                loadDataFromDB(); // Reload data
            } else {
                JOptionPane.showMessageDialog(this, "Error updating user.");
            }

            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating user: " + e.getMessage());
        }
    }

    private void deleteUserFromDB(int userId) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "root", "123456789");

            String query = "DELETE FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "User deleted successfully!");
                loadDataFromDB(); // Reload data
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting user.");
            }

            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new HomePage();
    }
}