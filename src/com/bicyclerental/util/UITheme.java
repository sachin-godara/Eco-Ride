package com.bicyclerental.util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * UITheme: Centralized Design System for Smart Bicycle Rental System (EcoRide)
 * Provides modern color tokens, fonts, and custom Swing component styling.
 */
public class UITheme {

    // Primary Colors
    public static final Color PRIMARY = new Color(16, 185, 129);       // Emerald Eco-Green
    public static final Color PRIMARY_DARK = new Color(5, 150, 105);
    public static final Color PRIMARY_LIGHT = new Color(209, 250, 229);
    
    // Background & Surfaces
    public static final Color BG_DARK = new Color(15, 23, 42);          // Slate 900
    public static final Color BG_CARD = new Color(30, 41, 59);          // Slate 800
    public static final Color BG_CARD_HOVER = new Color(51, 65, 85);    // Slate 700
    public static final Color BG_INPUT = new Color(15, 23, 42);
    
    // Text Colors
    public static final Color TEXT_LIGHT = new Color(248, 250, 252);
    public static final Color TEXT_MUTED = new Color(148, 163, 184);
    public static final Color TEXT_ACCENT = new Color(52, 211, 153);
    
    // Status & Accent Colors
    public static final Color ACCENT_BLUE = new Color(59, 130, 246);
    public static final Color ACCENT_AMBER = new Color(245, 158, 11);
    public static final Color ACCENT_ROSE = new Color(244, 63, 94);
    public static final Color BORDER_COLOR = new Color(51, 65, 85);

    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font METRIC_FONT = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font CODE_FONT = new Font("Consolas", Font.BOLD, 18);

    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(BODY_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setBackground(PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
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
        btn.setForeground(TEXT_LIGHT);
        btn.setBackground(BG_CARD_HOVER);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 18, 10, 18));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(BORDER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(BG_CARD_HOVER);
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
}
