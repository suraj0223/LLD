package com.parking;

public class Main {

  public static void main(String[] args) {
    ParkingLotService parkingLot = ParkingLotService.getInstance();

    ParkingLevel level1 = new ParkingLevel();
    level1.addParkingSpot(new ParkingSpot("123", SpotType.COMPACT, null));
    level1.addParkingSpot(new ParkingSpot("234", SpotType.REGULAR, null));
    level1.addParkingSpot(new ParkingSpot("456", SpotType.LARGE, null));
    parkingLot.addLevels(level1);

    // Factory pattern: create vehicles by type
    Vehicle compactVehicle = VehicleFactory.createVehicle(VehicleType.COMPACT, "123456");
    Vehicle regularVehicle = VehicleFactory.createVehicle(VehicleType.REGULAR, "678901");

    Ticket ticket1 = parkingLot.parkVehicle(compactVehicle);
    if (ticket1 != null) {
      System.out.println("Parked. Ticket #" + ticket1.getTicketId());
    }

    Ticket ticket2 = parkingLot.parkVehicle(regularVehicle);
    if (ticket2 != null) {
      System.out.println("Parked. Ticket #" + ticket2.getTicketId());
    }

    // Strategy pattern: choose payment method when exiting
    PaymentStrategy cashPayment = new CashPaymentStrategy();

    if (ticket2 != null) {
      parkingLot.removeVehicle(ticket2, cashPayment);
      System.out.println("Vehicle removed and payment done. " + ticket2.getVehicle().getLicensePlate());
    }
  }
}
