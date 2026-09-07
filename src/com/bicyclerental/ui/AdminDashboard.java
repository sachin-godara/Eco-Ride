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
 * AdminDashboard: Operations & Fleet Management Console.
 * Demonstrates station capacity rebalancing monitor, fleet status, and CRUD controls.
 */
public class AdminDashboard extends JFrame {

    private final User adminUser;
    private JTable stationTable;
    private DefaultTableModel stationModel;
    private JTable bikeTable;
    private DefaultTableModel bikeModel;

    public AdminDashboard(User adminUser) {
        this.adminUser = adminUser;
        setTitle("EcoRide - Enterprise Fleet & Operations Console | " + adminUser.getFullName());
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout());

        initUI();
        loadData();
    }

    private void initUI() {
        // Top Nav
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UITheme.BG_CARD);
        topBar.setBorder(new EmptyBorder(16, 24, 16, 24));

        JLabel lblTitle = new JLabel("EcoRide Fleet & Station Operations");
        lblTitle.setFont(UITheme.TITLE_FONT);
        lblTitle.setForeground(UITheme.PRIMARY);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        rightPanel.setOpaque(false);
        JLabel lblAdmin = new JLabel("Role: " + adminUser.getRole() + " (" + adminUser.getFullName() + ")");
        lblAdmin.setFont(UITheme.BODY_BOLD);
        lblAdmin.setForeground(UITheme.TEXT_LIGHT);

        JButton btnLogout = UITheme.createSecondaryButton("Log Out");
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        rightPanel.add(lblAdmin);
        rightPanel.add(btnLogout);

        topBar.add(lblTitle, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // Center Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UITheme.BODY_BOLD);
        tabbedPane.setBackground(UITheme.BG_CARD);
        tabbedPane.setForeground(UITheme.TEXT_LIGHT);

        // TAB 1: Station Dock Rebalancing (USP Focus)
        JPanel rebalancePanel = createStationRebalancePanel();
        tabbedPane.addTab("[1] Station Rebalancing Intelligence (USP)", rebalancePanel);

        // TAB 2: Fleet Management (CRUD)
        JPanel fleetPanel = createFleetManagementPanel();
        tabbedPane.addTab("[2] Fleet Inventory & Maintenance", fleetPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createStationRebalancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG_DARK);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Subheader banner
        JPanel banner = UITheme.createCardPanel();
        banner.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel lblNotice = new JLabel("Dynamic Dock Saturation Monitor: Stations with >80% bikes trigger rebalance work-orders to prevent return lockout.");
        lblNotice.setFont(UITheme.BODY_FONT);
        lblNotice.setForeground(UITheme.ACCENT_AMBER);
        banner.add(lblNotice);
        panel.add(banner, BorderLayout.NORTH);

        String[] cols = {"Station ID", "Station Name", "Location", "Total Docks", "Available Bikes", "Empty Docks", "Occupancy %", "Saturation Alert"};
        stationModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        stationTable = new JTable(stationModel);
        setupStyledTable(stationTable);

        panel.add(new JScrollPane(stationTable), BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(UITheme.BG_DARK);
        JButton btnDispatch = UITheme.createPrimaryButton("Dispatch Rebalancing Van to Congested Docks");
        btnDispatch.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Rebalancing work-order dispatched to field operator Rajesh Sharma.", "Dispatch Alert", JOptionPane.INFORMATION_MESSAGE);
        });
        footer.add(btnDispatch);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createFleetManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG_DARK);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        String[] cols = {"Bike ID", "Code", "Model", "Type", "Station ID", "Hourly Rate", "Battery", "Operational Status"};
        bikeModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        bikeTable = new JTable(bikeModel);
        setupStyledTable(bikeTable);

        panel.add(new JScrollPane(bikeTable), BorderLayout.CENTER);

        // CRUD Action Buttons
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        actions.setBackground(UITheme.BG_DARK);

        JButton btnAdd = UITheme.createPrimaryButton("+ Add New Bicycle");
        btnAdd.addActionListener(e -> addNewBicycleDialog());

        JButton btnToggleMaint = UITheme.createSecondaryButton("🔧 Toggle Maintenance Flag");
        btnToggleMaint.addActionListener(e -> toggleMaintenanceStatus());

        actions.add(btnAdd);
        actions.add(btnToggleMaint);
        panel.add(actions, BorderLayout.SOUTH);

        return panel;
    }

    private void setupStyledTable(JTable table) {
        table.setFont(UITheme.BODY_FONT);
        table.setRowHeight(30);
        table.setBackground(UITheme.BG_CARD);
        table.setForeground(UITheme.TEXT_LIGHT);
        table.setSelectionBackground(UITheme.PRIMARY_DARK);
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(UITheme.BG_DARK);
        table.getTableHeader().setForeground(UITheme.TEXT_MUTED);
        table.getTableHeader().setFont(UITheme.BODY_BOLD);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }
    }

    private void loadData() {
        // Load Stations
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

        // Load Bicycles
        bikeModel.setRowCount(0);
        for (Bicycle b : DataRepository.getInstance().getAllBicycles()) {
            bikeModel.addRow(new Object[]{
                    b.getBikeId(),
                    b.getBikeCode(),
                    b.getModelName(),
                    b.getBikeType(),
                    b.getCurrentStationId(),
                    "₹ " + String.format("%.2f", b.getHourlyRate()),
                    b.isElectric() ? b.getBatteryLevel() + "%" : "N/A",
                    b.getStatus()
            });
        }
    }

    private void addNewBicycleDialog() {
        JTextField txtCode = UITheme.createStyledTextField(10);
        txtCode.setText("BK-" + (110 + DataRepository.getInstance().getAllBicycles().size()));
        JTextField txtModel = UITheme.createStyledTextField(10);
        txtModel.setText("Firefox Raptor");
        JComboBox<String> cmbType = new JComboBox<>(new String[]{"STANDARD", "GEARED", "ELECTRIC"});
        JTextField txtRate = UITheme.createStyledTextField(10);
        txtRate.setText("30.00");

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.add(new JLabel("Bike Code:"));
        form.add(txtCode);
        form.add(new JLabel("Model Name:"));
        form.add(txtModel);
        form.add(new JLabel("Type:"));
        form.add(cmbType);
        form.add(new JLabel("Hourly Rate (₹):"));
        form.add(txtRate);

        int res = JOptionPane.showConfirmDialog(this, form, "Add New Fleet Bicycle", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                double rate = Double.parseDouble(txtRate.getText().trim());
                int nextId = DataRepository.getInstance().getAllBicycles().size() + 101;
                Bicycle b = new Bicycle(nextId, txtCode.getText().trim(), txtModel.getText().trim(),
                        (String) cmbType.getSelectedItem(), 1, "AVAILABLE", rate, 100);
                DataRepository.getInstance().getAllBicycles().add(b);
                loadData();
                JOptionPane.showMessageDialog(this, "Bicycle added to fleet successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid hourly rate number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void toggleMaintenanceStatus() {
        int row = bikeTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bicycle from the table.", "Selection Required", JOptionPane.WARNING_MESSAGE);
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
            loadData();
        }
    }
}
