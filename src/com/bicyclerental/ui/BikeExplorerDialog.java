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
        lblStation.setForeground(Color.BLACK);
        filterBar.add(lblStation);

        comboStation = new JComboBox<>();
        for (Station s : DataRepository.getInstance().getAllStations()) {
            comboStation.addItem(s);
        }
        comboStation.setFont(UITheme.BODY_BOLD);
        comboStation.setBackground(UITheme.BG_INPUT);
        comboStation.setForeground(Color.BLACK);
        comboStation.addActionListener(e -> loadBikes());
        filterBar.add(comboStation);

        JLabel lblType = new JLabel("Type Filter:");
        lblType.setFont(UITheme.BODY_BOLD);
        lblType.setForeground(Color.BLACK);
        filterBar.add(lblType);

        comboType = new JComboBox<>(new String[]{"ALL", "STANDARD", "GEARED", "ELECTRIC"});
        comboType.setFont(UITheme.BODY_BOLD);
        comboType.setBackground(UITheme.BG_INPUT);
        comboType.setForeground(Color.BLACK);
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
        bikeTable.setFont(UITheme.BODY_BOLD);
        bikeTable.setRowHeight(36);
        bikeTable.setBackground(UITheme.BG_CARD);
        bikeTable.setForeground(Color.BLACK);
        bikeTable.setSelectionBackground(new Color(209, 250, 229));
        bikeTable.setSelectionForeground(Color.BLACK);
        bikeTable.getTableHeader().setBackground(new Color(241, 245, 249));
        bikeTable.getTableHeader().setForeground(Color.BLACK);
        bikeTable.getTableHeader().setFont(UITheme.BODY_BOLD);

        // Center align table contents with high-contrast text
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                if (!isSelected) {
                    setForeground(Color.BLACK);
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                }
                return c;
            }
        };

        for (int i = 0; i < bikeTable.getColumnCount(); i++) {
            if (i == 6) { // Status column
                bikeTable.getColumnModel().getColumn(i).setCellRenderer(UITheme.createStatusCellRenderer());
            } else {
                bikeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        JScrollPane scrollPane = new JScrollPane(bikeTable);
        scrollPane.getViewport().setBackground(UITheme.BG_CARD);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 16, 10, 16),
                BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1, true)
        ));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Action Bar
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 16));
        footer.setBackground(UITheme.BG_DARK);

        JButton btnCancel = UITheme.createSecondaryButton("Close");
        btnCancel.addActionListener(e -> dispose());

        JButton btnUnlock = UITheme.createPrimaryButton("Unlock & Start Rental");
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
            UITheme.showStyledMessage(this, "Please select an available bicycle from the table first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bikeId = (int) tableModel.getValueAt(selectedRow, 0);
        Bicycle bike = DataRepository.getInstance().getBicycleById(bikeId);
        Station station = (Station) comboStation.getSelectedItem();

        if (bike == null || station == null) return;

        if (!"AVAILABLE".equalsIgnoreCase(bike.getStatus())) {
            UITheme.showStyledMessage(this, "Selected bicycle is currently " + bike.getStatus() + ". Please choose an available bicycle.", "Unavailable", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (DataRepository.getInstance().getCurrentUser().getWalletBalance() < 50.00) {
            UITheme.showStyledMessage(this, "Minimum wallet balance of INR 50.00 is required to initiate a ride. Please top up your wallet.", "Insufficient Funds", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Start Rental & Generate OTP
        Rental rental = DataRepository.getInstance().startRental(bike, station);

        // Show unlock confirmation dialog
        String message = "BIKE UNLOCK CONFIRMED\n\n" +
                         "Bicycle: " + bike.getBikeCode() + " (" + bike.getModelName() + ")\n" +
                         "Pickup Station: " + station.getStationName() + "\n" +
                         "Base Rate: INR 10.00 (Includes initial 15 mins)\n" +
                         "Hourly Rate: INR " + String.format("%.2f", bike.getHourlyRate()) + "/hr\n\n" +
                         "Unlock Passcode: " + rental.getUnlockPin() + "\n\n" +
                         "Enter this PIN on the dock terminal to release the lock.\n" +
                         "Enjoy your green ride!";

        UITheme.showStyledMessage(this, message, "Unlock Passcode Generated", JOptionPane.INFORMATION_MESSAGE);

        dispose();
        parentDashboard.startActiveRide(rental);
    }
}
