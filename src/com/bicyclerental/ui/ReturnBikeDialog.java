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
 * Demonstrates USP: Dynamic Dock Rebalancing, Capacity Intelligence, and One-Click Redirect.
 * Revamped for high-contrast executive aesthetic with crisp cards and 100% WCAG AAA visibility.
 */
public class ReturnBikeDialog extends JDialog {

    private final ActiveRideFrame rideFrame;
    private final CustomerDashboard dashboard;
    private final Rental rental;
    private final int durationMinutes;
    private final double distanceKm;
    private final double grossFare;
    private final int ecoCreditsEarned;
    private final double co2Saved;

    private JComboBox<Station> comboStations;
    private JProgressBar capacityBar;
    private JLabel lblCapacityStatus;
    private JLabel lblRebalanceAlert;
    private JButton btnSwitchAlternative;
    private JCheckBox chkRedeemCredits;
    private JLabel lblNetFare;

    public ReturnBikeDialog(Window parentWindow, ActiveRideFrame rideFrame, CustomerDashboard dashboard, Rental rental,
                            int durationMinutes, double distanceKm, double grossFare, int ecoCreditsEarned, double co2Saved) {
        super(parentWindow, "Dock Bicycle & Settle Payment - EcoRide", ModalityType.APPLICATION_MODAL);
        this.rideFrame = rideFrame;
        this.dashboard = dashboard;
        this.rental = rental;
        this.durationMinutes = durationMinutes;
        this.distanceKm = distanceKm;
        this.grossFare = grossFare;
        this.ecoCreditsEarned = ecoCreditsEarned;
        this.co2Saved = co2Saved;

        setSize(600, 680);
        setLocationRelativeTo(parentWindow);
        getContentPane().setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout());

        initUI();
    }

    public ReturnBikeDialog(ActiveRideFrame rideFrame, CustomerDashboard dashboard, Rental rental,
                            int durationMinutes, double grossFare, int ecoCreditsEarned, double co2Saved) {
        this(rideFrame != null ? rideFrame : dashboard, rideFrame, dashboard, rental, durationMinutes, (durationMinutes / 60.0) * 14.0, grossFare, ecoCreditsEarned, co2Saved);
    }

    public ReturnBikeDialog(CustomerDashboard dashboard, Rental rental,
                            int durationMinutes, double distanceKm, double grossFare, int ecoCreditsEarned, double co2Saved) {
        this(dashboard, null, dashboard, rental, durationMinutes, distanceKm, grossFare, ecoCreditsEarned, co2Saved);
    }

    private void initUI() {
        // Header
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(UITheme.BG_DARK);
        header.setBorder(new EmptyBorder(18, 24, 6, 24));

        JLabel lblTitle = new JLabel("Return Bicycle & Settle Fare");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(lblTitle);

        header.add(Box.createVerticalStrut(4));

        JLabel lblSubtitle = new JLabel("Review journey metrics, dock capacity, and wallet settlement");
        lblSubtitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSubtitle.setForeground(Color.BLACK);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(lblSubtitle);

        add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setBackground(UITheme.BG_DARK);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(10, 24, 10, 24));

        // 1. Trip Summary 2x2 Stat Grid (Apple Health / Modern SaaS aesthetic)
        JPanel tripCard = UITheme.createCardPanel();
        tripCard.setLayout(new BoxLayout(tripCard, BoxLayout.Y_AXIS));

        JLabel lblCard1 = new JLabel("TRIP SUMMARY & ECO IMPACT");
        lblCard1.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCard1.setForeground(Color.BLACK);
        tripCard.add(lblCard1);
        tripCard.add(Box.createVerticalStrut(10));

        JPanel statGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        statGrid.setOpaque(false);
        statGrid.add(UITheme.createStatBox("Duration", durationMinutes + " mins", Color.BLACK));
        statGrid.add(UITheme.createStatBox("CO2 Offset", String.format("%.2f kg", co2Saved), Color.BLACK));
        statGrid.add(UITheme.createStatBox("Gross Fare", String.format("INR %.2f", grossFare), Color.BLACK));
        statGrid.add(UITheme.createStatBox("Eco-Credits", "+" + ecoCreditsEarned + " pts", Color.BLACK));
        tripCard.add(statGrid);

        content.add(tripCard);
        content.add(Box.createVerticalStrut(12));

        // 2. Dock Rebalancing Station Selector
        JPanel dockCard = UITheme.createCardPanel();
        dockCard.setLayout(new BoxLayout(dockCard, BoxLayout.Y_AXIS));

        JLabel lblDockTitle = new JLabel("SMART DOCK CAPACITY & REBALANCING");
        lblDockTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDockTitle.setForeground(Color.BLACK);
        dockCard.add(lblDockTitle);
        dockCard.add(Box.createVerticalStrut(10));

        comboStations = new JComboBox<>();
        List<Station> stations = DataRepository.getInstance().getAllStations();
        for (Station s : stations) {
            comboStations.addItem(s);
        }
        comboStations.setFont(UITheme.BODY_BOLD);
        comboStations.setBackground(UITheme.BG_INPUT);
        comboStations.setForeground(UITheme.TEXT_LIGHT);
        comboStations.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                l.setBorder(new EmptyBorder(6, 10, 6, 10));
                l.setFont(UITheme.BODY_BOLD);
                if (isSelected) {
                    l.setBackground(new Color(209, 250, 229));
                    l.setForeground(Color.BLACK);
                } else {
                    l.setBackground(Color.WHITE);
                    l.setForeground(Color.BLACK);
                }
                return l;
            }
        });
        comboStations.addActionListener(e -> updateDockCapacityTelemetry());
        dockCard.add(comboStations);
        dockCard.add(Box.createVerticalStrut(10));

        // Capacity Progress Bar
        capacityBar = new JProgressBar(0, 100);
        capacityBar.setPreferredSize(new Dimension(300, 16));
        dockCard.add(capacityBar);
        dockCard.add(Box.createVerticalStrut(6));

        lblCapacityStatus = new JLabel("Dock Status: ");
        lblCapacityStatus.setFont(new Font("Segoe UI", Font.BOLD, 13));
        dockCard.add(lblCapacityStatus);
        dockCard.add(Box.createVerticalStrut(4));

        lblRebalanceAlert = new JLabel(" ");
        lblRebalanceAlert.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRebalanceAlert.setForeground(UITheme.TEXT_MUTED);
        dockCard.add(lblRebalanceAlert);
        dockCard.add(Box.createVerticalStrut(6));

        btnSwitchAlternative = UITheme.createSecondaryButton("Redirect to Recommended Dock");
        btnSwitchAlternative.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSwitchAlternative.setVisible(false);
        btnSwitchAlternative.addActionListener(e -> {
            for (int i = 0; i < comboStations.getItemCount(); i++) {
                if (comboStations.getItemAt(i).getStationId() == 1) {
                    comboStations.setSelectedIndex(i);
                    break;
                }
            }
        });
        dockCard.add(btnSwitchAlternative);

        content.add(dockCard);
        content.add(Box.createVerticalStrut(12));

        // 3. Payment & Eco-Credit Redemption Card
        User currentUser = DataRepository.getInstance().getCurrentUser();
        JPanel payCard = UITheme.createCardPanel();
        payCard.setLayout(new BoxLayout(payCard, BoxLayout.Y_AXIS));

        chkRedeemCredits = new JCheckBox("Redeem Eco-Credits for Fare Discount (Available: " + currentUser.getEcoCredits() + " pts)");
        chkRedeemCredits.setFont(UITheme.BODY_BOLD);
        chkRedeemCredits.setForeground(UITheme.TEXT_LIGHT);
        chkRedeemCredits.setOpaque(false);
        chkRedeemCredits.addActionListener(e -> recalculateNetFare());
        payCard.add(chkRedeemCredits);
        payCard.add(Box.createVerticalStrut(10));

        // Highlight Net Payable Box (Soft Emerald Accent)
        JPanel netPayBox = new JPanel(new BorderLayout());
        netPayBox.setBackground(new Color(240, 253, 244)); // Emerald 50
        netPayBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(134, 239, 172), 1, true), // Emerald 300
                new EmptyBorder(10, 14, 10, 14)
        ));

        JLabel lblPayDesc = new JLabel("Net Payable from Digital Wallet:");
        lblPayDesc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPayDesc.setForeground(Color.BLACK);
        netPayBox.add(lblPayDesc, BorderLayout.WEST);

        lblNetFare = new JLabel(String.format("INR %.2f", grossFare));
        lblNetFare.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNetFare.setForeground(Color.BLACK);
        netPayBox.add(lblNetFare, BorderLayout.EAST);

        payCard.add(netPayBox);
        content.add(payCard);

        add(content, BorderLayout.CENTER);

        // Footer Buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 14));
        footer.setBackground(UITheme.BG_DARK);

        JButton btnCancel = UITheme.createSecondaryButton("Back to Ride");
        btnCancel.setFont(UITheme.BODY_BOLD);
        btnCancel.addActionListener(e -> dispose());

        JButton btnConfirm = UITheme.createPrimaryButton("Confirm Return & Pay");
        btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 14));
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
        capacityBar.setValue((int) occupancy);

        Station bestAlternative = null;
        double lowestOccupancy = 100.0;
        for (int i = 0; i < comboStations.getItemCount(); i++) {
            Station candidate = comboStations.getItemAt(i);
            if (candidate.getStationId() != s.getStationId() && candidate.getEmptyDocks() > 0) {
                if (candidate.getOccupancyRate() < lowestOccupancy) {
                    lowestOccupancy = candidate.getOccupancyRate();
                    bestAlternative = candidate;
                }
            }
        }
        final Station targetStation = bestAlternative;
        if (targetStation != null) {
            btnSwitchAlternative.setText("Redirect to " + targetStation.getStationName() + " (" + targetStation.getEmptyDocks() + " free docks)");
            for (java.awt.event.ActionListener al : btnSwitchAlternative.getActionListeners()) {
                btnSwitchAlternative.removeActionListener(al);
            }
            btnSwitchAlternative.addActionListener(e -> comboStations.setSelectedItem(targetStation));
        }

        if (emptyDocks == 0) {
            capacityBar.setForeground(UITheme.ACCENT_ROSE);
            lblCapacityStatus.setText("Status: Station Full (0 Free Docks) - Unable to Dock");
            lblCapacityStatus.setForeground(UITheme.ACCENT_ROSE);
            lblRebalanceAlert.setText("Station is at 100% capacity. Please select an alternative nearby dock.");
            lblRebalanceAlert.setForeground(UITheme.ACCENT_ROSE);
            btnSwitchAlternative.setVisible(true);
        } else if (occupancy >= 80.0) {
            capacityBar.setForeground(UITheme.ACCENT_AMBER);
            lblCapacityStatus.setText("Status: High Congestion (" + emptyDocks + " free docks left)");
            lblCapacityStatus.setForeground(UITheme.ACCENT_AMBER);
            lblRebalanceAlert.setText("Station is nearly full. We recommend docking at our nearby partner dock.");
            lblRebalanceAlert.setForeground(UITheme.ACCENT_AMBER);
            btnSwitchAlternative.setVisible(true);
        } else {
            capacityBar.setForeground(UITheme.PRIMARY_DARK);
            lblCapacityStatus.setText("Status: Normal Availability (" + emptyDocks + " of " + s.getTotalDocks() + " docks free)");
            lblCapacityStatus.setForeground(Color.BLACK);
            lblRebalanceAlert.setText("Smooth dock availability confirmed at this station.");
            lblRebalanceAlert.setForeground(Color.BLACK);
            btnSwitchAlternative.setVisible(false);
        }
    }

    private void recalculateNetFare() {
        double discount = 0.0;
        User u = DataRepository.getInstance().getCurrentUser();
        if (chkRedeemCredits.isSelected()) {
            discount = Math.min(grossFare - 5.0, u.getEcoCredits());
        }
        double net = Math.max(0.0, grossFare - discount);
        if (discount > 0) {
            lblNetFare.setText(String.format("INR %.2f (-INR %.2f)", net, discount));
        } else {
            lblNetFare.setText(String.format("INR %.2f", net));
        }
    }

    private void completeDocking() {
        Station endStation = (Station) comboStations.getSelectedItem();
        if (endStation == null) return;

        if (endStation.getEmptyDocks() <= 0) {
            UITheme.showStyledMessage(this, "This docking station is completely full. Please click the button to switch to the recommended alternative dock.", "Dock Capacity Locked", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double discount = 0.0;
        User u = DataRepository.getInstance().getCurrentUser();
        if (chkRedeemCredits.isSelected()) {
            discount = Math.min(grossFare - 5.0, u.getEcoCredits());
            u.setEcoCredits(u.getEcoCredits() - (int) discount);
        }
        double net = Math.max(0.0, grossFare - discount);

        DataRepository.getInstance().completeRental(rental, endStation, durationMinutes, distanceKm, net, ecoCreditsEarned, co2Saved);

        if (rideFrame != null) {
            rideFrame.finishRide();
        }
        if (dashboard != null) {
            dashboard.endActiveRide();
        }
        dispose();

        new ReceiptDialog(dashboard, rental, endStation, durationMinutes, grossFare, discount, net, ecoCreditsEarned, co2Saved).setVisible(true);
    }
}
