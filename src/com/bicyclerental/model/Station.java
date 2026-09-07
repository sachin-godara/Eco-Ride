package com.bicyclerental.model;

import java.io.Serializable;

public class Station implements Serializable {
    private int stationId;
    private String stationName;
    private String locationAddress;
    private int totalDocks;
    private int availableBikes;
    private String status; // ACTIVE, UNDER_MAINTENANCE, CLOSED

    public Station() {}

    public Station(int stationId, String stationName, String locationAddress, int totalDocks, int availableBikes, String status) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.locationAddress = locationAddress;
        this.totalDocks = totalDocks;
        this.availableBikes = availableBikes;
        this.status = status;
    }

    public int getStationId() { return stationId; }
    public void setStationId(int stationId) { this.stationId = stationId; }

    public String getStationName() { return stationName; }
    public void setStationName(String stationName) { this.stationName = stationName; }

    public String getLocationAddress() { return locationAddress; }
    public void setLocationAddress(String locationAddress) { this.locationAddress = locationAddress; }

    public int getTotalDocks() { return totalDocks; }
    public void setTotalDocks(int totalDocks) { this.totalDocks = totalDocks; }

    public int getAvailableBikes() { return availableBikes; }
    public void setAvailableBikes(int availableBikes) { this.availableBikes = availableBikes; }

    public int getEmptyDocks() { return Math.max(0, totalDocks - availableBikes); }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // USP: Dock Capacity calculation
    public double getOccupancyRate() {
        if (totalDocks == 0) return 0.0;
        return ((double) availableBikes / totalDocks) * 100.0;
    }

    public String getCapacityStatus() {
        double rate = getOccupancyRate();
        if (availableBikes == 0) return "EMPTY (Starved)";
        if (getEmptyDocks() == 0) return "FULL (Congested)";
        if (rate >= 80.0) return "HIGH CONGESTION";
        if (rate <= 20.0) return "LOW AVAILABILITY";
        return "BALANCED";
    }

    @Override
    public String toString() {
        return stationName + " (" + availableBikes + "/" + totalDocks + " bikes available)";
    }
}
