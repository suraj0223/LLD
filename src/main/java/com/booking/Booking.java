package com.booking;

import java.time.LocalDateTime;
import java.util.List;

public class Booking {
    private String bookingId;
    private User user;
    private Show show;
    private List<ShowSeat> showSeats;
    private double totalAmount;
    private BookingStatus bookingStatus;
    private PaymentStatus paymentStatus;
    private LocalDateTime bookingTime;

    public String getBookingId() { return bookingId; }
    public User getUser() { return user; }
    public Show getShow() { return show; }
    public List<ShowSeat> getShowSeats() { return showSeats; }
    public double getTotalAmount() { return totalAmount; }
    public BookingStatus getBookingStatus() { return bookingStatus; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public LocalDateTime getBookingTime() { return bookingTime; }

    public void setBookingId(String id) { this.bookingId = id; }
    public void setUser(User user) { this.user = user; }
    public void setShow(Show show) { this.show = show; }
    public void setShowSeats(List<ShowSeat> seats) { this.showSeats = seats; }
    public void setTotalAmount(double amt) { this.totalAmount = amt; }
    public void setBookingStatus(BookingStatus s) { this.bookingStatus = s; }
    public void setPaymentStatus(PaymentStatus s) { this.paymentStatus = s; }
    public void setBookingTime(LocalDateTime t) { this.bookingTime = t; }
}
