package com.bicyclerental.ui;

import com.bicyclerental.model.Rental;
import com.bicyclerental.model.User;
import com.bicyclerental.service.DataRepository;
import com.bicyclerental.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * CustomerDashboard: Main user interface for riders.
 * Features eco-metrics, wallet overview, active ride indicators, and quick booking.
 */
public class CustomerDashboard extends JFrame {

    private final User user;
    private JLabel lblWallet;
    private JLabel lblCredits;
    private JLabel lblActiveStatus;
    private JButton btnViewActiveRide;
    private JButton btnRentBike;

    public CustomerDashboard(User user) {
        this.user = user;
        setTitle("EcoRide - Customer Dashboard | " + user.getFullName());
        setSize(920, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout());

        initUI();
    }

    private void initUI() {
        // 1. Navigation Top Bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UITheme.BG_CARD);
        topBar.setBorder(new EmptyBorder(16, 24, 16, 24));

        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brandPanel.setOpaque(false);
        JLabel lblLogo = new JLabel("EcoRide Portal");
        lblLogo.setFont(UITheme.TITLE_FONT);
        lblLogo.setForeground(UITheme.PRIMARY);
        brandPanel.add(lblLogo);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userPanel.setOpaque(false);
        JLabel lblUser = new JLabel("Welcome, " + user.getFullName());
        lblUser.setFont(UITheme.BODY_BOLD);
        lblUser.setForeground(UITheme.TEXT_LIGHT);

        JButton btnLogout = UITheme.createSecondaryButton("Sign Out");
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        userPanel.add(lblUser);
        userPanel.add(btnLogout);

        topBar.add(brandPanel, BorderLayout.WEST);
        topBar.add(userPanel, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // 2. Main Content Container
        JPanel mainContent = new JPanel();
        mainContent.setBackground(UITheme.BG_DARK);
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Metric Tiles Row
        JPanel metricsRow = new JPanel(new GridLayout(1, 4, 16, 0));
        metricsRow.setOpaque(false);
        metricsRow.setMaximumSize(new Dimension(1200, 110));

        lblWallet = new JLabel("INR " + String.format("%.2f", user.getWalletBalance()));
        metricsRow.add(createMetricCard("Wallet Balance", lblWallet, UITheme.PRIMARY));

        lblCredits = new JLabel(user.getEcoCredits() + " pts");
        metricsRow.add(createMetricCard("Eco-Credits (USP)", lblCredits, UITheme.ACCENT_AMBER));

        JLabel lblCo2 = new JLabel("12.6 kg");
        metricsRow.add(createMetricCard("CO2 Prevented", lblCo2, UITheme.ACCENT_BLUE));

        lblActiveStatus = new JLabel(DataRepository.getInstance().getActiveRental() != null ? "1 RIDE ACTIVE" : "IDLE");
        metricsRow.add(createMetricCard("Ride Status", lblActiveStatus, UITheme.TEXT_ACCENT));

        mainContent.add(metricsRow);
        mainContent.add(Box.createVerticalStrut(24));

        // Center Action Cards
        JPanel centerGrid = new JPanel(new GridLayout(1, 2, 20, 0));
        centerGrid.setOpaque(false);

        // Card Left: Primary Actions
        JPanel actionCard = UITheme.createCardPanel();
        actionCard.setLayout(new BoxLayout(actionCard, BoxLayout.Y_AXIS));

        JLabel lblActions = new JLabel("Rider Operations");
        lblActions.setFont(UITheme.SUBTITLE_FONT);
        lblActions.setForeground(UITheme.TEXT_LIGHT);
        actionCard.add(lblActions);
        actionCard.add(Box.createVerticalStrut(15));

        btnRentBike = UITheme.createPrimaryButton("Browse Stations & Unlock Bicycle");
        btnRentBike.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRentBike.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnRentBike.addActionListener(e -> openBikeCatalog());
        actionCard.add(btnRentBike);
        actionCard.add(Box.createVerticalStrut(12));

        btnViewActiveRide = UITheme.createSecondaryButton("View Active Trip Monitor");
        btnViewActiveRide.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnViewActiveRide.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnViewActiveRide.addActionListener(e -> openActiveRide());
        actionCard.add(btnViewActiveRide);
        actionCard.add(Box.createVerticalStrut(12));

        JButton btnAddMoney = UITheme.createSecondaryButton("Top-up Digital Wallet (+INR 200)");
        btnAddMoney.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAddMoney.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnAddMoney.addActionListener(e -> {
            user.setWalletBalance(user.getWalletBalance() + 200.00);
            updateDashboard();
            JOptionPane.showMessageDialog(this, "INR 200 credited to wallet successfully!", "Wallet Top-up", JOptionPane.INFORMATION_MESSAGE);
        });
        actionCard.add(btnAddMoney);

        centerGrid.add(actionCard);

        // Card Right: Unique Selling Proposition (USP) Showcase
        JPanel uspCard = UITheme.createCardPanel();
        uspCard.setLayout(new BoxLayout(uspCard, BoxLayout.Y_AXIS));

        JLabel lblUspTitle = new JLabel("EcoRide Green Mobility USP");
        lblUspTitle.setFont(UITheme.SUBTITLE_FONT);
        lblUspTitle.setForeground(UITheme.PRIMARY);
        uspCard.add(lblUspTitle);
        uspCard.add(Box.createVerticalStrut(10));

        JTextArea txtUsp = new JTextArea(
                "* Dynamic Dock Rebalancing:\n" +
                "  Real-time tracking of station saturation ensures riders always\n" +
                "  find empty return docks and prevents station starvation.\n\n" +
                "* Carbon Offset Engine:\n" +
                "  Earn 1 Eco-Credit for every 0.5 kg of CO2 emissions prevented\n" +
                "  compared to fuel-based vehicles (0.21 kg CO2 / km).\n\n" +
                "* In-App Redemption:\n" +
                "  Eco-credits automatically discount your final checkout fare!"
        );
        txtUsp.setEditable(false);
        txtUsp.setFont(UITheme.BODY_FONT);
        txtUsp.setForeground(UITheme.TEXT_MUTED);
        txtUsp.setBackground(UITheme.BG_CARD);
        txtUsp.setLineWrap(true);
        txtUsp.setWrapStyleWord(true);
        uspCard.add(txtUsp);

        centerGrid.add(uspCard);
        mainContent.add(centerGrid);

        add(mainContent, BorderLayout.CENTER);
        updateDashboard();
    }

    public void updateDashboard() {
        lblWallet.setText("₹ " + String.format("%.2f", user.getWalletBalance()));
        lblCredits.setText(user.getEcoCredits() + " pts");
        Rental active = DataRepository.getInstance().getActiveRental();
        if (active != null) {
            lblActiveStatus.setText("1 RIDE ACTIVE");
            lblActiveStatus.setForeground(UITheme.ACCENT_AMBER);
            btnRentBike.setEnabled(false);
            btnViewActiveRide.setEnabled(true);
        } else {
            lblActiveStatus.setText("NO ACTIVE RIDE");
            lblActiveStatus.setForeground(UITheme.PRIMARY);
            btnRentBike.setEnabled(true);
            btnViewActiveRide.setEnabled(false);
        }
    }

    private JPanel createMetricCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = UITheme.createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(UITheme.TEXT_MUTED);

        valueLabel.setFont(UITheme.METRIC_FONT);
        valueLabel.setForeground(accentColor);

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(6));
        card.add(valueLabel);
        return card;
    }

    private void openBikeCatalog() {
        new BikeExplorerDialog(this).setVisible(true);
    }

    private void openActiveRide() {
        Rental active = DataRepository.getInstance().getActiveRental();
        if (active != null) {
            new ActiveRideFrame(this, active).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "No active ride currently in progress.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
