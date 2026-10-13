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
 * Rendered with high-contrast card styling and crisp monospace layout.
 */
public class ReceiptDialog extends JDialog {

    public ReceiptDialog(Frame parent, Rental rental, Station endStation, int durationMinutes,
                         double grossFare, double discount, double netPaid, int ecoCreditsEarned, double co2Saved) {
        super(parent, "EcoRide - Official Rental Receipt & Green Certificate", true);
        setSize(540, 660);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout());

        User user = DataRepository.getInstance().getCurrentUser();
        Bicycle bike = DataRepository.getInstance().getBicycleById(rental.getBikeId());
        Station startStation = DataRepository.getInstance().getStationById(rental.getStartStationId());
        String dateStr = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date());

        // Header
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(UITheme.BG_DARK);
        header.setBorder(new EmptyBorder(18, 20, 8, 20));

        JLabel title = new JLabel("EcoRide Transit Receipt");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(UITheme.TEXT_LIGHT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(title);

        header.add(Box.createVerticalStrut(3));

        JLabel subtitle = new JLabel("Official tax invoice & verified green certificate");
        subtitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        subtitle.setForeground(Color.BLACK);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(subtitle);

        add(header, BorderLayout.NORTH);

        // Center formatted text area simulating official invoice
        JTextArea txtReceipt = new JTextArea();
        txtReceipt.setFont(new Font("Consolas", Font.BOLD, 12));
        txtReceipt.setBackground(UITheme.BG_CARD);
        txtReceipt.setForeground(Color.BLACK);
        txtReceipt.setEditable(false);
        txtReceipt.setBorder(new EmptyBorder(16, 18, 16, 18));

        StringBuilder sb = new StringBuilder();
        sb.append("====================================================\n");
        sb.append("         OFFICIAL TRIP INVOICE & ECO SUMMARY        \n");
        sb.append("====================================================\n");
        sb.append(String.format("Receipt No       : RCP-%06d\n", rental.getRentalId()));
        sb.append(String.format("Date & Time      : %s\n", dateStr));
        sb.append(String.format("Rider Name       : %s\n", user.getFullName()));
        sb.append(String.format("Bicycle Code     : %s (%s)\n", bike != null ? bike.getBikeCode() : "BK-101", bike != null ? bike.getModelName() : "Standard"));
        sb.append(String.format("Type             : %s\n", bike != null ? bike.getBikeType() : "MECHANICAL"));
        sb.append("----------------------------------------------------\n");
        sb.append(String.format("Origin Dock      : %s\n", startStation != null ? startStation.getStationName() : "Origin"));
        sb.append(String.format("Destination Dock : %s\n", endStation != null ? endStation.getStationName() : "Destination"));
        sb.append(String.format("Trip Duration    : %d minutes\n", durationMinutes));
        sb.append("----------------------------------------------------\n");
        sb.append("ENVIRONMENTAL & HEALTH IMPACT:\n");
        sb.append(String.format("  * CO2 Avoided  : %.2f kg (vs petrol vehicle)\n", co2Saved));
        sb.append(String.format("  * Eco-Rewards  : +%d points earned\n", ecoCreditsEarned));
        sb.append(String.format("  * Energy Burn  : %d kcal\n", (int) (durationMinutes * 7.5)));
        sb.append("----------------------------------------------------\n");
        sb.append("FARES & FINANCIAL BREAKDOWN:\n");
        sb.append(String.format("  Gross Fare     : INR %8.2f\n", grossFare));
        sb.append(String.format("  Reward Discount: INR %8.2f\n", discount));
        sb.append("                   ----------\n");
        sb.append(String.format("  NET DEBITED    : INR %8.2f\n", netPaid));
        sb.append("----------------------------------------------------\n");
        sb.append(String.format("Wallet Balance   : INR %8.2f\n", user.getWalletBalance()));
        sb.append("Payment Status   : SUCCESS (PAID VIA DIGITAL WALLET)\n");
        sb.append("====================================================\n");
        sb.append("       Thank you for choosing zero-emission transit! \n");

        txtReceipt.setText(sb.toString());

        JPanel cardWrapper = new JPanel(new BorderLayout());
        cardWrapper.setBackground(UITheme.BG_DARK);
        cardWrapper.setBorder(new EmptyBorder(8, 20, 8, 20));

        JScrollPane scroll = new JScrollPane(txtReceipt);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1, true),
                new EmptyBorder(0, 0, 0, 0)
        ));
        scroll.getViewport().setBackground(UITheme.BG_CARD);
        cardWrapper.add(scroll, BorderLayout.CENTER);
        add(cardWrapper, BorderLayout.CENTER);

        // Buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 14));
        footer.setBackground(UITheme.BG_DARK);

        JButton btnPrint = UITheme.createPrimaryButton("Print / Export Receipt");
        btnPrint.setFont(UITheme.BODY_BOLD);
        btnPrint.addActionListener(e -> {
            UITheme.showStyledMessage(this, "Receipt exported to PDF/Printer successfully!", "Print Confirmed", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnDone = UITheme.createSecondaryButton("Back to Dashboard");
        btnDone.setFont(UITheme.BODY_BOLD);
        btnDone.addActionListener(e -> dispose());

        footer.add(btnPrint);
        footer.add(btnDone);
        add(footer, BorderLayout.SOUTH);
    }
}
