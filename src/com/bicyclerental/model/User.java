package com.bicyclerental.model;

import java.io.Serializable;

public class User implements Serializable {
    private int userId;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String role; // "CUSTOMER", "OPERATOR", "ADMIN"
    private double walletBalance;
    private int ecoCredits;

    public User() {}

    public User(int userId, String username, String fullName, String email, String phone, String role, double walletBalance, int ecoCredits) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.walletBalance = walletBalance;
        this.ecoCredits = ecoCredits;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public double getWalletBalance() { return walletBalance; }
    public void setWalletBalance(double walletBalance) { this.walletBalance = walletBalance; }

    public int getEcoCredits() { return ecoCredits; }
    public void setEcoCredits(int ecoCredits) { this.ecoCredits = ecoCredits; }

    public boolean isAdmin() { return "ADMIN".equalsIgnoreCase(role); }
    public boolean isOperator() { return "OPERATOR".equalsIgnoreCase(role) || isAdmin(); }
}
