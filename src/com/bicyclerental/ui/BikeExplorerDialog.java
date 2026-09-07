package com.bicyclerental.ui;

import com.bicyclerental.model.Bicycle;
import com.bicyclerental.model.Rental;
import com.bicyclerental.model.Station;
import com.bicyclerental.service.DataRepository;
import com.bicyclerental.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * BikeExplorerDialog: Interactive station explorer and bicycle checkout form.
 */
public class BikeExplorerDialog extends JDialog {

    private final CustomerDashboard parentDashboard;
    private JComboBox<Station> comboStation;
    private JComboBox<String> comboType;
    private JTable bikeTable;
    private DefaultTableModel tableModel;
    private List<Bicycle> currentBikeList;

    public BikeExplorerDialog(CustomerDashboard parent) {
        super(parent, "EcoRide - Station & Fleet Explorer", true);
        this.parentDashboard = parent;
        setSize(850, 560);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout());

        initUI();
        loadBikes();
    }

    private void initUI() {
        // Filter Toolbar Top
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 16));
        filterBar.setBackground(UITheme.BG_CARD);
        filterBar.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel lblStation = new JLabel("Select Station:");
        lblStation.setFont(UITheme.BODY_BOLD);
        lblStation.setForeground(UITheme.TEXT_LIGHT);
        filterBar.add(lblStation);

        comboStation = new JComboBox<>();
        for (Station s : DataRepository.getInstance().getAllStations()) {
            comboStation.addItem(s);
        }
        comboStation.setFont(UITheme.BODY_FONT);
        comboStation.setBackground(UITheme.BG_INPUT);
        comboStation.setForeground(UITheme.TEXT_LIGHT);
        comboStation.addActionListener(e -> loadBikes());
        filterBar.add(comboStation);

        JLabel lblType = new JLabel("Type Filter:");
        lblType.setFont(UITheme.BODY_BOLD);
        lblType.setForeground(UITheme.TEXT_LIGHT);
        filterBar.add(lblType);

        comboType = new JComboBox<>(new String[]{"ALL", "STANDARD", "GEARED", "ELECTRIC"});
        comboType.setFont(UITheme.BODY_FONT);
        comboType.setBackground(UITheme.BG_INPUT);
        comboType.setForeground(UITheme.TEXT_LIGHT);
        comboType.addActionListener(e -> loadBikes());
        filterBar.add(comboType);

        add(filterBar, BorderLayout.NORTH);

        // Center Table
        String[] columns = {"ID", "Bike Code", "Model", "Type", "Hourly Rate", "Battery / Condition", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bikeTable = new JTable(tableModel);
        bikeTable.setFont(UITheme.BODY_FONT);
        bikeTable.setRowHeight(32);
        bikeTable.setBackground(UITheme.BG_CARD);
        bikeTable.setForeground(UITheme.TEXT_LIGHT);
        bikeTable.setSelectionBackground(UITheme.PRIMARY_DARK);
        bikeTable.setSelectionForeground(Color.WHITE);
        bikeTable.getTableHeader().setBackground(UITheme.BG_DARK);
        bikeTable.getTableHeader().setForeground(UITheme.TEXT_MUTED);
        bikeTable.getTableHeader().setFont(UITheme.BODY_BOLD);

        // Center align table contents
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < bikeTable.getColumnCount(); i++) {
            bikeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(bikeTable);
        scrollPane.getViewport().setBackground(UITheme.BG_DARK);
        scrollPane.setBorder(new EmptyBorder(10, 16, 10, 16));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Action Bar
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 16));
        footer.setBackground(UITheme.BG_DARK);

        JButton btnCancel = UITheme.createSecondaryButton("Close");
        btnCancel.addActionListener(e -> dispose());

        JButton btnUnlock = UITheme.createPrimaryButton("Confirm Reservation & Get Unlock OTP");
        btnUnlock.addActionListener(e -> handleUnlock());

        footer.add(btnCancel);
        footer.add(btnUnlock);
        add(footer, BorderLayout.SOUTH);
    }

    private void loadBikes() {
        tableModel.setRowCount(0);
        Station selectedStation = (Station) comboStation.getSelectedItem();
        if (selectedStation == null) return;

        String selectedType = (String) comboType.getSelectedItem();
        currentBikeList = DataRepository.getInstance().getBicyclesForStation(selectedStation.getStationId(), selectedType);

        for (Bicycle b : currentBikeList) {
            String batteryDisplay = b.isElectric() ? b.getBatteryLevel() + "% Charge" : "100% Mechanical";
            tableModel.addRow(new Object[]{
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

    private void handleUnlock() {
        int selectedRow = bikeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an available bicycle from the table first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bikeId = (int) tableModel.getValueAt(selectedRow, 0);
        Bicycle bike = DataRepository.getInstance().getBicycleById(bikeId);
        Station station = (Station) comboStation.getSelectedItem();

        if (bike == null || station == null) return;

        if (!"AVAILABLE".equalsIgnoreCase(bike.getStatus())) {
            JOptionPane.showMessageDialog(this, "Selected bicycle is currently " + bike.getStatus() + ". Please choose an available bicycle.", "Unavailable", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (DataRepository.getInstance().getCurrentUser().getWalletBalance() < 50.00) {
            JOptionPane.showMessageDialog(this, "Minimum wallet balance of INR 50.00 is required to initiate a ride. Please top up your wallet.", "Insufficient Funds", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Start Rental & Generate OTP
        Rental rental = DataRepository.getInstance().startRental(bike, station);

        // Show unlock confirmation dialog
        String message = "========================================\n" +
                         "       BIKE RESERVATION CONFIRMED!\n" +
                         "========================================\n\n" +
                         "Bicycle Code: " + bike.getBikeCode() + " (" + bike.getModelName() + ")\n" +
                         "Station: " + station.getStationName() + "\n" +
                         "Base Fare: INR 10.00 (Includes first 15 mins)\n" +
                         "Rate: INR " + bike.getHourlyRate() + "/hr\n\n" +
                         "ONE-TIME SMART UNLOCK PIN: " + rental.getUnlockPin() + "\n\n" +
                         "Enter this PIN on the dock keypad to release the bicycle.\n" +
                         "Have a safe and green ride!";

        JOptionPane.showMessageDialog(this, message, "Unlock Passcode Generated", JOptionPane.INFORMATION_MESSAGE);

        parentDashboard.updateDashboard();
        dispose();

        // Launch Active Ride Frame
        new ActiveRideFrame(parentDashboard, rental).setVisible(true);
    }
}
