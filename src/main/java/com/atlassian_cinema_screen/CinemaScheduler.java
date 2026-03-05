package com.atlassian_cinema_screen;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CinemaScheduler {

  // Cinema operating window: 10:00 AM to 11:00 PM
  private static final int OPEN_TIME_MINUTES = 10 * 60;  // 600
  private static final int CLOSE_TIME_MINUTES = 23 * 60; // 1380

  public boolean canSchedule(Movie movie, MovieSchedule schedule) {
    if (movie == null || schedule == null) {
      return false;
    }

    int movieDuration = movie.durationInMinutes();
    int operatingWindowMinutes = CLOSE_TIME_MINUTES - OPEN_TIME_MINUTES;

    if (movieDuration <= 0 || movieDuration > operatingWindowMinutes) {
      return false;
    }

    Map<String, Integer> durationByTitle = buildDurationByTitle(schedule.movies());

    List<Interval> occupiedSlots = buildOccupiedIntervals(schedule.screenings(), durationByTitle);

    // No existing screenings => any start in [OPEN, CLOSE - duration] works
    if (occupiedSlots.isEmpty()) {
      return true;
    }

    occupiedSlots.sort(Comparator.comparingInt(slot -> slot.getStart()));

    List<Interval> mergedOccupiedSlots = mergeOverlappingIntervals(occupiedSlots);

    return hasGapForDuration(mergedOccupiedSlots, movieDuration);
  }

  private Map<String, Integer> buildDurationByTitle(List<Movie> movies) {
    Map<String, Integer> durationByTitle = new HashMap<>();
    if (movies == null) {
      return durationByTitle;
    }

    for (Movie movie : movies) {
      if (movie == null || movie.title() == null) {
        continue;
      }
      durationByTitle.put(movie.title(), movie.durationInMinutes());
    }

    return durationByTitle;
  }

  private List<Interval> buildOccupiedIntervals(List<Screening> screenings,
    Map<String, Integer> durationByTitle) {
    List<Interval> occupiedSlots = new ArrayList<>();
    if (screenings == null) {
      return occupiedSlots;
    }

    for (Screening screening : screenings) {
      if (screening == null || screening.getTitle() == null) {
        continue;
      }

      Integer movieDuration = durationByTitle.get(screening.getTitle());
      if (movieDuration == null) {
        // Screening references a movie not present in "movies" list -> schedule is invalid
        return List.of(); // or throw IllegalArgumentException; choose based on contract
      }

      int screeningStart = screening.getStartTime();
      int screeningEndExclusive = screeningStart + movieDuration;

      occupiedSlots.add(new Interval(screeningStart, screeningEndExclusive));
    }

    return occupiedSlots;
  }

  private List<Interval> mergeOverlappingIntervals(List<Interval> sortedIntervals) {
    List<Interval> merged = new ArrayList<>();
    Interval current = sortedIntervals.getFirst();

    for (int i = 1; i < sortedIntervals.size(); i++) {
      Interval next = sortedIntervals.get(i);

      if (next.getStart() <= current.getEnd()) {
        current.setEnd(Math.max(current.getEnd(), next.getEnd()));
      } else {
        merged.add(current);
        current = next;
      }
    }

    merged.add(current);
    return merged;
  }

  private boolean hasGapForDuration(List<Interval> mergedOccupiedSlots, int requiredDurationMinutes) {
    int lastOccupiedEnd = OPEN_TIME_MINUTES;

    for (Interval occupied : mergedOccupiedSlots) {
      int occupiedStart = occupied.getStart();
      int occupiedEnd = occupied.getEnd();

      // Interval ends before opening => doesn't block anything inside the window
      if (occupiedEnd <= OPEN_TIME_MINUTES) {
        continue;
      }

      // Interval starts after closing => no more relevant blocks
      if (occupiedStart >= CLOSE_TIME_MINUTES) {
        break;
      }

      int blockedStartWithinWindow = Math.max(occupiedStart, OPEN_TIME_MINUTES);

      // Free gap is [lastOccupiedEnd, blockedStartWithinWindow)
      if (blockedStartWithinWindow - lastOccupiedEnd >= requiredDurationMinutes) {
        return true;
      }

      int blockedEndWithinWindow = Math.min(occupiedEnd, CLOSE_TIME_MINUTES);
      lastOccupiedEnd = Math.max(lastOccupiedEnd, blockedEndWithinWindow);
    }

    // Final free gap is [lastOccupiedEnd, CLOSE)
    return (CLOSE_TIME_MINUTES - lastOccupiedEnd) >= requiredDurationMinutes;
  }
}
