package com.booking;

import java.time.LocalDateTime;
import java.util.UUID;

public class ShowSeat {
    private static final int HOLD_TIMEOUT_MINUTES = 5;

    private String showSeatId;
    private Seat seat;
    private SeatStatus status;
    private double price;
    private LocalDateTime blockedAt;

    public ShowSeat(Seat seat, double price) {
        this.showSeatId = UUID.randomUUID().toString();
        this.seat = seat;
        this.status = SeatStatus.AVAILABLE;
        this.price = price;
    }

    public String getShowSeatId() { return showSeatId; }
    public Seat getSeat() { return seat; }
    public SeatStatus getStatus() { return status; }
    public double getPrice() { return price; }
    public LocalDateTime getBlockedAt() { return blockedAt; }
    public boolean isAvailable() { return status == SeatStatus.AVAILABLE; }

    public void block() {
        if (status != SeatStatus.AVAILABLE)
            throw new IllegalStateException("Seat " + seat.getSeatId() + " is not available");
        this.status = SeatStatus.BLOCKED;
        this.blockedAt = LocalDateTime.now();
    }

    public void book() {
        if (status != SeatStatus.BLOCKED)
            throw new IllegalStateException("Seat " + seat.getSeatId() + " is not blocked");
        this.status = SeatStatus.BOOKED;
    }

    public void release() {
        this.status = SeatStatus.AVAILABLE;
        this.blockedAt = null;
    }

    public boolean isHoldExpired() {
        return status == SeatStatus.BLOCKED
            && blockedAt != null
            && blockedAt.plusMinutes(HOLD_TIMEOUT_MINUTES).isBefore(LocalDateTime.now());
    }
}
