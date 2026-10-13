package com.bicyclerental.ui;

import com.bicyclerental.model.Bicycle;
import com.bicyclerental.model.Rental;
import com.bicyclerental.model.Station;
import com.bicyclerental.service.DataRepository;
import com.bicyclerental.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * ActiveRideFrame: Real-time telemetry dashboard displayed during an active ride.
 * Features live trip duration, estimated distance, real-time fare calculation, and CO2 offset metrics.
 */
public class ActiveRideFrame extends JFrame {

    private static ActiveRideFrame activeInstance = null;

    private final CustomerDashboard parentDashboard;
    private final Rental rental;
    private final Bicycle bicycle;
    private final Station startStation;

    private Timer timer;

    private JLabel lblTimer;
    private JLabel lblFare;
    private JLabel lblDistance;
    private JLabel lblCo2;
    private JLabel lblCalories;

    public static synchronized ActiveRideFrame openHUD(CustomerDashboard parent, Rental rental) {
        if (activeInstance != null && activeInstance.isDisplayable()) {
            activeInstance.toFront();
            activeInstance.requestFocus();
            activeInstance.updateTelemetry();
            return activeInstance;
        }
        activeInstance = new ActiveRideFrame(parent, rental);
        activeInstance.setVisible(true);
        return activeInstance;
    }

    public static synchronized ActiveRideFrame getActiveInstance() {
        return (activeInstance != null && activeInstance.isDisplayable()) ? activeInstance : null;
    }

    public ActiveRideFrame(CustomerDashboard parent, Rental rental) {
        this.parentDashboard = parent;
        this.rental = rental;
        this.bicycle = DataRepository.getInstance().getBicycleById(rental.getBikeId());
        this.startStation = DataRepository.getInstance().getStationById(rental.getStartStationId());

        setTitle("EcoRide - Active Ride HUD | " + (bicycle != null ? bicycle.getBikeCode() : "Ongoing Trip"));
        setSize(600, 570);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setAlwaysOnTop(true);
        getContentPane().setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout());

        activeInstance = this;

        initUI();
        updateTelemetry();
        startRideTimer();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                }
                activeInstance = null;
            }
        });
    }

    private void initUI() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.BG_CARD);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER_COLOR),
                new EmptyBorder(16, 20, 16, 20)
        ));

        JLabel lblTitle = new JLabel("Trip in Progress (HUD)");
        lblTitle.setFont(UITheme.TITLE_FONT);
        lblTitle.setForeground(UITheme.TEXT_LIGHT);

        String bikeDesc = bicycle != null ? bicycle.getBikeCode() + " | " + bicycle.getModelName() + " (" + bicycle.getBikeType() + ")" : "Active Bicycle";
        JLabel lblBikeInfo = new JLabel(bikeDesc);
        lblBikeInfo.setFont(UITheme.BODY_BOLD);
        lblBikeInfo.setForeground(Color.BLACK);

        header.add(lblTitle, BorderLayout.WEST);
        header.add(lblBikeInfo, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Center HUD
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(UITheme.BG_DARK);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EmptyBorder(18, 24, 18, 24));

        // Live Status Tag
        JPanel statusTagPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        statusTagPanel.setOpaque(false);
        JLabel lblLiveBadge = new JLabel("[ LIVE GPS METERING ACTIVE ]");
        lblLiveBadge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblLiveBadge.setForeground(Color.BLACK);
        statusTagPanel.add(lblLiveBadge);
        centerPanel.add(statusTagPanel);
        centerPanel.add(Box.createVerticalStrut(10));

        // Timer Card (Center highlight)
        JPanel timerCard = UITheme.createCardPanel();
        timerCard.setLayout(new BoxLayout(timerCard, BoxLayout.Y_AXIS));
        timerCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTimeDesc = new JLabel("ELAPSED TIME (LIVE METERED CLOCK)");
        lblTimeDesc.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTimeDesc.setForeground(UITheme.TEXT_MUTED);
        lblTimeDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTimer = new JLabel("00:00:00");
        lblTimer.setFont(new Font("Segoe UI", Font.BOLD, 46));
        lblTimer.setForeground(UITheme.TEXT_LIGHT);
        lblTimer.setAlignmentX(Component.CENTER_ALIGNMENT);

        timerCard.add(lblTimeDesc);
        timerCard.add(Box.createVerticalStrut(4));
        timerCard.add(lblTimer);
        centerPanel.add(timerCard);

        centerPanel.add(Box.createVerticalStrut(18));

        // 2x2 Telemetry Grid (Fare, Distance, CO2, Calories)
        JPanel telemetryGrid = new JPanel(new GridLayout(2, 2, 14, 14));
        telemetryGrid.setOpaque(false);

        lblFare = new JLabel("INR 10.00");
        telemetryGrid.add(createMiniMetric("Estimated Fare", lblFare, UITheme.ACCENT_AMBER));

        lblDistance = new JLabel("0.00 km");
        telemetryGrid.add(createMiniMetric("Estimated Distance", lblDistance, UITheme.ACCENT_BLUE));

        lblCo2 = new JLabel("0.00 kg");
        telemetryGrid.add(createMiniMetric("CO2 Avoided", lblCo2, Color.BLACK));

        lblCalories = new JLabel("0 kcal");
        telemetryGrid.add(createMiniMetric("Calories Burned", lblCalories, Color.BLACK));

        centerPanel.add(telemetryGrid);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom Actions
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 14));
        footer.setBackground(UITheme.BG_DARK);

        JButton btnEndRide = UITheme.createPrimaryButton("End Ride & Settle Fare");
        btnEndRide.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEndRide.setBackground(UITheme.PRIMARY);
        btnEndRide.addActionListener(e -> openReturnDialog());

        JButton btnEmergency = UITheme.createSecondaryButton("Emergency Assistance");
        btnEmergency.addActionListener(e -> {
            UITheme.showStyledMessage(this, "Emergency team alerted with your current GPS coordinates. Call campus security: +91 98765-00000.", "Campus Security Notified", JOptionPane.WARNING_MESSAGE);
        });

        footer.add(btnEmergency);
        footer.add(btnEndRide);
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel createMiniMetric(String title, JLabel val, Color col) {
        JPanel p = UITheme.createCardPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.setForeground(Color.BLACK);

        val.setFont(new Font("Segoe UI", Font.BOLD, 22));
        val.setForeground(col);

        p.add(t);
        p.add(Box.createVerticalStrut(4));
        p.add(val);
        return p;
    }

    public void updateTelemetry() {
        int secondsElapsed = rental.getElapsedSeconds();
        int hrs = secondsElapsed / 3600;
        int mins = (secondsElapsed % 3600) / 60;
        int secs = secondsElapsed % 60;
        if (lblTimer != null) {
            lblTimer.setText(String.format("%02d:%02d:%02d", hrs, mins, secs));
        }

        double distance = rental.calculateDistanceKm();
        if (lblDistance != null) {
            lblDistance.setText(String.format("%.2f km", distance));
        }

        double co2 = rental.calculateCo2SavedKg();
        if (lblCo2 != null) {
            lblCo2.setText(String.format("%.2f kg", co2));
        }

        int calories = rental.calculateCalories();
        if (lblCalories != null) {
            lblCalories.setText(calories + " kcal");
        }

        double currentFare = rental.calculateGrossFare(bicycle != null ? bicycle.getHourlyRate() : 20.0);
        if (lblFare != null) {
            lblFare.setText(String.format("INR %.2f", currentFare));
        }
    }

    private void startRideTimer() {
        // Fallback local timer in case dashboard master timer isn't ticking
        timer = new Timer(1000, e -> {
            updateTelemetry();
        });
        timer.start();
    }

    private void openReturnDialog() {
        int durationMinutes = rental.calculateDurationMinutes();
        double distance = rental.calculateDistanceKm();
        double co2 = rental.calculateCo2SavedKg();
        int ecoCredits = rental.calculateEcoCredits();
        double totalFare = rental.calculateGrossFare(bicycle != null ? bicycle.getHourlyRate() : 20.0);

        new ReturnBikeDialog(this, this, parentDashboard, rental, durationMinutes, distance, totalFare, ecoCredits, co2).setVisible(true);
    }

    public void finishRide() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        activeInstance = null;
        dispose();
    }
}
