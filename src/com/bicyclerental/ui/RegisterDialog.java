package com.bicyclerental.ui;

import com.bicyclerental.service.DataRepository;
import com.bicyclerental.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * RegisterDialog: Modal dialog for registering new riders with validation.
 */
public class RegisterDialog extends JDialog {

    private JTextField txtUsername;
    private JTextField txtFullName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JPasswordField txtPassword;
    private JLabel lblMsg;

    public RegisterDialog(Frame parent) {
        super(parent, "Create New Account - EcoRide", true);
        setSize(440, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout());

        initUI();
    }

    private void initUI() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        header.setBackground(UITheme.BG_DARK);
        header.setBorder(new EmptyBorder(15, 10, 5, 10));

        JLabel title = new JLabel("Join EcoRide Smart Mobility");
        title.setFont(UITheme.SUBTITLE_FONT);
        title.setForeground(UITheme.PRIMARY);
        header.add(title);
        add(header, BorderLayout.NORTH);

        JPanel formCard = UITheme.createCardPanel();
        formCard.setLayout(new GridLayout(6, 2, 10, 12));
        formCard.setBorder(new EmptyBorder(15, 20, 15, 20));

        formCard.add(createLabel("Username:"));
        txtUsername = UITheme.createStyledTextField(15);
        formCard.add(txtUsername);

        formCard.add(createLabel("Full Name:"));
        txtFullName = UITheme.createStyledTextField(15);
        formCard.add(txtFullName);

        formCard.add(createLabel("Email Address:"));
        txtEmail = UITheme.createStyledTextField(15);
        formCard.add(txtEmail);

        formCard.add(createLabel("Mobile Number:"));
        txtPhone = UITheme.createStyledTextField(15);
        formCard.add(txtPhone);

        formCard.add(createLabel("Password:"));
        txtPassword = UITheme.createStyledPasswordField(15);
        formCard.add(txtPassword);

        lblMsg = new JLabel("Starting wallet credit: ₹250.00");
        lblMsg.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblMsg.setForeground(UITheme.TEXT_ACCENT);
        formCard.add(lblMsg);

        add(formCard, BorderLayout.CENTER);

        // Buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 15));
        footer.setBackground(UITheme.BG_DARK);

        JButton btnCancel = UITheme.createSecondaryButton("Cancel");
        btnCancel.addActionListener(e -> dispose());

        JButton btnSubmit = UITheme.createPrimaryButton("Register & Claim ₹250");
        btnSubmit.addActionListener(e -> handleRegister());

        footer.add(btnCancel);
        footer.add(btnSubmit);
        add(footer, BorderLayout.SOUTH);
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UITheme.BODY_BOLD);
        l.setForeground(UITheme.TEXT_LIGHT);
        return l;
    }

    private void handleRegister() {
        String user = txtUsername.getText().trim();
        String name = txtFullName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if (user.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are mandatory.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = DataRepository.getInstance().registerUser(user, name, email, phone, "CUSTOMER");
        if (success) {
            JOptionPane.showMessageDialog(this, "Account created successfully! ₹250 welcome bonus credited.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists. Please pick another.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
