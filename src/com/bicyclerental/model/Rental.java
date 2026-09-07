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
}
