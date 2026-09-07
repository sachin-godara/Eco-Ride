package com.bicyclerental.ui;

import com.bicyclerental.model.User;
import com.bicyclerental.service.DataRepository;
import com.bicyclerental.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LoginFrame: Modern entry point for EcoRide Bicycle Rental System.
 */
public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> comboRole;
    private JLabel lblStatus;

    public LoginFrame() {
        setTitle("EcoRide - Smart Bicycle Rental System | Login");
        setSize(480, 580);
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
        headerPanel.setBorder(new EmptyBorder(30, 20, 10, 20));

        JLabel lblLogo = new JLabel("EcoRide");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblLogo.setForeground(UITheme.PRIMARY);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTagline = new JLabel("Smart Bicycle Rental & Carbon Offset Platform");
        lblTagline.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTagline.setForeground(UITheme.TEXT_MUTED);
        lblTagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblCourse = new JLabel("NSP Minor Project | Mid Term Evaluation");
        lblCourse.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblCourse.setForeground(UITheme.ACCENT_AMBER);
        lblCourse.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(lblLogo);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(lblTagline);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(lblCourse);

        add(headerPanel, BorderLayout.NORTH);

        // Center Card Panel
        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        centerWrapper.setBackground(UITheme.BG_DARK);

        JPanel card = UITheme.createCardPanel();
        card.setPreferredSize(new Dimension(390, 340));
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.gridx = 0;

        // Role Selector
        gbc.gridy = 0;
        JLabel lblRole = new JLabel("Login Role");
        lblRole.setFont(UITheme.BODY_BOLD);
        lblRole.setForeground(UITheme.TEXT_LIGHT);
        card.add(lblRole, gbc);

        gbc.gridy = 1;
        comboRole = new JComboBox<>(new String[]{"Customer / Student", "Administrator / Operator"});
        comboRole.setFont(UITheme.BODY_FONT);
        comboRole.setBackground(UITheme.BG_INPUT);
        comboRole.setForeground(UITheme.TEXT_LIGHT);
        card.add(comboRole, gbc);

        // Username
        gbc.gridy = 2;
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(UITheme.BODY_BOLD);
        lblUser.setForeground(UITheme.TEXT_LIGHT);
        card.add(lblUser, gbc);

        gbc.gridy = 3;
        txtUsername = UITheme.createStyledTextField(20);
        txtUsername.setText("student"); // Quick test default
        card.add(txtUsername, gbc);

        // Password
        gbc.gridy = 4;
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(UITheme.BODY_BOLD);
        lblPass.setForeground(UITheme.TEXT_LIGHT);
        card.add(lblPass, gbc);

        gbc.gridy = 5;
        txtPassword = UITheme.createStyledPasswordField(20);
        txtPassword.setText("user123");
        card.add(txtPassword, gbc);

        // Status / Error message
        gbc.gridy = 6;
        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(UITheme.ACCENT_ROSE);
        card.add(lblStatus, gbc);

        // Login Button
        gbc.gridy = 7;
        JButton btnLogin = UITheme.createPrimaryButton("Sign In to Portal");
        btnLogin.addActionListener(e -> performLogin());
        card.add(btnLogin, gbc);

        centerWrapper.add(card);
        add(centerWrapper, BorderLayout.CENTER);

        // Footer with Register & Hint
        JPanel footer = new JPanel();
        footer.setBackground(UITheme.BG_DARK);
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBorder(new EmptyBorder(10, 20, 20, 20));

        JPanel registerRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerRow.setBackground(UITheme.BG_DARK);

        JLabel lblNoAcc = new JLabel("New rider?");
        lblNoAcc.setForeground(UITheme.TEXT_MUTED);
        JButton btnRegister = new JButton("Create an Account");
        btnRegister.setFont(UITheme.BODY_BOLD);
        btnRegister.setForeground(UITheme.PRIMARY);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> openRegistration());

        registerRow.add(lblNoAcc);
        registerRow.add(btnRegister);

        JLabel lblHint = new JLabel("Demo Accounts: student / user123  |  admin / admin123");
        lblHint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblHint.setForeground(UITheme.TEXT_MUTED);
        lblHint.setAlignmentX(Component.CENTER_ALIGNMENT);

        footer.add(registerRow);
        footer.add(lblHint);
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
            dispose(); // Close login window
            if (user.isAdmin() || comboRole.getSelectedIndex() == 1) {
                new AdminDashboard(user).setVisible(true);
            } else {
                new CustomerDashboard(user).setVisible(true);
            }
        } else {
            lblStatus.setText("Invalid credentials. Try 'student' or 'admin'.");
        }
    }

    private void openRegistration() {
        new RegisterDialog(this).setVisible(true);
    }
}
