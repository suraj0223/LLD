package com.parking;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ticket {
  private static int ticketCounter = 0;

  private final int ticketId;
  private final Vehicle vehicle;
  private final ParkingSpot spot;
  private final LocalDateTime entryTime;
  private LocalDateTime exitTime;

  public Ticket(Vehicle vehicle, ParkingSpot spot) {
    this.ticketId = ++ticketCounter;
    this.vehicle = vehicle;
    this.spot = spot;
    this.entryTime = LocalDateTime.now();
    this.exitTime = null;
  }
}
