## BookMyShow — Movie Ticket Booking System (LLD)

### Requirements

1. A city has multiple theaters.
2. Each theater has multiple screens.
3. Each screen has a fixed seat layout and shows multiple shows of different movies at different times.
4. Each show has per-seat booking state and pricing.
5. User should be able to
   - SEARCH for movies in a city
   - SELECT THEATER → SCREEN → SHOW
   - SEE available seats
   - HOLD seats (temporary lock)
   - CONFIRM booking (after payment)
   - CANCEL booking
6. System should handle concurrent seat bookings safely.

### Entities

1. City
2. Theater
3. Screen
4. Show
5. Movie
6. Seat (physical layout — belongs to Screen)
   - SeatType (ENUM: ECONOMY, PREMIUM, RECLINER)
7. ShowSeat (per-show booking state — belongs to Show)
   - SeatStatus (ENUM: AVAILABLE, BLOCKED, BOOKED)
8. Booking
   - BookingStatus (ENUM: PENDING, CONFIRMED, CANCELLED)
   - PaymentStatus (ENUM: PENDING, PAID, REFUNDED)
9. Ticket (denormalized read-only receipt)
10. User
11. BookingService

### Class Diagram

```
BookingService
+ searchMoviesByCity(city): List<Movie>
+ getTheaters(city, Movie): List<Theater>
+ getShows(theater, Movie): List<Show>
+ getAvailableSeats(show): List<ShowSeat>
+ holdSeats(user, show, showSeats): Booking          // AVAILABLE → BLOCKED (5-min timeout)
+ confirmBooking(booking, paymentInfo): Booking       // BLOCKED → BOOKED
+ cancelBooking(Booking): void                        // BOOKED → AVAILABLE, status → CANCELLED
+ generateTicket(Booking): Ticket

City
- name: String
- theaters: List<Theater>
+ addTheater(Theater): void
+ getTheaters(): List<Theater>
+ removeTheater(Theater): void

Theater
- theaterId: String
- theaterName: String
- city: City
- screens: List<Screen>
+ addScreen(Screen): void
+ removeScreen(Screen): void
+ getScreens(): List<Screen>

Screen
- screenId: String
- theater: Theater
- seats: List<Seat>                    // physical layout, fixed
- shows: List<Show>
+ addShow(Show): void
+ removeShow(Show): void
+ getShows(): List<Show>

Show
- showId: String
- movie: Movie
- screen: Screen
- startTime: LocalDateTime
- endTime: LocalDateTime
- showSeats: List<ShowSeat>
- pricingMap: Map<SeatType, Double>
+ getAvailableSeats(): List<ShowSeat>
+ bookSeats(List<ShowSeat>): boolean

Movie
- movieId: int
- title: String
- duration: int
- genre: String

Seat
- seatId: String
- row: String
- number: int
- seatType: SeatType {ECONOMY, PREMIUM, RECLINER}

ShowSeat
- showSeatId: String
- seat: Seat                           // reference to physical seat
- show: Show
- status: SeatStatus {AVAILABLE, BLOCKED, BOOKED}
- price: double                        // derived from show.pricingMap + seat.seatType
- blockedAt: LocalDateTime             // for timeout expiry
+ isAvailable(): boolean
+ block(): void                        // AVAILABLE → BLOCKED
+ book(): void                         // BLOCKED → BOOKED
+ release(): void                      // BLOCKED/BOOKED → AVAILABLE

Booking
- bookingId: String
- user: User
- show: Show
- showSeats: List<ShowSeat>
- totalAmount: double
- bookingStatus: BookingStatus {PENDING, CONFIRMED, CANCELLED}
- paymentStatus: PaymentStatus {PENDING, PAID, REFUNDED}
- bookingTime: LocalDateTime
+ cancel(): void
+ confirm(): void

Ticket
- ticketId: String
- booking: Booking
- movieTitle: String
- theaterName: String
- screenId: String
- seats: List<Seat>
- showTime: LocalDateTime
- totalAmount: double

User
- userId: String
- name: String
- email: String
- bookings: List<Booking>

Concurrency Flow

holdSeats()        →  showSeat.status: AVAILABLE → BLOCKED  (blockedAt = now)
booking.status: PENDING

confirmBooking()   →  showSeat.status: BLOCKED → BOOKED
booking.status: CONFIRMED
booking.paymentStatus: PAID

cancelBooking()    →  showSeat.status: → AVAILABLE
booking.status: CANCELLED
booking.paymentStatus: REFUNDED

timeoutExpiry()    →  if blockedAt + 5min < now:
showSeat.status: BLOCKED → AVAILABLE
booking.status: CANCELLED
```