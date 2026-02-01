package com.parking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ParkingSpot {
  private SpotType spotType;
  private Vehicle vehicle;

  public boolean isAvailable() {
    return this.vehicle == null;
  }

  public boolean isOccupied() {
    return this.vehicle != null;
  }

  /** Vehicle fits if spot's type is same or larger (e.g. LARGE spot can fit COMPACT). */
  public boolean canFitVehicle(Vehicle vehicle) {
    SpotType required = SpotType.valueOf(vehicle.getVehicleType().name());
    return this.spotType.compareTo(required) >= 0;
  }

  public boolean assignVehicle(Vehicle vehicle) {
    if (this.isAvailable() && this.canFitVehicle(vehicle)) {
      this.vehicle = vehicle;
      return true;
    }
    return false;
  }

  public void removeVehicle() {
    this.vehicle = null;
  }

  public Vehicle getVehicle() {
    return this.vehicle;
  }

}
