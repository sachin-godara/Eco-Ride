package com.bicyclerental.util;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * UITheme: Centralized Design System for Smart Bicycle Rental System (EcoRide)
 * Configures FlatLaf modern Light Look-and-Feel, rounded component geometry, and aesthetic emerald accents.
 */
public class UITheme {

    // Primary Brand Colors (Aesthetic Emerald & Forest Green)
    public static final Color PRIMARY = new Color(16, 149, 106);       // Aesthetic Emerald Green
    public static final Color PRIMARY_DARK = new Color(4, 120, 87);    // Rich Forest Green 700 (High contrast)
    public static final Color PRIMARY_LIGHT = new Color(52, 211, 153);  // Mint Accent
    
    // Background Surfaces (Aesthetic Crisp White & Soft Slate)
    public static final Color BG_DARK = new Color(243, 244, 246);       // Slate 100 - Clean modern background canvas
    public static final Color BG_CARD = new Color(255, 255, 255);       // Pure Crisp White Cards
    public static final Color BG_CARD_HOVER = new Color(238, 242, 246); // Slate 150
    public static final Color BG_INPUT = new Color(255, 255, 255);      // Pure Crisp White Inputs
    
    // Text Colors (100% Solid Black for maximum contrast and effortless reading)
    public static final Color TEXT_LIGHT = new Color(0, 0, 0);          // Pure Solid Black (#000000)
    public static final Color TEXT_MUTED = new Color(0, 0, 0);          // Pure Solid Black (#000000)
    public static final Color TEXT_ACCENT = new Color(15, 118, 110);    // Teal 700 - High contrast teal
    
    // Status Accents (Rich, high-contrast tones)
    public static final Color ACCENT_BLUE = new Color(29, 78, 216);     // Blue 700
    public static final Color ACCENT_AMBER = new Color(180, 83, 9);     // Amber 700
    public static final Color ACCENT_ROSE = new Color(190, 18, 60);     // Rose 700
    public static final Color BORDER_COLOR = new Color(203, 213, 225);   // Slate 300 - Distinct modern card borders

    // Modern Typography
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font METRIC_FONT = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font CODE_FONT = new Font("Consolas", Font.BOLD, 18);

    public static void setupFlatLaf() {
        try {
            // Apply FlatLaf modern Light theme
            FlatLightLaf.setup();

            // High-contrast pure solid black text palette
            Color solidBlack = Color.BLACK;
            Color pureWhite = Color.WHITE;
            Color canvasBg = BG_DARK; // Slate 100
            Color cardBg = BG_CARD;   // pure white
            Color tableHeaderBg = new Color(241, 245, 249);
            Color selectionMint = new Color(209, 250, 229);

            // Global geometry
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("ProgressBar.arc", 10);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));

            // OptionPane & Dialogs (Crucial: Guarantees 100% black text readability)
            UIManager.put("OptionPane.background", pureWhite);
            UIManager.put("OptionPane.foreground", solidBlack);
            UIManager.put("OptionPane.messageForeground", solidBlack);
            UIManager.put("OptionPane.messageFont", BODY_BOLD);
            UIManager.put("OptionPane.buttonFont", BODY_BOLD);
            UIManager.put("Dialog.background", pureWhite);
            UIManager.put("Dialog.foreground", solidBlack);

            // Labels
            UIManager.put("Label.foreground", solidBlack);
            UIManager.put("Label.disabledForeground", solidBlack);
            UIManager.put("Label.font", BODY_BOLD);

            // TitlePane (Window & Dialog Title Bars in FlatLaf)
            UIManager.put("TitlePane.foreground", solidBlack);
            UIManager.put("TitlePane.inactiveForeground", solidBlack);
            UIManager.put("TitlePane.embeddedForeground", solidBlack);
            UIManager.put("TitlePane.font", BODY_BOLD);

            // Panels
            UIManager.put("Panel.background", canvasBg);
            UIManager.put("Panel.foreground", solidBlack);

            // Buttons
            UIManager.put("Button.foreground", solidBlack);
            UIManager.put("Button.background", pureWhite);
            UIManager.put("Button.default.background", new Color(241, 245, 249));
            UIManager.put("Button.default.foreground", solidBlack);
            UIManager.put("Button.default.borderColor", new Color(100, 116, 139));
            UIManager.put("Button.default.focusedBorderColor", solidBlack);
            UIManager.put("Button.default.focusColor", new Color(226, 232, 240));
            UIManager.put("Button.borderColor", new Color(203, 213, 225));
            UIManager.put("Button.focusedBorderColor", solidBlack);
            UIManager.put("Button.font", BODY_BOLD);
            UIManager.put("OptionPane.buttonFont", BODY_BOLD);
            UIManager.put("OptionPane.buttonForeground", solidBlack);

            // Tables
            UIManager.put("Table.rowHeight", 36);
            UIManager.put("Table.foreground", solidBlack);
            UIManager.put("Table.background", pureWhite);
            UIManager.put("Table.font", BODY_BOLD);
            UIManager.put("TableHeader.font", BODY_BOLD);
            UIManager.put("TableHeader.background", tableHeaderBg);
            UIManager.put("TableHeader.foreground", solidBlack);
            UIManager.put("Table.selectionBackground", selectionMint);
            UIManager.put("Table.selectionForeground", solidBlack);

            // TabbedPane
            UIManager.put("TabbedPane.showTabSeparators", true);
            UIManager.put("TabbedPane.selectedBackground", pureWhite);
            UIManager.put("TabbedPane.selectedForeground", solidBlack);
            UIManager.put("TabbedPane.unselectedForeground", solidBlack);
            UIManager.put("TabbedPane.foreground", solidBlack);
            UIManager.put("TabbedPane.background", tableHeaderBg);
            UIManager.put("TabbedPane.font", BODY_BOLD);
            UIManager.put("TabbedPane.tabInsets", new Insets(8, 16, 8, 16));

            // Text Inputs
            UIManager.put("TextField.foreground", solidBlack);
            UIManager.put("TextField.background", pureWhite);
            UIManager.put("TextField.caretColor", solidBlack);
            UIManager.put("TextField.font", BODY_BOLD);
            UIManager.put("PasswordField.foreground", solidBlack);
            UIManager.put("PasswordField.background", pureWhite);
            UIManager.put("PasswordField.caretColor", solidBlack);
            UIManager.put("PasswordField.font", BODY_BOLD);
            UIManager.put("TextArea.foreground", solidBlack);
            UIManager.put("TextArea.background", pureWhite);
            UIManager.put("TextArea.font", BODY_BOLD);

            // Comboboxes
            UIManager.put("ComboBox.foreground", solidBlack);
            UIManager.put("ComboBox.background", pureWhite);
            UIManager.put("ComboBox.font", BODY_BOLD);
            UIManager.put("ComboBox.selectionBackground", selectionMint);
            UIManager.put("ComboBox.selectionForeground", solidBlack);

            UIManager.put("Component.focusWidth", 1);
            UIManager.put("Component.innerFocusWidth", 0);
        } catch (Exception e) {
            System.err.println("[UITheme] FlatLaf initialization failed, falling back: " + e.getMessage());
        }
    }

    /**
     * showStyledMessage: Displays a crystal-clear, high-contrast modal dialog
     * with custom typography, clean padding, and pure solid black text.
     */
    public static void showStyledMessage(Component parent, String message, String title, int messageType) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(12, 18, 12, 18));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(10));

        String[] lines = message.split("\n");
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                panel.add(Box.createVerticalStrut(6));
                continue;
            }
            JLabel lblLine = new JLabel(line);
            lblLine.setForeground(Color.BLACK);
            lblLine.setAlignmentX(Component.LEFT_ALIGNMENT);
            lblLine.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
            if (line.contains("Passcode:") || line.contains("PIN:")) {
                lblLine.setFont(new Font("Segoe UI", Font.BOLD, 16));
            } else if (line.contains("CONFIRMED") || line.contains("Confirmed")) {
                lblLine.setFont(new Font("Segoe UI", Font.BOLD, 15));
            } else {
                lblLine.setFont(new Font("Segoe UI", Font.BOLD, 13));
            }
            panel.add(lblLine);
            panel.add(Box.createVerticalStrut(4));
        }

        Dimension d = panel.getPreferredSize();
        panel.setPreferredSize(new Dimension(Math.max(520, d.width + 100), d.height));

        JOptionPane optionPane = new JOptionPane(panel, messageType, JOptionPane.DEFAULT_OPTION);
        optionPane.setBackground(Color.WHITE);
        JDialog dialog = optionPane.createDialog(parent, title);
        styleDialogButtons(dialog);
        dialog.pack();
        dialog.setSize(Math.max(580, dialog.getWidth() + 80), dialog.getHeight());
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        dialog.dispose();
    }

    private static void styleDialogButtons(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JButton) {
                JButton btn = (JButton) c;
                btn.setForeground(Color.BLACK);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
                btn.setBackground(new Color(241, 245, 249));
                btn.setFocusPainted(false);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(148, 163, 184), 1, true),
                        new EmptyBorder(7, 22, 7, 22)
                ));
            } else if (c instanceof Container) {
                styleDialogButtons((Container) c);
            }
        }
    }

    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(BODY_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setBackground(PRIMARY);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(PRIMARY_DARK);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(PRIMARY);
            }
        });
        return btn;
    }

    public static JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(BODY_BOLD);
        btn.setForeground(Color.BLACK);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(9, 18, 9, 18)
        ));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(241, 245, 249)); // Slate 100 hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.WHITE);
            }
        });
        return btn;
    }

    public static JTextField createStyledTextField(int columns) {
        JTextField tf = new JTextField(columns);
        tf.setFont(BODY_FONT);
        tf.setBackground(BG_INPUT);
        tf.setForeground(TEXT_LIGHT);
        tf.setCaretColor(TEXT_LIGHT);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        return tf;
    }

    public static JPasswordField createStyledPasswordField(int columns) {
        JPasswordField pf = new JPasswordField(columns);
        pf.setFont(BODY_FONT);
        pf.setBackground(BG_INPUT);
        pf.setForeground(TEXT_LIGHT);
        pf.setCaretColor(TEXT_LIGHT);
        pf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        return pf;
    }

    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(16, 16, 16, 16)
        ));
        return panel;
    }

    public static JPanel createKeyValueRow(String key, String value, Color valueColor) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(5, 4, 5, 4));

        JLabel lblKey = new JLabel(key);
        lblKey.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblKey.setForeground(Color.BLACK);

        JLabel lblVal = new JLabel(value);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblVal.setForeground(valueColor != null ? valueColor : Color.BLACK);

        row.add(lblKey, BorderLayout.WEST);
        row.add(lblVal, BorderLayout.EAST);
        return row;
    }

    public static JPanel createStatBox(String label, String value, Color valueColor) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(new Color(248, 250, 252));
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(10, 14, 10, 14)
        ));

        JLabel lbl = new JLabel(label.toUpperCase());
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(Color.BLACK);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 16));
        val.setForeground(valueColor != null ? valueColor : Color.BLACK);

        box.add(lbl);
        box.add(Box.createVerticalStrut(4));
        box.add(val);
        return box;
    }

    public static DefaultTableCellRenderer createStatusCellRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                String text = value != null ? value.toString() : "";

                if (isSelected) {
                    setForeground(new Color(6, 78, 59));
                    setBackground(new Color(209, 250, 229));
                    setFont(BODY_BOLD);
                } else {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                    if ("AVAILABLE".equalsIgnoreCase(text) || text.contains("BALANCED") || text.contains("Normal") || "ACTIVE".equalsIgnoreCase(text)) {
                        setForeground(PRIMARY_DARK);
                        setFont(BODY_BOLD);
                    } else if ("IN_USE".equalsIgnoreCase(text) || text.contains("HIGH") || text.contains("Congestion")) {
                        setForeground(ACCENT_AMBER);
                        setFont(BODY_BOLD);
                    } else if ("MAINTENANCE".equalsIgnoreCase(text) || text.contains("FULL") || text.contains("STARVED")) {
                        setForeground(ACCENT_ROSE);
                        setFont(BODY_BOLD);
                    } else {
                        setForeground(Color.BLACK);
                        setFont(BODY_BOLD);
                    }
                }
                return c;
            }
        };
    }
}
