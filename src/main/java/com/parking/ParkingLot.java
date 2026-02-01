package com.parking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ParkingLot {
  private final List<ParkingLevel> parkingLevels;
  private static ParkingLot INSTANCE;

  private static final double HOURLY_RATE = 10.0;

  private ParkingLot() {
    parkingLevels = new ArrayList<>();
  }

  /** Singleton: use static INSTANCE and static getInstance() so callers use ParkingLot.getInstance(). */
  public static synchronized ParkingLot getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ParkingLot();
    }
    return INSTANCE;
  }

  public void addLevels(ParkingLevel level) {
    parkingLevels.add(level);
  }

  public ParkingSpot findParkingSpot(Vehicle vehicle) {
    for (ParkingLevel level : parkingLevels) {
      ParkingSpot spot = level.getParkingSpot(vehicle);
      if (spot != null) {
        return spot;
      }
    }
    return null;
  }

  public Ticket parkVehicle(Vehicle vehicle) {
    ParkingSpot spot = findParkingSpot(vehicle);
    if (spot != null && spot.assignVehicle(vehicle)) {
      return new Ticket(vehicle, spot);
    }
    return null;
  }

  /** Frees the spot associated with the ticket. */
  public void freeSpot(Ticket ticket) {
    if (ticket != null && ticket.getSpot() != null) {
      ticket.getSpot().removeVehicle();
      ticket.setExitTime(LocalDateTime.now());
    }
  }

  /**
   * Removes vehicle: sets exit time, calculates fee, processes payment via strategy, then frees spot.
   */
  public void removeVehicle(Ticket ticket, PaymentStrategy paymentStrategy) {
    if (ticket == null) {
      return;
    }
    ticket.setExitTime(LocalDateTime.now());
    double amount = calculateFee(ticket);
    if (paymentStrategy != null) {
      paymentStrategy.pay(amount);
    }
    freeSpot(ticket);
  }

  private double calculateFee(Ticket ticket) {
    LocalDateTime entry = ticket.getEntryTime();
    LocalDateTime exit = ticket.getExitTime() != null ? ticket.getExitTime() : LocalDateTime.now();
    long hours = java.time.Duration.between(entry, exit).toHours();
    if (hours < 1) {
      hours = 1;
    }
    return hours * HOURLY_RATE;
  }
}
