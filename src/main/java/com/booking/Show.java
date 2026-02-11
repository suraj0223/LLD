package com.booking;

import java.time.LocalDateTime;
import java.util.List;

public class Show {
    private String showId;
    private Movie movie;
    private LocalDateTime startTime;
    private List<ShowSeat> showSeats;

    public Show(String showId, Movie movie, LocalDateTime startTime, List<ShowSeat> showSeats) {
        this.showId = showId;
        this.movie = movie;
        this.startTime = startTime;
        this.showSeats = showSeats;
    }

    public String getShowId() { return showId; }
    public Movie getMovie() { return movie; }
    public LocalDateTime getStartTime() { return startTime; }
    public List<ShowSeat> getShowSeats() { return showSeats; }
}
