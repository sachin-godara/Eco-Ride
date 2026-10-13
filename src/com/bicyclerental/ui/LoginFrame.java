package com.bicyclerental.ui;

import com.bicyclerental.model.User;
import com.bicyclerental.service.DataRepository;
import com.bicyclerental.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * LoginFrame: Modern entry point for EcoRide Bicycle Rental System.
 * Features role-based authentication, quick login shortcuts, and responsive card layout.
 */
public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblStatus;

    public LoginFrame() {
        setTitle("EcoRide - Smart Bicycle Rental System | Authentication");
        setSize(520, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout());

        initUI();
    }

    private void initUI() {
        // Header Panel with Brand
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(UITheme.BG_DARK);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(new EmptyBorder(32, 24, 12, 24));

        JLabel lblLogo = new JLabel("EcoRide");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblLogo.setForeground(Color.BLACK);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTagline = new JLabel("Smart Bicycle Rental & Carbon Offset Platform");
        lblTagline.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTagline.setForeground(Color.BLACK);
        lblTagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(lblLogo);
        headerPanel.add(Box.createVerticalStrut(6));
        headerPanel.add(lblTagline);

        add(headerPanel, BorderLayout.NORTH);

        // Center Card Panel
        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        centerWrapper.setBackground(UITheme.BG_DARK);

        JPanel card = UITheme.createCardPanel();
        card.setPreferredSize(new Dimension(440, 360));
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 12, 6, 12);
        gbc.gridx = 0;

        // Username
        gbc.gridy = 0;
        JLabel lblUser = new JLabel("Username or Email");
        lblUser.setFont(UITheme.BODY_BOLD);
        lblUser.setForeground(UITheme.TEXT_LIGHT);
        card.add(lblUser, gbc);

        gbc.gridy = 1;
        txtUsername = UITheme.createStyledTextField(20);
        txtUsername.setText("student");
        card.add(txtUsername, gbc);

        // Password
        gbc.gridy = 2;
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(UITheme.BODY_BOLD);
        lblPass.setForeground(UITheme.TEXT_LIGHT);
        card.add(lblPass, gbc);

        gbc.gridy = 3;
        txtPassword = UITheme.createStyledPasswordField(20);
        txtPassword.setText("user123");
        card.add(txtPassword, gbc);

        // Status Error label
        gbc.gridy = 4;
        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(UITheme.ACCENT_ROSE);
        card.add(lblStatus, gbc);

        // Primary Sign In Button
        gbc.gridy = 5;
        JButton btnLogin = UITheme.createPrimaryButton("Sign In");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.addActionListener(e -> performLogin());
        card.add(btnLogin, gbc);

        // Subtle Quick Fill Row
        gbc.gridy = 6;
        JPanel quickPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        quickPanel.setOpaque(false);

        JLabel lblQuick = new JLabel("Quick Fill:");
        lblQuick.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblQuick.setForeground(UITheme.TEXT_MUTED);

        JButton btnQuickStudent = UITheme.createSecondaryButton("Rider Account");
        btnQuickStudent.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnQuickStudent.addActionListener(e -> {
            txtUsername.setText("student");
            txtPassword.setText("user123");
        });

        JButton btnQuickAdmin = UITheme.createSecondaryButton("Fleet Admin");
        btnQuickAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnQuickAdmin.addActionListener(e -> {
            txtUsername.setText("admin");
            txtPassword.setText("admin123");
        });

        quickPanel.add(lblQuick);
        quickPanel.add(btnQuickStudent);
        quickPanel.add(btnQuickAdmin);
        card.add(quickPanel, gbc);

        centerWrapper.add(card);
        add(centerWrapper, BorderLayout.CENTER);

        // Footer with Register link
        JPanel footer = new JPanel();
        footer.setBackground(UITheme.BG_DARK);
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBorder(new EmptyBorder(10, 20, 24, 20));

        JPanel registerRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerRow.setBackground(UITheme.BG_DARK);

        JLabel lblNoAcc = new JLabel("New rider?");
        lblNoAcc.setForeground(UITheme.TEXT_MUTED);
        JButton btnRegister = new JButton("Create an Account");
        btnRegister.setFont(UITheme.BODY_BOLD);
        btnRegister.setForeground(Color.BLACK);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> new RegisterDialog(this).setVisible(true));

        registerRow.add(lblNoAcc);
        registerRow.add(btnRegister);

        footer.add(registerRow);
        add(footer, BorderLayout.SOUTH);
    }

    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Please enter both username and password.");
            return;
        }

        User user = DataRepository.getInstance().authenticate(username, password);
        if (user != null) {
            lblStatus.setText(" ");
            dispose();
            if (user.isAdmin() || "ADMIN".equalsIgnoreCase(user.getRole())) {
                new AdminDashboard(user).setVisible(true);
            } else {
                new CustomerDashboard(user).setVisible(true);
            }
        } else {
            lblStatus.setText("Invalid credentials. Please verify your details.");
        }
    }
}
