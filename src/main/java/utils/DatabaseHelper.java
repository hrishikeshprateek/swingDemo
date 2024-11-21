package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {

    private static Connection connection = null;
    private static DatabaseHelper instance = null;

    public static DatabaseHelper getInstance() {
        if (instance == null) return instance = new DatabaseHelper();
        return instance;
    }

    public Connection getDbConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                return connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "root", "123456789");
            }
        } catch (SQLException e) {
            System.out.println("Database connection failed");
            e.printStackTrace();
        }
        return connection;
    }

    public void closeDbConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                getDbConnection().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
