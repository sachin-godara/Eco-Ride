package com.bicyclerental.ui;

import com.bicyclerental.model.Bicycle;
import com.bicyclerental.model.Station;
import com.bicyclerental.model.User;
import com.bicyclerental.service.DataRepository;
import com.bicyclerental.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * AdminDashboard: Enterprise Operations & Fleet Console.
 * Demonstrates station capacity rebalancing monitor, fleet status, full CRUD, and maintenance logs.
 */
public class AdminDashboard extends JFrame {

    private final User adminUser;
    private JLabel lblTotalRevenue;
    private JLabel lblTotalTrips;
    private JLabel lblTotalFleet;
    private JLabel lblCampusCo2;

    private JTable stationTable;
    private DefaultTableModel stationModel;
    private JTable bikeTable;
    private DefaultTableModel bikeModel;
    private JList<String> maintenanceList;
    private DefaultListModel<String> maintenanceListModel;

    public AdminDashboard(User adminUser) {
        this.adminUser = adminUser;
        setTitle("EcoRide - Enterprise Fleet & Operations Console | " + adminUser.getFullName());
        setSize(1060, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout());

        initUI();
        refreshAllData();
    }

    private void initUI() {
        // Top Nav
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UITheme.BG_CARD);
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER_COLOR),
                new EmptyBorder(14, 24, 14, 24)
        ));

        JLabel lblTitle = new JLabel("EcoRide Fleet & Operations Console");
        lblTitle.setFont(UITheme.TITLE_FONT);
        lblTitle.setForeground(Color.BLACK);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        rightPanel.setOpaque(false);
        JLabel lblAdmin = new JLabel("Administrator: " + adminUser.getFullName());
        lblAdmin.setFont(UITheme.BODY_BOLD);
        lblAdmin.setForeground(UITheme.TEXT_LIGHT);

        JButton btnLogout = UITheme.createSecondaryButton("Sign Out");
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        rightPanel.add(lblAdmin);
        rightPanel.add(btnLogout);

        topBar.add(lblTitle, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // Center Content
        JPanel center = new JPanel();
        center.setBackground(UITheme.BG_DARK);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(new EmptyBorder(16, 20, 16, 20));

        // Metric Row
        JPanel metricRow = new JPanel(new GridLayout(1, 4, 14, 0));
        metricRow.setOpaque(false);
        metricRow.setMaximumSize(new Dimension(1400, 95));

        lblTotalRevenue = new JLabel("INR 0.00");
        metricRow.add(createMetricTile("Campus Revenue", lblTotalRevenue, Color.BLACK));

        lblTotalTrips = new JLabel("0 Completed");
        metricRow.add(createMetricTile("Total Completed Trips", lblTotalTrips, UITheme.ACCENT_BLUE));

        lblTotalFleet = new JLabel("0 Bikes");
        metricRow.add(createMetricTile("Active Fleet Size", lblTotalFleet, UITheme.ACCENT_AMBER));

        lblCampusCo2 = new JLabel("0.00 kg");
        metricRow.add(createMetricTile("Campus CO2 Prevented", lblCampusCo2, Color.BLACK));

        center.add(metricRow);
        center.add(Box.createVerticalStrut(14));

        // Tabbed Pane
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UITheme.BODY_BOLD);
        tabs.setBackground(UITheme.BG_CARD);
        tabs.setForeground(UITheme.TEXT_LIGHT);

        // TAB 1: Station Rebalancing Intelligence
        tabs.addTab("Station Capacity & Fleet Rebalancing", createStationPanel());

        // TAB 2: Fleet Inventory Management
        tabs.addTab("Fleet Inventory & Asset Management", createFleetPanel());

        // TAB 3: Maintenance Logs
        tabs.addTab("Maintenance & Defect Logs", createMaintenancePanel());

        center.add(tabs);
        add(center, BorderLayout.CENTER);
    }

    private JPanel createMetricTile(String title, JLabel val, Color col) {
        JPanel p = UITheme.createCardPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.setForeground(Color.BLACK);

        val.setFont(UITheme.METRIC_FONT);
        val.setForeground(col);

        p.add(t);
        p.add(Box.createVerticalStrut(4));
        p.add(val);
        return p;
    }

    private JPanel createStationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel banner = UITheme.createCardPanel();
        banner.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 4));
        JLabel lblNotice = new JLabel("Dynamic Dock Saturation Telemetry: Stations exceeding 80% saturation dispatch rebalancing vans.");
        lblNotice.setFont(UITheme.BODY_BOLD);
        lblNotice.setForeground(Color.BLACK);
        banner.add(lblNotice);
        panel.add(banner, BorderLayout.NORTH);

        String[] cols = {"Station ID", "Station Name", "Location", "Total Docks", "Available Bikes", "Empty Docks", "Occupancy %", "Saturation Alert"};
        stationModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        stationTable = new JTable(stationModel);
        setupTable(stationTable);
        panel.add(new JScrollPane(stationTable), BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footer.setBackground(UITheme.BG_DARK);

        JButton btnDispatch = UITheme.createPrimaryButton("Dispatch Fleet Rebalancing Van to Sitapura Hub");
        btnDispatch.addActionListener(e -> {
            UITheme.showStyledMessage(this, "Rebalancing work-order dispatched to field operator Rajesh Sharma.\nAssigned: Transfer 10 bicycles from Metro Hub to Central Campus.", "Dispatch Order Generated", JOptionPane.INFORMATION_MESSAGE);
        });
        footer.add(btnDispatch);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createFleetPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        String[] cols = {"Bike ID", "Code", "Model", "Category", "Station", "Hourly Rate", "Battery", "Operational Status"};
        bikeModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        bikeTable = new JTable(bikeModel);
        setupTable(bikeTable);
        panel.add(new JScrollPane(bikeTable), BorderLayout.CENTER);

        // CRUD Action Buttons
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        actions.setBackground(UITheme.BG_DARK);

        JButton btnAdd = UITheme.createPrimaryButton("+ Add New Bicycle");
        btnAdd.addActionListener(e -> addNewBicycleDialog());

        JButton btnEdit = UITheme.createSecondaryButton("Edit Hourly Rate");
        btnEdit.addActionListener(e -> editBicycleDialog());

        JButton btnToggleMaint = UITheme.createSecondaryButton("Toggle Maintenance Status");
        btnToggleMaint.addActionListener(e -> toggleMaintenanceStatus());

        JButton btnDelete = UITheme.createSecondaryButton("Decommission Bicycle");
        btnDelete.addActionListener(e -> deleteBicycle());

        actions.add(btnAdd);
        actions.add(btnEdit);
        actions.add(btnToggleMaint);
        actions.add(btnDelete);
        panel.add(actions, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMaintenancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        maintenanceListModel = new DefaultListModel<>();
        maintenanceList = new JList<>(maintenanceListModel);
        maintenanceList.setFont(new Font("Consolas", Font.PLAIN, 13));
        maintenanceList.setBackground(UITheme.BG_CARD);
        maintenanceList.setForeground(UITheme.TEXT_LIGHT);
        panel.add(new JScrollPane(maintenanceList), BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footer.setBackground(UITheme.BG_DARK);
        JButton btnClear = UITheme.createSecondaryButton("Mark Issue Resolved");
        btnClear.addActionListener(e -> {
            int idx = maintenanceList.getSelectedIndex();
            if (idx != -1) {
                maintenanceListModel.remove(idx);
                UITheme.showStyledMessage(this, "Ticket marked RESOLVED and archived.", "Resolved", JOptionPane.INFORMATION_MESSAGE);
            } else {
                UITheme.showStyledMessage(this, "Please select an issue log first.", "Select Log", JOptionPane.WARNING_MESSAGE);
            }
        });
        footer.add(btnClear);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    private void setupTable(JTable table) {
        table.setFont(UITheme.BODY_BOLD);
        table.setRowHeight(36);
        table.setBackground(UITheme.BG_CARD);
        table.setForeground(Color.BLACK);
        table.setSelectionBackground(UITheme.PRIMARY_DARK);
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(new Color(241, 245, 249));
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setFont(UITheme.BODY_BOLD);

        DefaultTableCellRenderer renderer = UITheme.createStatusCellRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private void refreshAllData() {
        lblTotalRevenue.setText("INR " + String.format("%.2f", DataRepository.getInstance().getTotalRevenue()));
        lblTotalTrips.setText(DataRepository.getInstance().getUserRentalHistory(2).size() + " Trips");
        lblTotalFleet.setText(DataRepository.getInstance().getAllBicycles().size() + " Bikes");
        lblCampusCo2.setText(String.format("%.2f kg", DataRepository.getInstance().getTotalCo2Saved()));

        // Stations
        stationModel.setRowCount(0);
        for (Station s : DataRepository.getInstance().getAllStations()) {
            stationModel.addRow(new Object[]{
                    s.getStationId(),
                    s.getStationName(),
                    s.getLocationAddress(),
                    s.getTotalDocks(),
                    s.getAvailableBikes(),
                    s.getEmptyDocks(),
                    String.format("%.1f%%", s.getOccupancyRate()),
                    s.getCapacityStatus()
            });
        }

        // Bicycles
        bikeModel.setRowCount(0);
        for (Bicycle b : DataRepository.getInstance().getAllBicycles()) {
            Station s = DataRepository.getInstance().getStationById(b.getCurrentStationId());
            bikeModel.addRow(new Object[]{
                    b.getBikeId(),
                    b.getBikeCode(),
                    b.getModelName(),
                    b.getBikeType(),
                    s != null ? s.getStationName() : "In Transit",
                    "INR " + String.format("%.2f", b.getHourlyRate()),
                    b.isElectric() ? b.getBatteryLevel() + "%" : "N/A",
                    b.getStatus()
            });
        }

        // Maintenance Logs
        maintenanceListModel.clear();
        for (String m : DataRepository.getInstance().getMaintenanceReports()) {
            maintenanceListModel.addElement(m);
        }
        if (maintenanceListModel.isEmpty()) {
            maintenanceListModel.addElement("[AUDIT] No open critical defect tickets. Fleet operating at 95% availability.");
        }
    }

    private void addNewBicycleDialog() {
        JTextField txtCode = UITheme.createStyledTextField(10);
        txtCode.setText("BK-" + (110 + DataRepository.getInstance().getAllBicycles().size()));
        JTextField txtModel = UITheme.createStyledTextField(10);
        txtModel.setText("Firefox Raptor 29");
        JComboBox<String> cmbType = new JComboBox<>(new String[]{"STANDARD", "GEARED", "ELECTRIC"});
        JTextField txtRate = UITheme.createStyledTextField(10);
        txtRate.setText("35.00");

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setBackground(Color.WHITE);

        JLabel lblCode = new JLabel("Bike Code:");
        lblCode.setFont(UITheme.BODY_BOLD);
        lblCode.setForeground(UITheme.TEXT_LIGHT);
        form.add(lblCode);
        form.add(txtCode);

        JLabel lblModel = new JLabel("Model Name:");
        lblModel.setFont(UITheme.BODY_BOLD);
        lblModel.setForeground(UITheme.TEXT_LIGHT);
        form.add(lblModel);
        form.add(txtModel);

        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setFont(UITheme.BODY_BOLD);
        lblCategory.setForeground(UITheme.TEXT_LIGHT);
        form.add(lblCategory);
        form.add(cmbType);

        JLabel lblRate = new JLabel("Hourly Rate (INR):");
        lblRate.setFont(UITheme.BODY_BOLD);
        lblRate.setForeground(UITheme.TEXT_LIGHT);
        form.add(lblRate);
        form.add(txtRate);

        int res = JOptionPane.showConfirmDialog(this, form, "Add New Bicycle to Fleet", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                double rate = Double.parseDouble(txtRate.getText().trim());
                int nextId = DataRepository.getInstance().getAllBicycles().size() + 101;
                Bicycle b = new Bicycle(nextId, txtCode.getText().trim(), txtModel.getText().trim(),
                        (String) cmbType.getSelectedItem(), 1, "AVAILABLE", rate, 100);
                DataRepository.getInstance().addBicycle(b);
                refreshAllData();
                UITheme.showStyledMessage(this, "Bicycle added to fleet successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                UITheme.showStyledMessage(this, "Invalid hourly rate.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editBicycleDialog() {
        int row = bikeTable.getSelectedRow();
        if (row == -1) {
            UITheme.showStyledMessage(this, "Please select a bicycle from the table.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int bikeId = (int) bikeModel.getValueAt(row, 0);
        Bicycle b = DataRepository.getInstance().getBicycleById(bikeId);
        if (b != null) {
            String input = JOptionPane.showInputDialog(this, "Enter new hourly rate for " + b.getBikeCode() + ":", b.getHourlyRate());
            if (input != null && !input.trim().isEmpty()) {
                try {
                    b.setHourlyRate(Double.parseDouble(input.trim()));
                    refreshAllData();
                    UITheme.showStyledMessage(this, "Hourly rate updated.", "Updated", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    UITheme.showStyledMessage(this, "Invalid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void toggleMaintenanceStatus() {
        int row = bikeTable.getSelectedRow();
        if (row == -1) {
            UITheme.showStyledMessage(this, "Please select a bicycle from the table.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int bikeId = (int) bikeModel.getValueAt(row, 0);
        Bicycle b = DataRepository.getInstance().getBicycleById(bikeId);
        if (b != null) {
            if ("MAINTENANCE".equalsIgnoreCase(b.getStatus())) {
                b.setStatus("AVAILABLE");
            } else {
                b.setStatus("MAINTENANCE");
            }
            refreshAllData();
        }
    }

    private void deleteBicycle() {
        int row = bikeTable.getSelectedRow();
        if (row == -1) {
            UITheme.showStyledMessage(this, "Please select a bicycle from the table.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int bikeId = (int) bikeModel.getValueAt(row, 0);
        Bicycle b = DataRepository.getInstance().getBicycleById(bikeId);
        if (b != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to decommission " + b.getBikeCode() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                b.setStatus("DECOMMISSIONED");
                refreshAllData();
                UITheme.showStyledMessage(this, "Bicycle decommissioned and removed from active rental pool.", "Decommissioned", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
