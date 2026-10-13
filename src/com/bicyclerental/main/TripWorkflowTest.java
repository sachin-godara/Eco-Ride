package com.bicyclerental.main;

import com.bicyclerental.model.*;
import com.bicyclerental.service.DataRepository;

public class TripWorkflowTest {
    public static void main(String[] args) {
        System.out.println(">>> STARTING AUTOMATED TRIP IN PROGRESS VERIFICATION <<<");

        DataRepository repo = DataRepository.getInstance();
        User user = repo.authenticate("student", "user123");
        assert user != null : "Student authentication failed!";
        System.out.println("[PASS] Authenticated user: " + user.getFullName() + " (Wallet: INR " + user.getWalletBalance() + ", Credits: " + user.getEcoCredits() + ")");

        Station origin = repo.getStationById(1);
        Bicycle bike = repo.getBicycleById(101);
        double initialWallet = user.getWalletBalance();
        int initialCredits = user.getEcoCredits();

        // 1. Start Rental
        Rental rental = repo.startRental(bike, origin);
        assert rental != null : "Rental creation failed!";
        assert repo.getActiveRental() == rental : "Active rental not registered in repository!";
        assert "IN_USE".equals(bike.getStatus()) : "Bike status not updated to IN_USE!";
        assert rental.getElapsedSeconds() == 0 : "Initial elapsed seconds not 0!";
        System.out.println("[PASS] Rental started: PIN " + rental.getUnlockPin() + " for " + bike.getBikeCode());

        // 2. Simulate Active Ride Progress (600 seconds = 10 minutes)
        for (int i = 0; i < 600; i++) {
            rental.tick(1);
        }
        assert rental.getElapsedSeconds() == 600 : "Elapsed seconds should be 600!";
        int duration = rental.calculateDurationMinutes();
        double dist = rental.calculateDistanceKm();
        double co2 = rental.calculateCo2SavedKg();
        int calories = rental.calculateCalories();
        double grossFare = rental.calculateGrossFare(bike.getHourlyRate());
        int credits = rental.calculateEcoCredits();

        System.out.println("[PASS] Active ride ticking verified: " + rental.getElapsedSeconds() + "s (" + duration + " mins)");
        System.out.printf("       Distance: %.2f km, CO2 Avoided: %.2f kg, Calories: %d kcal, Gross Fare: INR %.2f, Credits: %d\n",
                dist, co2, calories, grossFare, credits);

        assert duration == 10 : "Duration should be 10 mins";
        assert dist > 2.0 : "Distance should be ~2.33 km";
        assert co2 > 0.4 : "CO2 should be ~0.49 kg";
        assert grossFare == 10.00 : "Base fare should be INR 10.00 for first 15 mins";

        // 3. Complete Rental / Docking
        Station destination = repo.getStationById(2);
        int initialAvailable = destination.getAvailableBikes();
        double discount = Math.min(grossFare - 5.0, user.getEcoCredits());
        double net = grossFare - discount;

        repo.completeRental(rental, destination, duration, dist, net, credits, co2);

        assert repo.getActiveRental() == null : "Active rental should be cleared!";
        assert "AVAILABLE".equals(bike.getStatus()) : "Bike should be reset to AVAILABLE!";
        assert bike.getCurrentStationId() == destination.getStationId() : "Bike station should be destination station!";
        assert destination.getAvailableBikes() == initialAvailable + 1 : "Destination dock available bikes should increment!";
        assert rental.getDistanceKm() == dist : "DistanceKm should be persisted on rental!";
        assert "COMPLETED".equals(rental.getRentalStatus()) : "Rental status should be COMPLETED!";

        System.out.printf("[PASS] Trip docked and settled. Net paid: INR %.2f, Eco-Credits earned: +%d\n", net, credits);
        System.out.printf("       Updated Wallet: INR %.2f (Deducted INR %.2f)\n", user.getWalletBalance(), net);
        System.out.printf("       Updated Eco-Credits: %d pts\n", user.getEcoCredits());

        assert user.getWalletBalance() == initialWallet - net : "Wallet deduction mismatch!";

        System.out.println("==================================================================");
        System.out.println(">>> ALL TRIP IN PROGRESS VERIFICATION CHECKS PASSED 100%! <<<");
        System.out.println("==================================================================");
    }
}
