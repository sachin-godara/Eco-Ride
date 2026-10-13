package com.bicyclerental.main;

import com.bicyclerental.dao.DBConnection;
import com.bicyclerental.ui.LoginFrame;
import com.bicyclerental.util.UITheme;

import javax.swing.*;

/**
 * MainApplication: Entry point for the Smart Bicycle Rental System (EcoRide).
 * Initializes FlatLaf modern aesthetic light UI and launches the Login interface safely on the EDT.
 */
public class MainApplication {

    public static void main(String[] args) {
        System.out.println("==================================================================");
        System.out.println("  ECORIDE SMART BICYCLE MOBILITY SYSTEM - ENTERPRISE CORE");
        System.out.println("  Smart Dock Telemetry & Green Urban Fleet Platform");
        System.out.println("  Runtime: OpenJDK " + System.getProperty("java.version") + " (" + System.getProperty("os.name") + ")");
        System.out.println("==================================================================");

        // Configure FlatLaf modern design before instantiating any UI components
        UITheme.setupFlatLaf();

        // Check MySQL connection in background without blocking UI
        new Thread(() -> {
            DBConnection.getConnection();
        }).start();

        // Launch UI on Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
