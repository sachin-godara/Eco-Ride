package com.bicyclerental.ui;

import com.bicyclerental.model.Rental;
import com.bicyclerental.model.Station;
import com.bicyclerental.model.User;
import com.bicyclerental.service.DataRepository;
import com.bicyclerental.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * ReturnBikeDialog: Dock selection and return processor.
 * Demonstrates USP: Dynamic Dock Rebalancing & Station Capacity Intelligence.
 */
public class ReturnBikeDialog extends JDialog {

    private final ActiveRideFrame rideFrame;
    private final CustomerDashboard dashboard;
    private final Rental rental;
    private final int durationMinutes;
    private final double grossFare;
    private final int ecoCreditsEarned;
    private final double co2Saved;

    private JComboBox<Station> comboStations;
    private JLabel lblCapacityStatus;
    private JLabel lblRebalanceAlert;
    private JCheckBox chkRedeemCredits;
    private JLabel lblNetFare;

    public ReturnBikeDialog(ActiveRideFrame rideFrame, CustomerDashboard dashboard, Rental rental,
                            int durationMinutes, double grossFare, int ecoCreditsEarned, double co2Saved) {
        super(rideFrame, "Dock Bicycle & Settle Payment - EcoRide", true);
        this.rideFrame = rideFrame;
        this.dashboard = dashboard;
        this.rental = rental;
        this.durationMinutes = durationMinutes;
        this.grossFare = grossFare;
        this.ecoCreditsEarned = ecoCreditsEarned;
        this.co2Saved = co2Saved;

        setSize(520, 560);
        setLocationRelativeTo(rideFrame);
        getContentPane().setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout());

        initUI();
    }

    private void initUI() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        header.setBackground(UITheme.BG_DARK);
        header.setBorder(new EmptyBorder(16, 16, 5, 16));

        JLabel lblTitle = new JLabel("Return Bicycle & Settle Invoice");
        lblTitle.setFont(UITheme.SUBTITLE_FONT);
        lblTitle.setForeground(UITheme.PRIMARY);
        header.add(lblTitle);
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setBackground(UITheme.BG_DARK);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Trip Summary Card
        JPanel tripCard = UITheme.createCardPanel();
        tripCard.setLayout(new GridLayout(4, 2, 8, 8));

        tripCard.add(createLabel("Total Duration:"));
        tripCard.add(createValue(durationMinutes + " minutes"));

        tripCard.add(createLabel("CO₂ Emissions Prevented:"));
        tripCard.add(createValue(String.format("%.2f kg CO₂", co2Saved)));

        tripCard.add(createLabel("Gross Fare:"));
        tripCard.add(createValue(String.format("₹ %.2f", grossFare)));

        tripCard.add(createLabel("Eco-Credits Earned:"));
        tripCard.add(createValue("+" + ecoCreditsEarned + " credits"));

        content.add(tripCard);
        content.add(Box.createVerticalStrut(16));

        // Dock Rebalancing Station Selector (USP Highlight)
        JPanel dockCard = UITheme.createCardPanel();
        dockCard.setLayout(new BoxLayout(dockCard, BoxLayout.Y_AXIS));

        JLabel lblDockTitle = new JLabel("[USP] Destination Dock Selection & Rebalancing");
        lblDockTitle.setFont(UITheme.BODY_BOLD);
        lblDockTitle.setForeground(UITheme.TEXT_LIGHT);
        dockCard.add(lblDockTitle);
        dockCard.add(Box.createVerticalStrut(10));

        comboStations = new JComboBox<>();
        List<Station> stations = DataRepository.getInstance().getAllStations();
        for (Station s : stations) {
            comboStations.addItem(s);
        }
        comboStations.setFont(UITheme.BODY_FONT);
        comboStations.setBackground(UITheme.BG_INPUT);
        comboStations.setForeground(UITheme.TEXT_LIGHT);
        comboStations.addActionListener(e -> updateDockCapacityTelemetry());
        dockCard.add(comboStations);
        dockCard.add(Box.createVerticalStrut(8));

        lblCapacityStatus = new JLabel("Dock Status: ");
        lblCapacityStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dockCard.add(lblCapacityStatus);
        dockCard.add(Box.createVerticalStrut(6));

        lblRebalanceAlert = new JLabel(" ");
        lblRebalanceAlert.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        dockCard.add(lblRebalanceAlert);

        content.add(dockCard);
        content.add(Box.createVerticalStrut(16));

        // Payment & Eco-Credit Redemption
        User currentUser = DataRepository.getInstance().getCurrentUser();
        JPanel payCard = UITheme.createCardPanel();
        payCard.setLayout(new BoxLayout(payCard, BoxLayout.Y_AXIS));

        chkRedeemCredits = new JCheckBox("Redeem Eco-Credits for Discount (Available: " + currentUser.getEcoCredits() + " pts)");
        chkRedeemCredits.setFont(UITheme.BODY_BOLD);
        chkRedeemCredits.setForeground(UITheme.ACCENT_AMBER);
        chkRedeemCredits.setOpaque(false);
        chkRedeemCredits.addActionListener(e -> recalculateNetFare());
        payCard.add(chkRedeemCredits);
        payCard.add(Box.createVerticalStrut(8));

        lblNetFare = new JLabel("Net Payable from Digital Wallet: INR " + String.format("%.2f", grossFare));
        lblNetFare.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNetFare.setForeground(UITheme.PRIMARY);
        payCard.add(lblNetFare);

        content.add(payCard);
        add(content, BorderLayout.CENTER);

        // Buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 14));
        footer.setBackground(UITheme.BG_DARK);

        JButton btnCancel = UITheme.createSecondaryButton("Back to Ride");
        btnCancel.addActionListener(e -> dispose());

        JButton btnConfirm = UITheme.createPrimaryButton("Confirm Docking & Pay");
        btnConfirm.addActionListener(e -> completeDocking());

        footer.add(btnCancel);
        footer.add(btnConfirm);
        add(footer, BorderLayout.SOUTH);

        updateDockCapacityTelemetry();
    }

    private void updateDockCapacityTelemetry() {
        Station s = (Station) comboStations.getSelectedItem();
        if (s == null) return;

        int emptyDocks = s.getEmptyDocks();
        double occupancy = s.getOccupancyRate();

        if (emptyDocks == 0) {
            lblCapacityStatus.setText("Status: FULL (0 Empty Docks) - CANNOT DOCK HERE");
            lblCapacityStatus.setForeground(UITheme.ACCENT_ROSE);
            lblRebalanceAlert.setText("[ALERT] Saturation Alert! Please return to Central Campus Dock (350m away).");
            lblRebalanceAlert.setForeground(UITheme.ACCENT_AMBER);
        } else if (occupancy >= 80.0) {
            lblCapacityStatus.setText("Status: HIGH CONGESTION (" + emptyDocks + " docks left)");
            lblCapacityStatus.setForeground(UITheme.ACCENT_AMBER);
            lblRebalanceAlert.setText("[ALERT] Station nearly full. Operator fleet rebalance work-order triggered.");
            lblRebalanceAlert.setForeground(UITheme.ACCENT_AMBER);
        } else {
            lblCapacityStatus.setText("Status: BALANCED (" + emptyDocks + " of " + s.getTotalDocks() + " docks available)");
            lblCapacityStatus.setForeground(UITheme.PRIMARY);
            lblRebalanceAlert.setText("[OK] Smooth dock availability confirmed.");
            lblRebalanceAlert.setForeground(UITheme.TEXT_MUTED);
        }
    }

    private void recalculateNetFare() {
        double discount = 0.0;
        User u = DataRepository.getInstance().getCurrentUser();
        if (chkRedeemCredits.isSelected()) {
            discount = Math.min(grossFare - 5.0, u.getEcoCredits()); // Keep at least ₹5 base
        }
        double net = Math.max(0.0, grossFare - discount);
        lblNetFare.setText("Net Payable from Digital Wallet: INR " + String.format("%.2f", net) + " (Discount: INR " + String.format("%.2f", discount) + ")");
    }

    private void completeDocking() {
        Station endStation = (Station) comboStations.getSelectedItem();
        if (endStation == null) return;

        if (endStation.getEmptyDocks() <= 0) {
            JOptionPane.showMessageDialog(this, "This docking station is completely full. Please choose another station with available empty docks.", "Dock Capacity Locked", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double discount = 0.0;
        User u = DataRepository.getInstance().getCurrentUser();
        if (chkRedeemCredits.isSelected()) {
            discount = Math.min(grossFare - 5.0, u.getEcoCredits());
            u.setEcoCredits(u.getEcoCredits() - (int) discount);
        }
        double net = Math.max(0.0, grossFare - discount);

        // Execute completion in DataRepository
        DataRepository.getInstance().completeRental(rental, endStation, durationMinutes, net, ecoCreditsEarned, co2Saved);

        rideFrame.finishRide();
        dashboard.updateDashboard();
        dispose();

        // Show Invoice Receipt
        new ReceiptDialog(dashboard, rental, endStation, durationMinutes, grossFare, discount, net, ecoCreditsEarned, co2Saved).setVisible(true);
    }

    private JLabel createLabel(String t) {
        JLabel l = new JLabel(t);
        l.setFont(UITheme.BODY_BOLD);
        l.setForeground(UITheme.TEXT_LIGHT);
        return l;
    }

    private JLabel createValue(String v) {
        JLabel l = new JLabel(v);
        l.setFont(UITheme.BODY_FONT);
        l.setForeground(UITheme.TEXT_MUTED);
        return l;
    }
}
