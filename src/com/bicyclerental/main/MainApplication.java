package com.bicyclerental.main;

import com.bicyclerental.dao.DBConnection;
import com.bicyclerental.ui.LoginFrame;

import javax.swing.*;

/**
 * MainApplication: Entry point for the Smart Bicycle Rental System (EcoRide).
 * Launches the Swing GUI safely on the Event Dispatch Thread (EDT).
 */
public class MainApplication {

    public static void main(String[] args) {
        System.out.println("==================================================================");
        System.out.println("  🚲 SMART BICYCLE RENTAL SYSTEM (EcoRide) - NSP MINOR PROJECT");
        System.out.println("  Faculty Evaluator: Mr. Rohit Kumar (Dept. of AI & DS)");
        System.out.println("  Running on Java " + System.getProperty("java.version") + " (" + System.getProperty("os.name") + ")");
        System.out.println("==================================================================");

        // Attempt initial database connectivity check in background
        new Thread(() -> {
            DBConnection.getConnection();
        }).start();

        // Launch UI on Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
