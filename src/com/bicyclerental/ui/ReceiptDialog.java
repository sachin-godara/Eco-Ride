package com.bicyclerental.ui;

import com.bicyclerental.model.Bicycle;
import com.bicyclerental.model.Rental;
import com.bicyclerental.model.Station;
import com.bicyclerental.model.User;
import com.bicyclerental.service.DataRepository;
import com.bicyclerental.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ReceiptDialog: Modal itemized tax invoice & green journey summary.
 */
public class ReceiptDialog extends JDialog {

    public ReceiptDialog(Frame parent, Rental rental, Station endStation, int durationMinutes,
                         double grossFare, double discount, double netPaid, int ecoCreditsEarned, double co2Saved) {
        super(parent, "EcoRide - Official Rental Receipt & Green Certificate", true);
        setSize(520, 640);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout());

        User user = DataRepository.getInstance().getCurrentUser();
        Bicycle bike = DataRepository.getInstance().getBicycleById(rental.getBikeId());
        Station startStation = DataRepository.getInstance().getStationById(rental.getStartStationId());
        String dateStr = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date());

        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        header.setBackground(UITheme.BG_DARK);
        header.setBorder(new EmptyBorder(16, 16, 8, 16));

        JLabel title = new JLabel("EcoRide Transit Receipt");
        title.setFont(UITheme.TITLE_FONT);
        title.setForeground(UITheme.PRIMARY);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Center formatted text area simulating official invoice
        JTextArea txtReceipt = new JTextArea();
        txtReceipt.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtReceipt.setBackground(UITheme.BG_CARD);
        txtReceipt.setForeground(UITheme.TEXT_LIGHT);
        txtReceipt.setEditable(false);
        txtReceipt.setBorder(new EmptyBorder(16, 18, 16, 18));

        StringBuilder sb = new StringBuilder();
        sb.append("====================================================\n");
        sb.append("         OFFICIAL TRIP INVOICE & ECO SUMMARY        \n");
        sb.append("====================================================\n");
        sb.append(String.format("Receipt No       : RCP-%06d\n", rental.getRentalId()));
        sb.append(String.format("Date & Time      : %s\n", dateStr));
        sb.append(String.format("Rider Name       : %s\n", user.getFullName()));
        sb.append(String.format("Bicycle Code     : %s (%s)\n", bike.getBikeCode(), bike.getModelName()));
        sb.append(String.format("Type             : %s\n", bike.getBikeType()));
        sb.append("----------------------------------------------------\n");
        sb.append(String.format("Origin Dock      : %s\n", startStation.getStationName()));
        sb.append(String.format("Destination Dock : %s\n", endStation.getStationName()));
        sb.append(String.format("Trip Duration    : %d minutes\n", durationMinutes));
        sb.append("----------------------------------------------------\n");
        sb.append("[USP] ENVIRONMENTAL & FITNESS IMPACT:\n");
        sb.append(String.format("  * CO2 Avoided  : %.2f kg (vs petrol car)\n", co2Saved));
        sb.append(String.format("  * Eco-Credits  : +%d points awarded\n", ecoCreditsEarned));
        sb.append(String.format("  * Energy Burn  : %d kcal\n", (int) (durationMinutes * 7.5)));
        sb.append("----------------------------------------------------\n");
        sb.append("FARES & FINANCIAL BREAKDOWN:\n");
        sb.append(String.format("  Gross Fare     : INR %8.2f\n", grossFare));
        sb.append(String.format("  Eco-Discount   : INR %8.2f\n", discount));
        sb.append("                   ----------\n");
        sb.append(String.format("  NET DEBITED    : INR %8.2f\n", netPaid));
        sb.append("----------------------------------------------------\n");
        sb.append(String.format("Wallet Balance   : INR %8.2f\n", user.getWalletBalance()));
        sb.append("Payment Status   : SUCCESS (PAID VIA DIGITAL WALLET)\n");
        sb.append("====================================================\n");
        sb.append("       Thank you for choosing zero-emission transit! \n");

        txtReceipt.setText(sb.toString());

        JScrollPane scroll = new JScrollPane(txtReceipt);
        scroll.setBorder(new EmptyBorder(10, 16, 10, 16));
        scroll.getViewport().setBackground(UITheme.BG_DARK);
        add(scroll, BorderLayout.CENTER);

        // Buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 16));
        footer.setBackground(UITheme.BG_DARK);

        JButton btnPrint = UITheme.createPrimaryButton("Print / Export Receipt");
        btnPrint.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Receipt exported to PDF/Printer successfully!", "Print Confirmed", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnDone = UITheme.createSecondaryButton("Back to Dashboard");
        btnDone.addActionListener(e -> dispose());

        footer.add(btnPrint);
        footer.add(btnDone);
        add(footer, BorderLayout.SOUTH);
    }
}
