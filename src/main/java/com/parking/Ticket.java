package com.parking;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ticket {
  private final String ticketId;
  private final Vehicle vehicle;
  private final ParkingSpot spot;
  private final LocalDateTime entryTime;
  private LocalDateTime exitTime;

  public Ticket(Vehicle vehicle, ParkingSpot spot) {
    this.ticketId = UUID.randomUUID().toString();
    this.vehicle = vehicle;
    this.spot = spot;
    this.entryTime = LocalDateTime.now();
    this.exitTime = null;
  }
}
