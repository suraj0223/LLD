package com.amazonlocker;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class Main {

    public static void main(String[] args) {
        // Create compartments
        List<Compartment> compartments = Arrays.asList(
                new Compartment("S1", Size.SMALL),
                new Compartment("S2", Size.SMALL),
                new Compartment("M1", Size.MEDIUM),
                new Compartment("M2", Size.MEDIUM),
                new Compartment("L1", Size.LARGE)
        );

        // get locker with best-fit strategy
        Locker locker = Locker.getLocker(compartments, new BestFitStrategy());

        // Deposit packages of different sizes
        System.out.println("=== Depositing Packages ===");
        AccessToken token1 = locker.depositPackage(new Package("ORD-001", Size.SMALL, "alice@example.com"));
        AccessToken token2 = locker.depositPackage(new Package("ORD-002", Size.MEDIUM, "bob@example.com"));
        AccessToken token3 = locker.depositPackage(new Package("ORD-003", Size.LARGE, "charlie@example.com"));

        // Successful pickup
        System.out.println("\n=== Picking Up Package ===");
        locker.pickup(token1.getCode());

        // Invalid pickup attempt
        System.out.println("\n=== Invalid Pickup Attempt ===");
        try {
            locker.pickup("000000");
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Cleanup expired tokens
        System.out.println("\n=== Cleaning Up Expired Tokens ===");
        locker.cleanupExpiredTokens();
        System.out.println("Cleanup complete (no expired tokens expected).");
    }
}
