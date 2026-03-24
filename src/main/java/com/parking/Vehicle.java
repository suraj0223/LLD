package com.parking;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.design_patterns.factory.VehicleType;

@Getter
@AllArgsConstructor
public class Vehicle {
  private final String licensePlate;
  private final VehicleType vehicleType;
}
