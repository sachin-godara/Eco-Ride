package com.bicyclerental.ui;

import com.bicyclerental.model.*;
import com.bicyclerental.service.DataRepository;
import com.bicyclerental.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * CustomerDashboard: Comprehensive modern rider portal.
 * Features multi-tab navigation: Live Booking, Trip History, Wallet & Rewards, Maintenance Reporting.
 */
public class CustomerDashboard extends JFrame {

    private final User user;
    private JLabel lblWalletMetric;
    private JLabel lblCreditsMetric;
    private JLabel lblCo2Metric;
    private JLabel lblActiveStatusMetric;

    // Tab 1 Components
    private JComboBox<Station> comboStations;
    private JComboBox<String> comboTypeFilter;
    private JTable fleetTable;
    private DefaultTableModel fleetTableModel;
    private JProgressBar stationCapacityBar;
    private JLabel lblStationCapacityText;
    private JButton btnUnlockBike;
    private Timer masterRideTimer;
    private JPanel activeRideAlertCard;
    private JLabel lblLiveTripHeader;
    private JLabel lblLiveTimer;
    private JLabel lblLiveFare;
    private JLabel lblLiveDistance;
    private JLabel lblLiveCo2;
    private JLabel lblLiveCalories;
    private JButton btnDashboardPopout;
    private JButton btnDashboardDock;

    // Tab 2 Components (History)
    private JTable historyTable;
    private DefaultTableModel historyTableModel;

    // Tab 3 Components (Wallet)
    private JLabel lblWalletDetailed;
    private JLabel lblCreditsDetailed;

    public CustomerDashboard(User user) {
        this.user = user;
        setTitle("EcoRide - Rider Portal | " + user.getFullName());
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout());

        initUI();
        refreshAllData();
    }

    private void initUI() {
        // Top Navigation Header
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UITheme.BG_CARD);
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER_COLOR),
                new EmptyBorder(14, 24, 14, 24)
        ));

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brand.setOpaque(false);
        JLabel lblLogo = new JLabel("EcoRide Portal");
        lblLogo.setFont(UITheme.TITLE_FONT);
        lblLogo.setForeground(Color.BLACK);

        JLabel lblTag = new JLabel("Campus Smart Bike Sharing");
        lblTag.setFont(UITheme.BODY_FONT);
        lblTag.setForeground(UITheme.TEXT_MUTED);
        brand.add(lblLogo);
        brand.add(lblTag);

        JPanel rightUser = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        rightUser.setOpaque(false);
        JLabel lblName = new JLabel(user.getFullName() + " (" + user.getUsername() + ")");
        lblName.setFont(UITheme.BODY_BOLD);
        lblName.setForeground(UITheme.TEXT_LIGHT);

        JButton btnSignOut = UITheme.createSecondaryButton("Sign Out");
        btnSignOut.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        rightUser.add(lblName);
        rightUser.add(btnSignOut);

        topBar.add(brand, BorderLayout.WEST);
        topBar.add(rightUser, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // Center Container
        JPanel centerContainer = new JPanel();
        centerContainer.setBackground(UITheme.BG_DARK);
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));
        centerContainer.setBorder(new EmptyBorder(16, 20, 16, 20));

        // 4 Key Metric Cards
        JPanel metricsRow = new JPanel(new GridLayout(1, 4, 14, 0));
        metricsRow.setOpaque(false);
        metricsRow.setMaximumSize(new Dimension(1400, 95));

        lblWalletMetric = new JLabel("INR 0.00");
        metricsRow.add(createMetricTile("Digital Wallet", lblWalletMetric, Color.BLACK));

        lblCreditsMetric = new JLabel("0 pts");
        metricsRow.add(createMetricTile("Eco-Reward Points", lblCreditsMetric, UITheme.ACCENT_AMBER));

        lblCo2Metric = new JLabel("0.00 kg");
        metricsRow.add(createMetricTile("Total CO2 Avoided", lblCo2Metric, UITheme.ACCENT_BLUE));

        lblActiveStatusMetric = new JLabel("IDLE");
        metricsRow.add(createMetricTile("Trip Status", lblActiveStatusMetric, UITheme.TEXT_ACCENT));

        centerContainer.add(metricsRow);
        centerContainer.add(Box.createVerticalStrut(14));

        // Embedded Active Ride Live HUD (Visible when a ride is active)
        activeRideAlertCard = UITheme.createCardPanel();
        activeRideAlertCard.setLayout(new BorderLayout(10, 8));
        activeRideAlertCard.setMaximumSize(new Dimension(1400, 150));
        activeRideAlertCard.setBackground(new Color(236, 253, 245)); // Aesthetic mint / emerald 50
        activeRideAlertCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(16, 185, 129), 1),
                new EmptyBorder(10, 16, 10, 16)
        ));

        // Top Header of Active Ride Card
        JPanel hudTop = new JPanel(new BorderLayout());
        hudTop.setOpaque(false);

        lblLiveTripHeader = new JLabel("[LIVE TRIP] BK-101 | Origin: Central Campus Dock");
        lblLiveTripHeader.setFont(UITheme.BODY_BOLD);
        lblLiveTripHeader.setForeground(Color.BLACK);

        JPanel hudTopRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        hudTopRight.setOpaque(false);

        btnDashboardPopout = UITheme.createSecondaryButton("Pop-out HUD");
        btnDashboardPopout.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnDashboardPopout.addActionListener(e -> openActiveRide());

        hudTopRight.add(btnDashboardPopout);

        hudTop.add(lblLiveTripHeader, BorderLayout.WEST);
        hudTop.add(hudTopRight, BorderLayout.EAST);
        activeRideAlertCard.add(hudTop, BorderLayout.NORTH);

        // Center: 5 Telemetry Metrics Grid
        JPanel telemetryPanel = new JPanel(new GridLayout(1, 5, 12, 0));
        telemetryPanel.setOpaque(false);

        lblLiveTimer = new JLabel("00:00:00");
        telemetryPanel.add(createMiniHudTile("TRIP DURATION", lblLiveTimer, UITheme.TEXT_LIGHT, 24));

        lblLiveFare = new JLabel("INR 10.00");
        telemetryPanel.add(createMiniHudTile("CURRENT CHARGE", lblLiveFare, UITheme.ACCENT_AMBER, 20));

        lblLiveDistance = new JLabel("0.00 km");
        telemetryPanel.add(createMiniHudTile("DISTANCE", lblLiveDistance, UITheme.ACCENT_BLUE, 20));

        lblLiveCo2 = new JLabel("0.00 kg");
        telemetryPanel.add(createMiniHudTile("CO2 AVOIDED", lblLiveCo2, Color.BLACK, 20));

        lblLiveCalories = new JLabel("0 kcal");
        telemetryPanel.add(createMiniHudTile("CALORIES", lblLiveCalories, Color.BLACK, 20));

        activeRideAlertCard.add(telemetryPanel, BorderLayout.CENTER);

        // Bottom: Prominent Return Docking Button
        JPanel hudBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        hudBottom.setOpaque(false);

        btnDashboardDock = UITheme.createPrimaryButton("End Ride & Settle Fare");
        btnDashboardDock.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDashboardDock.addActionListener(e -> openReturnDialogFromDashboard());
        hudBottom.add(btnDashboardDock);

        activeRideAlertCard.add(hudBottom, BorderLayout.SOUTH);
        centerContainer.add(activeRideAlertCard);
        centerContainer.add(Box.createVerticalStrut(12));

        // Main Tabbed Pane
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UITheme.BODY_BOLD);
        tabs.setBackground(UITheme.BG_CARD);
        tabs.setForeground(UITheme.TEXT_LIGHT);

        // TAB 1: Live Fleet & Booking
        tabs.addTab("Live Fleet & Booking", createBookingTab());

        // TAB 2: Trip History & Invoices
        tabs.addTab("My Trip History & Receipts", createHistoryTab());

        // TAB 3: Digital Wallet & Eco-Rewards
        tabs.addTab("Wallet & Green Rewards", createWalletTab());

        // TAB 4: Defect Reporting
        tabs.addTab("Report Bike Issue", createReportTab());

        centerContainer.add(tabs);
        add(centerContainer, BorderLayout.CENTER);
    }

    private JPanel createMetricTile(String title, JLabel value, Color color) {
        JPanel card = UITheme.createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 11));
        t.setForeground(UITheme.TEXT_MUTED);

        value.setFont(UITheme.METRIC_FONT);
        value.setForeground(color);

        card.add(t);
        card.add(Box.createVerticalStrut(4));
        card.add(value);
        return card;
    }

    private JPanel createBookingTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Filter Bar & Station Capacity
        JPanel filterCard = UITheme.createCardPanel();
        filterCard.setLayout(new BorderLayout());

        JPanel filtersLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 4));
        filtersLeft.setOpaque(false);

        JLabel lblStation = new JLabel("Docking Station:");
        lblStation.setFont(UITheme.BODY_BOLD);
        lblStation.setForeground(UITheme.TEXT_LIGHT);
        filtersLeft.add(lblStation);

        comboStations = new JComboBox<>();
        for (Station s : DataRepository.getInstance().getAllStations()) {
            comboStations.addItem(s);
        }
        comboStations.setFont(UITheme.BODY_FONT);
        comboStations.setBackground(UITheme.BG_INPUT);
        comboStations.setForeground(UITheme.TEXT_LIGHT);
        comboStations.addActionListener(e -> updateStationCapacityAndBikes());
        filtersLeft.add(comboStations);

        JLabel lblType = new JLabel("Category:");
        lblType.setFont(UITheme.BODY_BOLD);
        lblType.setForeground(UITheme.TEXT_LIGHT);
        filtersLeft.add(lblType);

        comboTypeFilter = new JComboBox<>(new String[]{"ALL", "STANDARD", "GEARED", "ELECTRIC"});
        comboTypeFilter.setFont(UITheme.BODY_FONT);
        comboTypeFilter.setBackground(UITheme.BG_INPUT);
        comboTypeFilter.setForeground(UITheme.TEXT_LIGHT);
        comboTypeFilter.addActionListener(e -> loadFleetTable());
        filtersLeft.add(comboTypeFilter);

        filterCard.add(filtersLeft, BorderLayout.WEST);

        // Station Capacity Meter on Right
        JPanel capacityRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        capacityRight.setOpaque(false);

        lblStationCapacityText = new JLabel("Station Occupancy: 0%");
        lblStationCapacityText.setFont(UITheme.BODY_BOLD);
        lblStationCapacityText.setForeground(Color.BLACK);

        stationCapacityBar = new JProgressBar(0, 100);
        stationCapacityBar.setPreferredSize(new Dimension(140, 18));
        stationCapacityBar.setForeground(UITheme.PRIMARY_DARK);

        capacityRight.add(lblStationCapacityText);
        capacityRight.add(stationCapacityBar);
        filterCard.add(capacityRight, BorderLayout.EAST);

        panel.add(filterCard, BorderLayout.NORTH);

        // Fleet Table
        String[] cols = {"Bike ID", "Bike Code", "Model", "Category", "Rate / Hour", "Battery / Condition", "Status"};
        fleetTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        fleetTable = new JTable(fleetTableModel);
        fleetTable.setFont(UITheme.BODY_BOLD);
        fleetTable.setRowHeight(36);
        fleetTable.setBackground(UITheme.BG_CARD);
        fleetTable.setForeground(Color.BLACK);
        fleetTable.setSelectionBackground(UITheme.PRIMARY_DARK);
        fleetTable.setSelectionForeground(Color.WHITE);
        fleetTable.getTableHeader().setBackground(new Color(241, 245, 249));
        fleetTable.getTableHeader().setForeground(Color.BLACK);
        fleetTable.getTableHeader().setFont(UITheme.BODY_BOLD);

        // Apply custom status renderer
        DefaultTableCellRenderer renderer = UITheme.createStatusCellRenderer();
        for (int i = 0; i < fleetTable.getColumnCount(); i++) {
            fleetTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane scroll = new JScrollPane(fleetTable);
        scroll.getViewport().setBackground(UITheme.BG_DARK);
        scroll.setBorder(new EmptyBorder(10, 0, 10, 0));
        panel.add(scroll, BorderLayout.CENTER);

        // Bottom CTA
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 10));
        bottomBar.setBackground(UITheme.BG_DARK);

        btnUnlockBike = UITheme.createPrimaryButton("Unlock & Start Rental");
        btnUnlockBike.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnUnlockBike.addActionListener(e -> handleUnlockBike());
        bottomBar.add(btnUnlockBike);

        panel.add(bottomBar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createHistoryTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG_DARK);
        panel.setBorder(new EmptyBorder(14, 14, 14, 14));

        String[] cols = {"Trip ID", "Bike Code", "Origin Station", "Destination", "Duration", "Distance", "Fare Paid", "CO2 Saved", "Status"};
        historyTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        historyTable = new JTable(historyTableModel);
        historyTable.setFont(UITheme.BODY_BOLD);
        historyTable.setRowHeight(36);
        historyTable.setBackground(UITheme.BG_CARD);
        historyTable.setForeground(Color.BLACK);
        historyTable.setSelectionBackground(UITheme.PRIMARY_DARK);
        historyTable.setSelectionForeground(Color.WHITE);
        historyTable.getTableHeader().setBackground(new Color(241, 245, 249));
        historyTable.getTableHeader().setForeground(Color.BLACK);
        historyTable.getTableHeader().setFont(UITheme.BODY_BOLD);

        DefaultTableCellRenderer renderer = UITheme.createStatusCellRenderer();
        for (int i = 0; i < historyTable.getColumnCount(); i++) {
            historyTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        panel.add(new JScrollPane(historyTable), BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footer.setBackground(UITheme.BG_DARK);

        JButton btnViewReceipt = UITheme.createPrimaryButton("View / Reprint Selected Receipt");
        btnViewReceipt.addActionListener(e -> viewSelectedReceipt());
        footer.add(btnViewReceipt);

        panel.add(footer, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createWalletTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.BG_DARK);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel card = UITheme.createCardPanel();
        card.setPreferredSize(new Dimension(560, 360));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("EcoRide Digital Mobility Wallet");
        title.setFont(UITheme.SUBTITLE_FONT);
        title.setForeground(Color.BLACK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(16));

        lblWalletDetailed = new JLabel("Current Balance: INR 0.00");
        lblWalletDetailed.setFont(UITheme.METRIC_FONT);
        lblWalletDetailed.setForeground(UITheme.TEXT_LIGHT);
        lblWalletDetailed.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblWalletDetailed);
        card.add(Box.createVerticalStrut(8));

        lblCreditsDetailed = new JLabel("Eco-Credits: 0 points (Redeemable for INR discounts)");
        lblCreditsDetailed.setFont(UITheme.BODY_BOLD);
        lblCreditsDetailed.setForeground(UITheme.ACCENT_AMBER);
        lblCreditsDetailed.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblCreditsDetailed);
        card.add(Box.createVerticalStrut(24));

        JLabel topUpTitle = new JLabel("Instant Wallet Top-up:");
        topUpTitle.setFont(UITheme.BODY_BOLD);
        topUpTitle.setForeground(UITheme.TEXT_LIGHT);
        topUpTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(topUpTitle);
        card.add(Box.createVerticalStrut(12));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        btnRow.setOpaque(false);

        JButton btn100 = UITheme.createPrimaryButton("+ INR 100");
        btn100.addActionListener(e -> addFunds(100.0));
        JButton btn200 = UITheme.createPrimaryButton("+ INR 200");
        btn200.addActionListener(e -> addFunds(200.0));
        JButton btn500 = UITheme.createPrimaryButton("+ INR 500");
        btn500.addActionListener(e -> addFunds(500.0));

        btnRow.add(btn100);
        btnRow.add(btn200);
        btnRow.add(btn500);
        card.add(btnRow);

        panel.add(card);
        return panel;
    }

    private JPanel createReportTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.BG_DARK);

        JPanel card = UITheme.createCardPanel();
        card.setPreferredSize(new Dimension(500, 380));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Report Bicycle Maintenance Issue");
        title.setFont(UITheme.SUBTITLE_FONT);
        title.setForeground(UITheme.ACCENT_ROSE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(16));

        JComboBox<Bicycle> cmbBikes = new JComboBox<>();
        for (Bicycle b : DataRepository.getInstance().getAllBicycles()) {
            cmbBikes.addItem(b);
        }
        cmbBikes.setFont(UITheme.BODY_FONT);
        cmbBikes.setBackground(UITheme.BG_INPUT);
        cmbBikes.setForeground(UITheme.TEXT_LIGHT);
        JLabel lblSelectBike = new JLabel("Select Bicycle:");
        lblSelectBike.setFont(UITheme.BODY_BOLD);
        lblSelectBike.setForeground(UITheme.TEXT_LIGHT);
        card.add(lblSelectBike);
        card.add(cmbBikes);
        card.add(Box.createVerticalStrut(10));

        JComboBox<String> cmbDefect = new JComboBox<>(new String[]{"Flat Tire / Puncture", "Brake Slippage", "Chain Stuck / Loose", "Battery Fault", "Other"});
        cmbDefect.setFont(UITheme.BODY_FONT);
        cmbDefect.setBackground(UITheme.BG_INPUT);
        cmbDefect.setForeground(UITheme.TEXT_LIGHT);
        JLabel lblDefectCat = new JLabel("Defect Category:");
        lblDefectCat.setFont(UITheme.BODY_BOLD);
        lblDefectCat.setForeground(UITheme.TEXT_LIGHT);
        card.add(lblDefectCat);
        card.add(cmbDefect);
        card.add(Box.createVerticalStrut(10));

        JTextField txtDesc = UITheme.createStyledTextField(20);
        txtDesc.setText("Front brake cable loose, requires servicing.");
        JLabel lblDesc = new JLabel("Brief Description:");
        lblDesc.setFont(UITheme.BODY_BOLD);
        lblDesc.setForeground(UITheme.TEXT_LIGHT);
        card.add(lblDesc);
        card.add(txtDesc);
        card.add(Box.createVerticalStrut(16));

        JButton btnSubmit = UITheme.createPrimaryButton("Submit Defect Report to Fleet Technicians");
        btnSubmit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSubmit.addActionListener(e -> {
            Bicycle b = (Bicycle) cmbBikes.getSelectedItem();
            if (b != null) {
                DataRepository.getInstance().reportMaintenance(b.getBikeId(), (String) cmbDefect.getSelectedItem(), txtDesc.getText().trim());
                UITheme.showStyledMessage(this, "Issue logged. Bicycle " + b.getBikeCode() + " has been flagged for MAINTENANCE and locked from rentals.", "Report Recorded", JOptionPane.INFORMATION_MESSAGE);
                refreshAllData();
            }
        });
        card.add(btnSubmit);

        panel.add(card);
        return panel;
    }

    private void addFunds(double amount) {
        user.setWalletBalance(user.getWalletBalance() + amount);
        refreshAllData();
        UITheme.showStyledMessage(this, "INR " + String.format("%.2f", amount) + " added to your digital wallet successfully.", "Wallet Recharged", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateStationCapacityAndBikes() {
        Station s = (Station) comboStations.getSelectedItem();
        if (s != null) {
            int rate = (int) s.getOccupancyRate();
            stationCapacityBar.setValue(rate);
            lblStationCapacityText.setText("Occupancy: " + s.getAvailableBikes() + "/" + s.getTotalDocks() + " (" + rate + "%)");
            if (rate >= 80) {
                stationCapacityBar.setForeground(UITheme.ACCENT_AMBER);
            } else {
                stationCapacityBar.setForeground(UITheme.PRIMARY);
            }
        }
        loadFleetTable();
    }

    private void loadFleetTable() {
        fleetTableModel.setRowCount(0);
        Station s = (Station) comboStations.getSelectedItem();
        if (s == null) return;

        String typeFilter = (String) comboTypeFilter.getSelectedItem();
        List<Bicycle> bikes = DataRepository.getInstance().getBicyclesForStation(s.getStationId(), typeFilter);

        for (Bicycle b : bikes) {
            String batteryDisplay = b.isElectric() ? b.getBatteryLevel() + "% Battery" : "Mechanical (100%)";
            fleetTableModel.addRow(new Object[]{
                    b.getBikeId(),
                    b.getBikeCode(),
                    b.getModelName(),
                    b.getBikeType(),
                    "INR " + String.format("%.2f", b.getHourlyRate()) + "/hr",
                    batteryDisplay,
                    b.getStatus()
            });
        }
    }

    private void loadHistoryTable() {
        historyTableModel.setRowCount(0);
        List<Rental> list = DataRepository.getInstance().getUserRentalHistory(user.getUserId());
        for (Rental r : list) {
            Bicycle b = DataRepository.getInstance().getBicycleById(r.getBikeId());
            Station start = DataRepository.getInstance().getStationById(r.getStartStationId());
            Station end = r.getEndStationId() != null ? DataRepository.getInstance().getStationById(r.getEndStationId()) : null;

            historyTableModel.addRow(new Object[]{
                    "TRP-" + r.getRentalId(),
                    b != null ? b.getBikeCode() : "N/A",
                    start != null ? start.getStationName() : "N/A",
                    end != null ? end.getStationName() : "In Progress",
                    r.getDurationMinutes() + " mins",
                    String.format("%.1f km", r.getDistanceKm()),
                    "INR " + String.format("%.2f", r.getTotalCost()),
                    String.format("%.2f kg", r.getCo2SavedKg()),
                    r.getRentalStatus()
            });
        }
    }

    public void refreshAllData() {
        lblWalletMetric.setText("INR " + String.format("%.2f", user.getWalletBalance()));
        lblCreditsMetric.setText(user.getEcoCredits() + " pts");
        lblWalletDetailed.setText("Current Balance: INR " + String.format("%.2f", user.getWalletBalance()));
        lblCreditsDetailed.setText("Eco-Credits: " + user.getEcoCredits() + " points (Redeemable for INR discounts)");

        // Calculate total CO2 avoided across trips
        double totalCo2 = 0.0;
        for (Rental r : DataRepository.getInstance().getUserRentalHistory(user.getUserId())) {
            totalCo2 += r.getCo2SavedKg();
        }
        lblCo2Metric.setText(String.format("%.2f kg", totalCo2));

        Rental active = DataRepository.getInstance().getActiveRental();
        if (active != null) {
            lblActiveStatusMetric.setText("1 RIDE ACTIVE");
            lblActiveStatusMetric.setForeground(UITheme.ACCENT_AMBER);
            btnUnlockBike.setEnabled(false);
            btnUnlockBike.setText("Trip in Progress - Dock Current Bicycle to Rent Another");
            if (masterRideTimer == null || !masterRideTimer.isRunning()) {
                startMasterRideTimer();
            }
            updateActiveRideDisplay();
        } else {
            lblActiveStatusMetric.setText("IDLE");
            lblActiveStatusMetric.setForeground(Color.BLACK);
            activeRideAlertCard.setVisible(false);
            btnUnlockBike.setEnabled(true);
            btnUnlockBike.setText("Unlock & Start Rental");
            if (masterRideTimer != null && masterRideTimer.isRunning()) {
                masterRideTimer.stop();
            }
        }

        updateStationCapacityAndBikes();
        loadHistoryTable();
    }

    private JPanel createMiniHudTile(String labelText, JLabel valueLabel, Color color, int fontSize) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(labelText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 11));
        title.setForeground(Color.BLACK);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        valueLabel.setForeground(color);

        p.add(title);
        p.add(Box.createVerticalStrut(2));
        p.add(valueLabel);
        return p;
    }

    private void handleUnlockBike() {
        int selectedRow = fleetTable.getSelectedRow();
        if (selectedRow == -1) {
            UITheme.showStyledMessage(this, "Please select an available bicycle from the table first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bikeId = (int) fleetTableModel.getValueAt(selectedRow, 0);
        Bicycle bike = DataRepository.getInstance().getBicycleById(bikeId);
        Station station = (Station) comboStations.getSelectedItem();

        if (bike == null || station == null) return;

        if (!"AVAILABLE".equalsIgnoreCase(bike.getStatus())) {
            UITheme.showStyledMessage(this, "Selected bicycle is currently " + bike.getStatus() + ". Please choose an available bicycle.", "Unavailable", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (user.getWalletBalance() < 50.00) {
            UITheme.showStyledMessage(this, "Minimum wallet balance of INR 50.00 is required to initiate a ride. Please top up your wallet in the Wallet tab.", "Insufficient Funds", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Start Rental & Generate OTP
        Rental rental = DataRepository.getInstance().startRental(bike, station);

        String message = "Rental Reservation Confirmed\n\n" +
                         "Vehicle: " + bike.getBikeCode() + " (" + bike.getModelName() + ")\n" +
                         "Origin Station: " + station.getStationName() + "\n" +
                         "Pricing: Base INR 10.00 (includes 15 min), then INR " + String.format("%.2f", bike.getHourlyRate()) + "/hr\n\n" +
                         "Smart Dock Passcode: " + rental.getUnlockPin() + "\n\n" +
                         "Enter this passcode on the dock terminal to release the bicycle.";

        UITheme.showStyledMessage(this, message, "Rental Unlocked", JOptionPane.INFORMATION_MESSAGE);

        startActiveRide(rental);
    }

    public void startActiveRide(Rental rental) {
        refreshAllData();
        startMasterRideTimer();
        updateActiveRideDisplay();
        toFront();
        requestFocus();
    }

    public void endActiveRide() {
        if (masterRideTimer != null && masterRideTimer.isRunning()) {
            masterRideTimer.stop();
        }
        ActiveRideFrame hud = ActiveRideFrame.getActiveInstance();
        if (hud != null) {
            hud.finishRide();
        }
        refreshAllData();
    }

    private void startMasterRideTimer() {
        if (masterRideTimer != null && masterRideTimer.isRunning()) {
            masterRideTimer.stop();
        }
        masterRideTimer = new Timer(1000, e -> {
            Rental active = DataRepository.getInstance().getActiveRental();
            if (active != null) {
                active.tick(1);
                updateActiveRideDisplay();
            } else {
                endActiveRide();
            }
        });
        masterRideTimer.start();
    }

    public void updateActiveRideDisplay() {
        Rental active = DataRepository.getInstance().getActiveRental();
        if (active == null) {
            activeRideAlertCard.setVisible(false);
            if (masterRideTimer != null && masterRideTimer.isRunning()) {
                masterRideTimer.stop();
            }
            return;
        }

        activeRideAlertCard.setVisible(true);
        Bicycle b = DataRepository.getInstance().getBicycleById(active.getBikeId());
        Station s = DataRepository.getInstance().getStationById(active.getStartStationId());

        String bikeCode = b != null ? b.getBikeCode() + " (" + b.getModelName() + ")" : "Bicycle #" + active.getBikeId();
        String stationName = s != null ? s.getStationName() : "Station #" + active.getStartStationId();
        lblLiveTripHeader.setText("[LIVE TRIP] " + bikeCode + "  |  Origin: " + stationName + "  |  Unlock PIN: " + active.getUnlockPin());

        int secs = active.getElapsedSeconds();
        int hrs = secs / 3600;
        int mins = (secs % 3600) / 60;
        int remSecs = secs % 60;
        lblLiveTimer.setText(String.format("%02d:%02d:%02d", hrs, mins, remSecs));

        double hourlyRate = b != null ? b.getHourlyRate() : 20.0;
        lblLiveFare.setText(String.format("INR %.2f", active.calculateGrossFare(hourlyRate)));
        lblLiveDistance.setText(String.format("%.2f km", active.calculateDistanceKm()));
        lblLiveCo2.setText(String.format("%.2f kg", active.calculateCo2SavedKg()));
        lblLiveCalories.setText(active.calculateCalories() + " kcal");

        ActiveRideFrame hud = ActiveRideFrame.getActiveInstance();
        if (hud != null) {
            hud.updateTelemetry();
        }
    }

    private void openReturnDialogFromDashboard() {
        Rental active = DataRepository.getInstance().getActiveRental();
        if (active != null) {
            int duration = active.calculateDurationMinutes();
            double dist = active.calculateDistanceKm();
            double co2 = active.calculateCo2SavedKg();
            int credits = active.calculateEcoCredits();
            Bicycle b = DataRepository.getInstance().getBicycleById(active.getBikeId());
            double gross = active.calculateGrossFare(b != null ? b.getHourlyRate() : 20.0);

            new ReturnBikeDialog(this, active, duration, dist, gross, credits, co2).setVisible(true);
        }
    }

    private void openActiveRide() {
        Rental active = DataRepository.getInstance().getActiveRental();
        if (active != null) {
            ActiveRideFrame.openHUD(this, active);
        }
    }

    private void viewSelectedReceipt() {
        int row = historyTable.getSelectedRow();
        if (row == -1) {
            UITheme.showStyledMessage(this, "Please select a trip from the history table to view its receipt.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String tripCode = (String) historyTableModel.getValueAt(row, 0);
        int rentalId = Integer.parseInt(tripCode.replace("TRP-", ""));
        for (Rental r : DataRepository.getInstance().getUserRentalHistory(user.getUserId())) {
            if (r.getRentalId() == rentalId) {
                Station end = r.getEndStationId() != null ? DataRepository.getInstance().getStationById(r.getEndStationId()) : DataRepository.getInstance().getStationById(r.getStartStationId());
                new ReceiptDialog(this, r, end, r.getDurationMinutes(), r.getTotalCost() + r.getEcoCreditsEarned(), r.getEcoCreditsEarned(), r.getTotalCost(), r.getEcoCreditsEarned(), r.getCo2SavedKg()).setVisible(true);
                return;
            }
        }
    }

    public void updateDashboard() {
        refreshAllData();
    }
}
