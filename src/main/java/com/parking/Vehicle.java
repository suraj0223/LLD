package com.parking;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Vehicle {
  private final String licensePlate;
  private final VehicleType vehicleType;
}
