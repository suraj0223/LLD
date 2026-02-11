package com.parking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ParkingLotService {
  private final List<ParkingLevel> parkingLevels;
  private static ParkingLotService INSTANCE;

  private static final double HOURLY_RATE = 10.0;

  private final ConcurrentHashMap<String, ReentrantLock> spotLocks = new ConcurrentHashMap<>();

  private ReentrantLock getLock(ParkingSpot spot) {
    return spotLocks.computeIfAbsent(spot.getSpotId(), k -> new ReentrantLock(true));
  }

  private ParkingLotService() {
    parkingLevels = new ArrayList<>();
  }

  /** Singleton: use static INSTANCE and static getInstance() so callers use ParkingLotService.getInstance(). */
  public static synchronized ParkingLotService getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ParkingLotService();
    }
    return INSTANCE;
  }

  public void addLevels(ParkingLevel level) {
    parkingLevels.add(level);
  }

  /**
   * Finds and parks a vehicle in one atomic step per spot.
   * Iterates all levels/spots, locks the candidate, verifies availability, assigns.
   */
  public Ticket parkVehicle(Vehicle vehicle) {
    for (ParkingLevel level : parkingLevels) {
      for (ParkingSpot spot : level.getParkingSpots()) {
        if (!spot.isOccupied() && spot.canFitVehicle(vehicle)) {
          ReentrantLock lock = getLock(spot);
          lock.lock();
          try {
            if (spot.assignVehicle(vehicle)) {
              return new Ticket(vehicle, spot);
            }
          } finally {
            lock.unlock();
          }
        }
      }
    }
    return null;
  }

  /** Frees the spot associated with the ticket. */
  public void freeSpot(Ticket ticket) {
    if (ticket != null && ticket.getSpot() != null) {
      ParkingSpot spot = ticket.getSpot();
      ReentrantLock lock = getLock(spot);
      lock.lock();
      try {
        spot.removeVehicle();
        ticket.setExitTime(LocalDateTime.now());
      } finally {
        lock.unlock();
      }
    }
  }

  /**
   * Removes vehicle: sets exit time, calculates fee, processes payment via strategy, then frees spot.
   */
  public void removeVehicle(Ticket ticket, PaymentStrategy paymentStrategy) {
    if (ticket == null || ticket.getSpot() == null) {
      return;
    }
    ParkingSpot spot = ticket.getSpot();
    ReentrantLock lock = getLock(spot);
    lock.lock();
    try {
      ticket.setExitTime(LocalDateTime.now());
      double amount = calculateFee(ticket);
      if (paymentStrategy != null) {
        paymentStrategy.pay(amount);
      }
      spot.removeVehicle();
    } finally {
      lock.unlock();
    }
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
