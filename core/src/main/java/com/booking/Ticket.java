package com.booking;

import java.time.LocalDateTime;
import java.util.List;

public class Ticket {
    private String ticketId;
    private String bookingId;
    private String movieTitle;
    private String theaterName;
    private String screenId;
    private LocalDateTime showTime;
    private List<String> seatLabels;
    private double totalAmount;

    public String getTicketId() { return ticketId; }
    public String getBookingId() { return bookingId; }
    public String getMovieTitle() { return movieTitle; }
    public String getTheaterName() { return theaterName; }
    public String getScreenId() { return screenId; }
    public LocalDateTime getShowTime() { return showTime; }
    public List<String> getSeatLabels() { return seatLabels; }
    public double getTotalAmount() { return totalAmount; }

    public void setTicketId(String id) { this.ticketId = id; }
    public void setBookingId(String id) { this.bookingId = id; }
    public void setMovieTitle(String t) { this.movieTitle = t; }
    public void setTheaterName(String t) { this.theaterName = t; }
    public void setScreenId(String s) { this.screenId = s; }
    public void setShowTime(LocalDateTime t) { this.showTime = t; }
    public void setSeatLabels(List<String> s) { this.seatLabels = s; }
    public void setTotalAmount(double a) { this.totalAmount = a; }
}
