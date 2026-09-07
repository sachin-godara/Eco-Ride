package com.bicyclerental.model;

import java.io.Serializable;

public class Bicycle implements Serializable {
    private int bikeId;
    private String bikeCode;
    private String modelName;
    private String bikeType; // STANDARD, GEARED, ELECTRIC
    private int currentStationId;
    private String status;   // AVAILABLE, RESERVED, IN_USE, MAINTENANCE
    private double hourlyRate;
    private int batteryLevel; // 0 to 100 for ELECTRIC

    public Bicycle() {}

    public Bicycle(int bikeId, String bikeCode, String modelName, String bikeType, int currentStationId, String status, double hourlyRate, int batteryLevel) {
        this.bikeId = bikeId;
        this.bikeCode = bikeCode;
        this.modelName = modelName;
        this.bikeType = bikeType;
        this.currentStationId = currentStationId;
        this.status = status;
        this.hourlyRate = hourlyRate;
        this.batteryLevel = batteryLevel;
    }

    public int getBikeId() { return bikeId; }
    public void setBikeId(int bikeId) { this.bikeId = bikeId; }

    public String getBikeCode() { return bikeCode; }
    public void setBikeCode(String bikeCode) { this.bikeCode = bikeCode; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public String getBikeType() { return bikeType; }
    public void setBikeType(String bikeType) { this.bikeType = bikeType; }

    public int getCurrentStationId() { return currentStationId; }
    public void setCurrentStationId(int currentStationId) { this.currentStationId = currentStationId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public int getBatteryLevel() { return batteryLevel; }
    public void setBatteryLevel(int batteryLevel) { this.batteryLevel = batteryLevel; }

    public boolean isElectric() {
        return "ELECTRIC".equalsIgnoreCase(bikeType);
    }
}
