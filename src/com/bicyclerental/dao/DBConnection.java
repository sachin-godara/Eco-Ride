package com.bicyclerental.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection: Thread-safe Singleton Database Connection Manager
 * Connects to MySQL 8.x relational database server for persistent enterprise storage.
 * Includes short failover timeout to prevent UI lag if MySQL is offline.
 */
public class DBConnection {

    // 1.2s connectTimeout & socketTimeout to prevent GUI freeze if MySQL server is offline
    private static final String URL = "jdbc:mysql://localhost:3306/bicycle_rental_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&connectTimeout=1200&socketTimeout=1200";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // Default password, configurable

    private static Connection connection = null;
    private static boolean driverLoaded = false;
    private static boolean connectionAvailable = false;
    private static boolean loggedStatus = false;

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
                    if (!loggedStatus) {
                        System.out.println("[DBConnection] Connected to MySQL database: bicycle_rental_db");
                        loggedStatus = true;
                    }
                }
            }
        } catch (SQLException e) {
            connectionAvailable = false;
            if (!loggedStatus) {
                System.out.println("[DBConnection] MySQL offline (using high-performance in-memory repository for prototype demo).");
                loggedStatus = true;
            }
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
