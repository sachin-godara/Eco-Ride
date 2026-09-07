package com.bicyclerental.ui;

import com.bicyclerental.model.Bicycle;
import com.bicyclerental.model.Rental;
import com.bicyclerental.model.Station;
import com.bicyclerental.service.DataRepository;
import com.bicyclerental.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ActiveRideFrame: Real-time Heads-Up Display (HUD) for ongoing bicycle rental.
 * Runs on javax.swing.Timer to update duration, live fare, and green metrics without blocking the EDT.
 */
public class ActiveRideFrame extends JFrame {

    private final CustomerDashboard parentDashboard;
    private final Rental rental;
    private final Bicycle bicycle;
    private final Station startStation;

    private Timer timer;
    private int secondsElapsed = 0;

    private JLabel lblTimer;
    private JLabel lblFare;
    private JLabel lblDistance;
    private JLabel lblCo2;
    private JLabel lblCalories;

    public ActiveRideFrame(CustomerDashboard parent, Rental rental) {
        this.parentDashboard = parent;
        this.rental = rental;
        this.bicycle = DataRepository.getInstance().getBicycleById(rental.getBikeId());
        this.startStation = DataRepository.getInstance().getStationById(rental.getStartStationId());

        setTitle("EcoRide - Active Ride Monitor | " + bicycle.getBikeCode());
        setSize(560, 520);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Must end ride cleanly
        setLocationRelativeTo(parent);
        getContentPane().setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout());

        initUI();
        startRideTimer();
    }

    private void initUI() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.BG_CARD);
        header.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel lblTitle = new JLabel("🚲 Active Trip in Progress");
        lblTitle.setFont(UITheme.TITLE_FONT);
        lblTitle.setForeground(UITheme.PRIMARY);

        JLabel lblBikeInfo = new JLabel(bicycle.getBikeCode() + " • " + bicycle.getModelName());
        lblBikeInfo.setFont(UITheme.BODY_BOLD);
        lblBikeInfo.setForeground(UITheme.TEXT_MUTED);

        header.add(lblTitle, BorderLayout.WEST);
        header.add(lblBikeInfo, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Center HUD
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(UITheme.BG_DARK);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EmptyBorder(20, 24, 20, 24));

        // Timer Panel (Center highlight)
        JPanel timerCard = UITheme.createCardPanel();
        timerCard.setLayout(new BoxLayout(timerCard, BoxLayout.Y_AXIS));
        timerCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTimeDesc = new JLabel("ELAPSED TIME (LIVE)");
        lblTimeDesc.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTimeDesc.setForeground(UITheme.TEXT_MUTED);
        lblTimeDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTimer = new JLabel("00:00:00");
        lblTimer.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblTimer.setForeground(UITheme.TEXT_LIGHT);
        lblTimer.setAlignmentX(Component.CENTER_ALIGNMENT);

        timerCard.add(lblTimeDesc);
        timerCard.add(Box.createVerticalStrut(4));
        timerCard.add(lblTimer);
        centerPanel.add(timerCard);

        centerPanel.add(Box.createVerticalStrut(20));

        // 2x2 Telemetry Grid (Fare, Distance, CO2, Calories)
        JPanel telemetryGrid = new JPanel(new GridLayout(2, 2, 14, 14));
        telemetryGrid.setOpaque(false);

        lblFare = new JLabel("₹ 10.00");
        telemetryGrid.add(createMiniMetric("Estimated Fare", lblFare, UITheme.ACCENT_AMBER));

        lblDistance = new JLabel("0.00 km");
        telemetryGrid.add(createMiniMetric("Simulated Distance", lblDistance, UITheme.ACCENT_BLUE));

        lblCo2 = new JLabel("0.00 kg");
        telemetryGrid.add(createMiniMetric("CO₂ Prevented (USP)", lblCo2, UITheme.PRIMARY));

        lblCalories = new JLabel("0 kcal");
        telemetryGrid.add(createMiniMetric("Calories Burned", lblCalories, UITheme.TEXT_ACCENT));

        centerPanel.add(telemetryGrid);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom Actions
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 16));
        footer.setBackground(UITheme.BG_DARK);

        JButton btnEndRide = UITheme.createPrimaryButton(">> Select Dock & Complete Ride <<");
        btnEndRide.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnEndRide.setBackground(UITheme.ACCENT_ROSE);
        btnEndRide.addActionListener(e -> openReturnDialog());

        footer.add(btnEndRide);
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel createMiniMetric(String title, JLabel val, Color col) {
        JPanel p = UITheme.createCardPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 11));
        t.setForeground(UITheme.TEXT_MUTED);

        val.setFont(new Font("Segoe UI", Font.BOLD, 20));
        val.setForeground(col);

        p.add(t);
        p.add(Box.createVerticalStrut(4));
        p.add(val);
        return p;
    }

    private void startRideTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsElapsed += 5; // Fast-forward simulation: 5 seconds per tick for realistic demo!
                int hrs = secondsElapsed / 3600;
                int mins = (secondsElapsed % 3600) / 60;
                int secs = secondsElapsed % 60;
                lblTimer.setText(String.format("%02d:%02d:%02d", hrs, mins, secs));

                // Telemetry calculations
                double distance = (secondsElapsed / 3600.0) * 14.0; // 14 km/h
                lblDistance.setText(String.format("%.2f km", distance));

                double co2 = distance * 0.21; // 0.21 kg per km
                lblCo2.setText(String.format("%.2f kg", co2));

                int calories = (int) ((secondsElapsed / 60.0) * 7.5);
                lblCalories.setText(calories + " kcal");

                // Fare calculation: Base fare ₹10 covers first 15 mins. Beyond that, hourly rate / 60 per minute
                double currentFare = 10.00;
                if (mins > 15 || hrs > 0) {
                    int billableMinutes = (hrs * 60 + mins) - 15;
                    currentFare += billableMinutes * (bicycle.getHourlyRate() / 60.0);
                }
                lblFare.setText(String.format("₹ %.2f", currentFare));
            }
        });
        timer.start();
    }

    private void openReturnDialog() {
        int durationMinutes = Math.max(1, secondsElapsed / 60);
        double distance = (secondsElapsed / 3600.0) * 14.0;
        double co2 = distance * 0.21;
        int ecoCredits = (int) (co2 / 0.5);

        double totalFare = 10.00;
        if (durationMinutes > 15) {
            totalFare += (durationMinutes - 15) * (bicycle.getHourlyRate() / 60.0);
        }

        new ReturnBikeDialog(this, parentDashboard, rental, durationMinutes, totalFare, ecoCredits, co2).setVisible(true);
    }

    public void finishRide() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        dispose();
    }
}
