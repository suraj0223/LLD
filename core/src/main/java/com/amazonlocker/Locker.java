package com.amazonlocker;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Getter
public class Locker {
    private final List<Compartment> compartments;
    private final Map<String, AccessToken> activeTokens;
    private final AllocationStrategy allocationStrategy;
    private final Random random;
    private static Locker lockerInstance;

    public static synchronized Locker getLocker(List<Compartment> compartments, AllocationStrategy allocationStrategy) {
        if (lockerInstance == null) {
            lockerInstance = new Locker(compartments, allocationStrategy);
        }
        return lockerInstance;
    }

    private Locker(List<Compartment> compartments, AllocationStrategy allocationStrategy) {
        this.compartments = compartments;
        this.activeTokens = new HashMap<>();
        this.allocationStrategy = allocationStrategy;
        this.random = new Random();
    }

    public AccessToken depositPackage(Package pkg) {
        Compartment compartment = allocationStrategy.allocate(compartments, pkg.getSize());
        if (compartment == null) {
            throw new IllegalStateException("No available compartment for package " + pkg.getOrderId());
        }

        compartment.markOccupied();
        String otp = String.format("%06d", random.nextInt(1000000));
        LocalDateTime expiration = LocalDateTime.now().plusDays(7);
        AccessToken token = new AccessToken(otp, expiration, compartment, pkg);
        activeTokens.put(otp, token);

        System.out.println("Package " + pkg.getOrderId() + " deposited in compartment " + compartment.getId()
                + ". OTP: " + otp + " sent to " + pkg.getReceiverContact());
        return token;
    }

    public Package pickup(String tokenCode) {
        AccessToken token = activeTokens.get(tokenCode);
        if (token == null) {
            throw new IllegalStateException("Invalid token: " + tokenCode);
        }
        if (token.isExpired()) {
            throw new IllegalStateException("Token has expired: " + tokenCode);
        }

        token.getCompartment().markFree();
        activeTokens.remove(tokenCode);
        System.out.println("Package " + token.getPkg().getOrderId() + " picked up from compartment "
                + token.getCompartment().getId());
        return token.getPkg();
    }

    public void cleanupExpiredTokens() {
        Iterator<Map.Entry<String, AccessToken>> it = activeTokens.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, AccessToken> entry = it.next();
            if (entry.getValue().isExpired()) {
                entry.getValue().getCompartment().markFree();
                System.out.println("Expired token " + entry.getKey() + " cleaned up. Compartment "
                        + entry.getValue().getCompartment().getId() + " freed.");
                it.remove();
            }
        }
    }
}
