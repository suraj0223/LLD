package com.parking;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingLevel {
  private List<ParkingSpot> parkingSpots = new ArrayList<>();

  public ParkingSpot getParkingSpot(Vehicle vehicle) {
    for (ParkingSpot spot : parkingSpots) {
      if (!spot.isOccupied() && spot.canFitVehicle(vehicle)) {
        return spot;
      }
    }
    return null;
  }

  public void addParkingSpot(ParkingSpot spot) {
    parkingSpots.add(spot);
  }

  public void removeParkingSpot(ParkingSpot spot) {
    parkingSpots.remove(spot);
  }

  public List<ParkingSpot> getParkingSpots() {
    return parkingSpots;
  }

}
