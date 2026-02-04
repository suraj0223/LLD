## Entities
- ParkingLot
- ParkingLevel
- ParkingSpot
- Vehicle
- Ticket
- PaymentStrategy (and implementations)
- VehicleFactory

## Design Patterns
- **Singleton**: ParkingLot (single instance via `getInstance()`)
- **Strategy**: Payment (CashPaymentStrategy, CardPaymentStrategy, UpiPaymentStrategy)
- **Factory**: Vehicle creation by type (VehicleFactory)

---

## Classes

### ParkingLot
- [-] List&lt;ParkingLevel&gt; parkingLevels
- [-] static ParkingLot INSTANCE
- [+] static getInstance() : ParkingLot
- [+] addLevels(ParkingLevel level) : void
- [+] findParkingSpot(Vehicle vehicle) : ParkingSpot
- [+] parkVehicle(Vehicle vehicle) : Ticket
- [+] freeSpot(Ticket ticket) : void
- [+] removeVehicle(Ticket ticket, PaymentStrategy paymentStrategy) : void

### ParkingLevel
- [-] List&lt;ParkingSpot&gt; parkingSpots
- [+] getParkingSpot(Vehicle vehicle) : ParkingSpot
- [+] addParkingSpot(ParkingSpot spot) : void
- [+] removeParkingSpot(ParkingSpot spot) : void
- [+] getParkingSpots() : List&lt;ParkingSpot&gt;

### ParkingSpot
- [-] SpotType spotType
- [-] Vehicle vehicle (null when available; occupancy derived from this)
- [+] isAvailable() : boolean
- [+] isOccupied() : boolean
- [+] canFitVehicle(Vehicle vehicle) : boolean
- [+] assignVehicle(Vehicle vehicle) : boolean
- [+] removeVehicle() : void
- [+] getVehicle() : Vehicle

### Vehicle
- [-] String licensePlate
- [-] VehicleType vehicleType

### Ticket
- [-] int ticketId
- [-] Vehicle vehicle
- [-] ParkingSpot spot
- [-] LocalDateTime entryTime
- [-] LocalDateTime exitTime
- [+] Ticket(Vehicle vehicle, ParkingSpot spot)

### PaymentStrategy (interface)
- [+] pay(double amount) : void

### CashPaymentStrategy / CardPaymentStrategy / UpiPaymentStrategy
- Implement PaymentStrategy

### VehicleFactory
- [+] createVehicle(VehicleType type, String licensePlate) : Vehicle

---

## Enums

### SpotType
- COMPACT
- REGULAR
- LARGE

### VehicleType
- COMPACT
- REGULAR
- LARGE
