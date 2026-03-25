package com.booking;

public class Seat {
    private String seatId;
    private String row;
    private int number;
    private SeatType seatType;

    public Seat(String seatId, String row, int number, SeatType seatType) {
        this.seatId = seatId;
        this.row = row;
        this.number = number;
        this.seatType = seatType;
    }

    public String getSeatId() { return seatId; }
    public String getRow() { return row; }
    public int getNumber() { return number; }
    public SeatType getSeatType() { return seatType; }
}
