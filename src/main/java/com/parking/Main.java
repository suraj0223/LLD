package com.parking;

public class Main {

  public static void main(String[] args) {
    ParkingLot parkingLot = ParkingLot.getInstance();

    ParkingLevel level1 = new ParkingLevel();
    level1.addParkingSpot(new ParkingSpot(SpotType.COMPACT, null));
    level1.addParkingSpot(new ParkingSpot(SpotType.REGULAR, null));
    level1.addParkingSpot(new ParkingSpot(SpotType.LARGE, null));
    parkingLot.addLevels(level1);

    // Factory pattern: create vehicles by type
    Vehicle compactVehicle = VehicleFactory.createVehicle(VehicleType.COMPACT, "123456");
    Vehicle regularVehicle = VehicleFactory.createVehicle(VehicleType.REGULAR, "678901");

    ParkingSpot found = parkingLot.findParkingSpot(compactVehicle);
    System.out.println("Found spot for compact: " + (found != null));

    Ticket ticket = parkingLot.parkVehicle(regularVehicle);
    if (ticket != null) {
      System.out.println("Parked. Ticket #" + ticket.getTicketId());
    }

    // Strategy pattern: choose payment method when exiting
    PaymentStrategy cashPayment = new CashPaymentStrategy();

    if (ticket != null) {
      parkingLot.removeVehicle(ticket, cashPayment);
      System.out.println("Vehicle removed and payment done.");
    }
  }
}
