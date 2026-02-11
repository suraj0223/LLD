package com.booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class BookingService {

    private final ConcurrentHashMap<String, ReentrantLock> showLocks = new ConcurrentHashMap<>();

    private ReentrantLock getLock(Show show) {
        return showLocks.computeIfAbsent(show.getShowId(), k -> new ReentrantLock(true));
    }

    /**
     * STEP 1: Hold seats — AVAILABLE → BLOCKED
     *
     * Temporarily blocks the requested seats for 5 minutes
     * so the user can complete payment.
     */
    public Booking holdSeats(User user, Show show, List<ShowSeat> showSeats) {
        ReentrantLock lock = getLock(show);
        lock.lock();
        try {
            // 1. Cleanup: release any expired holds on this show
            for (ShowSeat ss : show.getShowSeats()) {
                if (ss.isHoldExpired()) {
                    ss.release();
                }
            }

            // 2. Validate all requested seats are available
            for (ShowSeat ss : showSeats) {
                if (!ss.isAvailable()) {
                    throw new IllegalStateException(
                        "Seat " + ss.getSeat().getSeatId() + " is not available");
                }
            }

            // 3. Block all seats
            for (ShowSeat ss : showSeats) {
                ss.block();
            }

            // 4. Create PENDING booking
            double totalAmount = showSeats.stream()
                .mapToDouble(ShowSeat::getPrice)
                .sum();

            Booking booking = new Booking();
            booking.setBookingId(UUID.randomUUID().toString());
            booking.setUser(user);
            booking.setShow(show);
            booking.setShowSeats(showSeats);
            booking.setTotalAmount(totalAmount);
            booking.setBookingStatus(BookingStatus.PENDING);
            booking.setPaymentStatus(PaymentStatus.PENDING);
            booking.setBookingTime(LocalDateTime.now());

            return booking;
        } finally {
            lock.unlock();
        }
    }

    /**
     * STEP 2: Confirm booking — BLOCKED → BOOKED
     *
     * Called after successful payment.
     */
    public Booking confirmBooking(Booking booking, String paymentInfo) {
        Show show = booking.getShow();
        ReentrantLock lock = getLock(show);
        lock.lock();
        try {
            // 1. Validate booking is PENDING
            if (booking.getBookingStatus() != BookingStatus.PENDING) {
                throw new IllegalStateException(
                    "Booking " + booking.getBookingId() + " is not in PENDING state");
            }

            // 2. Check if any hold has expired
            for (ShowSeat ss : booking.getShowSeats()) {
                if (ss.isHoldExpired()) {
                    // Hold expired — release all seats and cancel booking
                    for (ShowSeat seat : booking.getShowSeats()) {
                        if (seat.getStatus() == SeatStatus.BLOCKED) {
                            seat.release();
                        }
                    }
                    booking.setBookingStatus(BookingStatus.CANCELLED);
                    booking.setPaymentStatus(PaymentStatus.REFUNDED);
                    throw new IllegalStateException(
                        "Hold expired for booking " + booking.getBookingId());
                }
            }

            // 3. Move all seats BLOCKED → BOOKED
            for (ShowSeat ss : booking.getShowSeats()) {
                ss.book();
            }

            // 4. Confirm booking
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            booking.setPaymentStatus(PaymentStatus.PAID);

            return booking;
        } finally {
            lock.unlock();
        }
    }

    /**
     * STEP 3: Cancel booking
     *
     * Releases all seats back to AVAILABLE and marks booking CANCELLED.
     * Works for both PENDING (held) and CONFIRMED (paid) bookings.
     */
    public void cancelBooking(Booking booking) {
        Show show = booking.getShow();
        ReentrantLock lock = getLock(show);
        lock.lock();
        try {
            if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
                throw new IllegalStateException(
                    "Booking " + booking.getBookingId() + " is already cancelled");
            }

            // Release all seats back to AVAILABLE
            for (ShowSeat ss : booking.getShowSeats()) {
                ss.release();
            }

            booking.setBookingStatus(BookingStatus.CANCELLED);

            // Refund if payment was made
            if (booking.getPaymentStatus() == PaymentStatus.PAID) {
                booking.setPaymentStatus(PaymentStatus.REFUNDED);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * STEP 4: Generate ticket (denormalized read-only receipt)
     *
     * Only works for CONFIRMED bookings.
     */
    public Ticket generateTicket(Booking booking) {
        if (booking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException(
                "Cannot generate ticket for non-confirmed booking");
        }

        List<String> seatLabels = booking.getShowSeats().stream()
            .map(ss -> ss.getSeat().getRow() + ss.getSeat().getNumber())
            .collect(Collectors.toList());

        Ticket ticket = new Ticket();
        ticket.setTicketId(UUID.randomUUID().toString());
        ticket.setBookingId(booking.getBookingId());
        ticket.setMovieTitle(booking.getShow().getMovie().getTitle());
        ticket.setShowTime(booking.getShow().getStartTime());
        ticket.setSeatLabels(seatLabels);
        ticket.setTotalAmount(booking.getTotalAmount());

        return ticket;
    }
}
