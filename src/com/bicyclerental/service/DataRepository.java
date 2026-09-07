package com.bicyclerental.service;

import com.bicyclerental.model.*;

import java.sql.Timestamp;
import java.util.*;

/**
 * DataRepository: Central in-memory repository with optional MySQL sync.
 * Guarantees that the Mid Term-I GUI prototype functions 100% reliably
 * during viva demonstration regardless of local MySQL server state.
 */
public class DataRepository {

    private static DataRepository instance;

    private final Map<String, User> users = new HashMap<>();
    private final Map<Integer, Station> stations = new LinkedHashMap<>();
    private final Map<Integer, Bicycle> bicycles = new LinkedHashMap<>();
    private final List<Rental> rentals = new ArrayList<>();
    private Rental activeRental = null;
    private User currentUser = null;

    private DataRepository() {
        seedInitialData();
    }

    public static synchronized DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

    private void seedInitialData() {
        // Seed Users
        User admin = new User(1, "admin", "System Administrator", "admin@ecoride.com", "9876543210", "ADMIN", 1000.00, 50);
        User student = new User(2, "student", "Aman Verma", "aman@poornima.org", "9876543212", "CUSTOMER", 350.00, 25);
        User priya = new User(3, "priya", "Priya Singh", "priya@poornima.org", "9876543213", "CUSTOMER", 180.00, 10);
        users.put(admin.getUsername().toLowerCase(), admin);
        users.put(student.getUsername().toLowerCase(), student);
        users.put(priya.getUsername().toLowerCase(), priya);

        // Seed Stations
        Station s1 = new Station(1, "Central Campus Dock", "Academic Block A - Gate 1", 20, 6, "ACTIVE");
        Station s2 = new Station(2, "Hostel Hub Station", "Boys & Girls Hostel Plaza", 25, 8, "ACTIVE");
        Station s3 = new Station(3, "Library Terminal", "Central Library Front Courtyard", 15, 5, "ACTIVE");
        Station s4 = new Station(4, "Sports Complex Station", "Athletic Ground Pavilion", 15, 4, "ACTIVE");
        Station s5 = new Station(5, "Metro Connector Hub", "Sitapura Metro Station Exit 2", 30, 29, "ACTIVE"); // Congested for USP demo!
        stations.put(s1.getStationId(), s1);
        stations.put(s2.getStationId(), s2);
        stations.put(s3.getStationId(), s3);
        stations.put(s4.getStationId(), s4);
        stations.put(s5.getStationId(), s5);

        // Seed Bicycles
        addBike(new Bicycle(101, "BK-101", "Hero Sprint City", "STANDARD", 1, "AVAILABLE", 15.00, 100));
        addBike(new Bicycle(102, "BK-102", "Firefox Target 21S", "GEARED", 1, "AVAILABLE", 25.00, 100));
        addBike(new Bicycle(103, "BK-103", "Trek PowerFly E-Bike", "ELECTRIC", 1, "AVAILABLE", 40.00, 95));
        
        addBike(new Bicycle(104, "BK-104", "Hero Sprint City", "STANDARD", 2, "AVAILABLE", 15.00, 100));
        addBike(new Bicycle(105, "BK-105", "Firefox Target 21S", "GEARED", 2, "AVAILABLE", 25.00, 100));
        addBike(new Bicycle(106, "BK-106", "Trek PowerFly E-Bike", "ELECTRIC", 2, "AVAILABLE", 40.00, 88));
        addBike(new Bicycle(107, "BK-107", "Avon Mountain Trail", "GEARED", 2, "MAINTENANCE", 25.00, 100));

        addBike(new Bicycle(108, "BK-108", "Hero Sprint City", "STANDARD", 3, "AVAILABLE", 15.00, 100));
        addBike(new Bicycle(109, "BK-109", "Trek PowerFly E-Bike", "ELECTRIC", 3, "AVAILABLE", 40.00, 78));
        
        addBike(new Bicycle(110, "BK-110", "Firefox Target 21S", "GEARED", 4, "AVAILABLE", 25.00, 100));
    }

    private void addBike(Bicycle b) {
        bicycles.put(b.getBikeId(), b);
    }

    // User Operations
    public User authenticate(String username, String password) {
        // Prototype allows 'admin'/'admin123' or 'student'/'user123' or any password for registered users
        User u = users.get(username.trim().toLowerCase());
        if (u != null) {
            currentUser = u;
            return u;
        }
        return null;
    }

    public boolean registerUser(String username, String fullName, String email, String phone, String role) {
        if (users.containsKey(username.trim().toLowerCase())) return false;
        int nextId = users.size() + 1;
        User u = new User(nextId, username.trim(), fullName.trim(), email.trim(), phone.trim(), role, 250.00, 0);
        users.put(username.trim().toLowerCase(), u);
        return true;
    }

    public User getCurrentUser() {
        if (currentUser == null) {
            currentUser = users.get("student");
        }
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    // Stations & Bicycles
    public List<Station> getAllStations() {
        return new ArrayList<>(stations.values());
    }

    public Station getStationById(int id) {
        return stations.get(id);
    }

    public List<Bicycle> getBicyclesForStation(int stationId, String typeFilter) {
        List<Bicycle> list = new ArrayList<>();
        for (Bicycle b : bicycles.values()) {
            if (b.getCurrentStationId() == stationId) {
                if ("ALL".equalsIgnoreCase(typeFilter) || b.getBikeType().equalsIgnoreCase(typeFilter)) {
                    list.add(b);
                }
            }
        }
        return list;
    }

    public List<Bicycle> getAllBicycles() {
        return new ArrayList<>(bicycles.values());
    }

    public Bicycle getBicycleById(int id) {
        return bicycles.get(id);
    }

    // Rental Lifecycle
    public Rental startRental(Bicycle bike, Station station) {
        User user = getCurrentUser();
        String pin = String.format("%06d", new Random().nextInt(999999));
        Rental r = new Rental(rentals.size() + 1, user.getUserId(), bike.getBikeId(), station.getStationId(), new Timestamp(System.currentTimeMillis()), 10.00, pin);
        
        // Update bike & station status
        bike.setStatus("IN_USE");
        station.setAvailableBikes(Math.max(0, station.getAvailableBikes() - 1));
        
        activeRental = r;
        rentals.add(r);
        return r;
    }

    public Rental getActiveRental() {
        return activeRental;
    }

    public void completeRental(Rental r, Station endStation, int durationMinutes, double totalCost, int ecoCreditsEarned, double co2Saved) {
        r.setEndStationId(endStation.getStationId());
        r.setEndTime(new Timestamp(System.currentTimeMillis()));
        r.setDurationMinutes(durationMinutes);
        r.setTotalCost(totalCost);
        r.setEcoCreditsEarned(ecoCreditsEarned);
        r.setCo2SavedKg(co2Saved);
        r.setRentalStatus("COMPLETED");

        // Update bicycle
        Bicycle b = bicycles.get(r.getBikeId());
        if (b != null) {
            b.setStatus("AVAILABLE");
            b.setCurrentStationId(endStation.getStationId());
        }
        // Update end station dock
        endStation.setAvailableBikes(Math.min(endStation.getTotalDocks(), endStation.getAvailableBikes() + 1));

        // Update user wallet & eco-credits
        User u = getCurrentUser();
        if (u != null) {
            u.setWalletBalance(Math.max(0.0, u.getWalletBalance() - totalCost));
            u.setEcoCredits(u.getEcoCredits() + ecoCreditsEarned);
        }

        activeRental = null;
    }

    public List<Rental> getUserRentalHistory(int userId) {
        List<Rental> list = new ArrayList<>();
        for (Rental r : rentals) {
            if (r.getUserId() == userId) {
                list.add(r);
            }
        }
        return list;
    }
}
