package com.atlassian_cinema_screen;

import java.util.List;

public class Main {

  public static void main(String[] args) {
    CinemaScheduler cinemaScheduler = new CinemaScheduler();

    List<Movie> movies = List.of(
      new Movie("Lord Of The Rings", 120),
      new Movie("Back To The Future", 90)
    );

    List<Screening> screenings = List.of(
      new Screening("Lord Of The Rings", 660),
      new Screening("Lord Of The Rings", 840),
      new Screening("Back To The Future", 1020),
      new Screening("Lord Of The Rings", 1200)
    );

    MovieSchedule schedule = new MovieSchedule(movies, screenings);

    System.out.println(cinemaScheduler.canSchedule(new Movie("Any 90-min Movie", 90), schedule));   // true
    System.out.println(cinemaScheduler.canSchedule(new Movie("Any 120-min Movie", 120), schedule)); // false
  }
}
