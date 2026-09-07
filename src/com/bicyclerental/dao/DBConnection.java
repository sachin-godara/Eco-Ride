package com.bicyclerental.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection: Thread-safe Singleton Database Connection Manager
 * Connects to local MySQL 8.x server for the NSP Minor Project.
 */
public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/bicycle_rental_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // Default password, configurable

    private static Connection connection = null;
    private static boolean driverLoaded = false;
    private static boolean connectionAvailable = false;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            driverLoaded = true;
        } catch (ClassNotFoundException e) {
            System.err.println("[DBConnection] MySQL JDBC Driver not found on classpath: " + e.getMessage());
        }
    }

    private DBConnection() {}

    public static synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                if (driverLoaded) {
                    connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    connectionAvailable = true;
                    System.out.println("[DBConnection] Successfully connected to MySQL database: bicycle_rental_db");
                }
            }
        } catch (SQLException e) {
            connectionAvailable = false;
            System.err.println("[DBConnection] Could not connect to MySQL: " + e.getMessage());
            System.err.println("[DBConnection] (Falling back to in-memory data store for GUI prototype demonstration)");
        }
        return connection;
    }

    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DBConnection] Connection closed cleanly.");
            }
        } catch (SQLException e) {
            System.err.println("[DBConnection] Error closing connection: " + e.getMessage());
        }
    }
}
