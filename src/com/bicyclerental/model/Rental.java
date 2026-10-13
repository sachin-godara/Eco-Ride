package com.bicyclerental.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Rental implements Serializable {
    private int rentalId;
    private int userId;
    private int bikeId;
    private int startStationId;
    private Integer endStationId;
    private Timestamp startTime;
    private Timestamp endTime;
    private int durationMinutes;
    private double distanceKm;
    private double baseFare;
    private double totalCost;
    private int ecoCreditsEarned;
    private double co2SavedKg;
    private String rentalStatus; // ACTIVE, COMPLETED, CANCELLED
    private String unlockPin;

    public Rental() {}

    public Rental(int rentalId, int userId, int bikeId, int startStationId, Timestamp startTime, double baseFare, String unlockPin) {
        this.rentalId = rentalId;
        this.userId = userId;
        this.bikeId = bikeId;
        this.startStationId = startStationId;
        this.startTime = startTime;
        this.baseFare = baseFare;
        this.unlockPin = unlockPin;
        this.rentalStatus = "ACTIVE";
    }

    public int getRentalId() { return rentalId; }
    public void setRentalId(int rentalId) { this.rentalId = rentalId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getBikeId() { return bikeId; }
    public void setBikeId(int bikeId) { this.bikeId = bikeId; }

    public int getStartStationId() { return startStationId; }
    public void setStartStationId(int startStationId) { this.startStationId = startStationId; }

    public Integer getEndStationId() { return endStationId; }
    public void setEndStationId(Integer endStationId) { this.endStationId = endStationId; }

    public Timestamp getStartTime() { return startTime; }
    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }

    public Timestamp getEndTime() { return endTime; }
    public void setEndTime(Timestamp endTime) { this.endTime = endTime; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }

    public double getBaseFare() { return baseFare; }
    public void setBaseFare(double baseFare) { this.baseFare = baseFare; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public int getEcoCreditsEarned() { return ecoCreditsEarned; }
    public void setEcoCreditsEarned(int ecoCreditsEarned) { this.ecoCreditsEarned = ecoCreditsEarned; }

    public double getCo2SavedKg() { return co2SavedKg; }
    public void setCo2SavedKg(double co2SavedKg) { this.co2SavedKg = co2SavedKg; }

    public String getRentalStatus() { return rentalStatus; }
    public void setRentalStatus(String rentalStatus) { this.rentalStatus = rentalStatus; }

    public String getUnlockPin() { return unlockPin; }
    public void setUnlockPin(String unlockPin) { this.unlockPin = unlockPin; }

    // Live Active Ride Tracking
    private int elapsedSeconds = 0;

    public int getElapsedSeconds() { return elapsedSeconds; }
    public void setElapsedSeconds(int elapsedSeconds) { this.elapsedSeconds = elapsedSeconds; }
    public void tick() { this.elapsedSeconds += 1; }
    public void tick(int seconds) { this.elapsedSeconds += seconds; }

    public int calculateDurationMinutes() {
        if (elapsedSeconds <= 0) return 0;
        return Math.max(1, elapsedSeconds / 60);
    }

    public double calculateDistanceKm() {
        return (elapsedSeconds / 3600.0) * 14.0; // 14 km/h average cycling speed
    }

    public double calculateCo2SavedKg() {
        return calculateDistanceKm() * 0.21; // 0.21 kg CO2 avoided per km vs petrol car
    }

    public int calculateCalories() {
        return (int) ((elapsedSeconds / 60.0) * 7.5); // 7.5 kcal per minute
    }

    public double calculateGrossFare(double hourlyRate) {
        double fare = baseFare > 0 ? baseFare : 10.00; // Base fare covers first 15 mins
        int totalMinutes = calculateDurationMinutes();
        if (totalMinutes > 15) {
            fare += (totalMinutes - 15) * (hourlyRate / 60.0);
        }
        return fare;
    }

    public int calculateEcoCredits() {
        return (int) Math.round(calculateCo2SavedKg() / 0.5); // 1 Eco-Reward point per 0.5 kg CO2 (rounded)
    }
}
