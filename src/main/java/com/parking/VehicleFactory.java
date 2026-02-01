package com.parking;

/**
 * Factory for creating Vehicle instances by VehicleType.
 */
public class VehicleFactory {

  public static Vehicle createVehicle(VehicleType type, String licensePlate) {
    if (type == null || licensePlate == null || licensePlate.isBlank()) {
      throw new IllegalArgumentException("VehicleType and licensePlate must be non-null and non-empty.");
    }
    return switch (type) {
      case COMPACT -> new Vehicle(licensePlate.trim(), VehicleType.COMPACT);
      case REGULAR -> new Vehicle(licensePlate.trim(), VehicleType.REGULAR);
      case LARGE -> new Vehicle(licensePlate.trim(), VehicleType.LARGE);
    };
  }
}
