package com.atlassian_cinema_screen;

/*
import java.util.*;



{
  "movies": [
    {
      "title": "Lord Of The Rings",
      "durationInMinutes":  120
    },
    {
      "title": "Back To The Future",
      "durationInMinutes":  90
    }
   ],
  "screenings": [
    {
       "title": "Lord Of The Rings",
       "startTime": 660
     },
     {
       "title": "Lord Of The Rings",
       "startTime": 840
     },
     {
       "title": "Back To The Future",
       "startTime": 1020
     },
     {
       "title": "Lord Of The Rings",
       "startTime": 1200
     }
   ]
 }



public class CinemaScheduler {

    // Cinema operating window: 10:00 AM to 11:00 PM
    private static final int OPEN = 10 * 60;   // 600
    private static final int CLOSE = 23 * 60;  // 1380

    public static class Movie {
        public final String title;
        public final int durationInMinutes;

        public Movie(String title, int durationInMinutes) {
            this.title = title;
            this.durationInMinutes = durationInMinutes;
        }
    }

    public static class Screening {
        public final String title;
        public final int startTime; // minutes from midnight

        public Screening(String title, int startTime) {
            this.title = title;
            this.startTime = startTime;
        }
    }

    public static class MovieSchedule {
        public final List<Movie> movies;
        public final List<Screening> screenings;

        public MovieSchedule(List<Movie> movies, List<Screening> screenings) {
            this.movies = movies;
            this.screenings = screenings;
        }
    }

    private static class Interval {
        int start;
        int end; // exclusive

        Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    // Main function requested
    public static boolean canSchedule(Movie newMovie, MovieSchedule schedule) {
        if (newMovie == null || schedule == null) return false;
        int D = newMovie.durationInMinutes;

        if (D <= 0) return false;
        if (D > (CLOSE - OPEN)) return false;

        // 1) Build title -> duration map
        Map<String, Integer> durationByTitle = new HashMap<>();
        if (schedule.movies != null) {
            for (Movie m : schedule.movies) {
                if (m != null && m.title != null) {
                    durationByTitle.put(m.title, m.durationInMinutes);
                }
            }
        }

        // 2) Convert screenings to intervals [start, end)
        List<Interval> intervals = new ArrayList<>();
        if (schedule.screenings != null) {
            for (Screening s : schedule.screenings) {
                if (s == null || s.title == null) continue;

                Integer dur = durationByTitle.get(s.title);
                if (dur == null) {
                    // Unknown title in screenings -> can't reliably compute end.
                    // In an interview, I'd either:
                    // - treat schedule invalid and return false, OR
                    // - skip/throw. Here: fail fast.
                    return false;
                }

                int start = s.startTime;
                int end = start + dur;

                // If schedule contains screenings completely outside the day window,
                // they don't affect available slots inside [OPEN, CLOSE].
                // But if they overlap with [OPEN, CLOSE], they matter.
                // We'll clamp later via gap scan; still keep original interval.
                intervals.add(new Interval(start, end));
            }
        }

        // If no existing screenings: any start in [OPEN, CLOSE - D] works.
        if (intervals.isEmpty()) return true;

        // 3) Sort intervals by start time
        intervals.sort(Comparator.comparingInt(a -> a.start));

        // 4) Merge overlapping intervals (defensive)
        List<Interval> merged = new ArrayList<>();
        Interval cur = intervals.get(0);
        for (int i = 1; i < intervals.size(); i++) {
            Interval nxt = intervals.get(i);
            if (nxt.start <= cur.end) {
                cur.end = Math.max(cur.end, nxt.end);
            } else {
                merged.add(cur);
                cur = nxt;
            }
        }
        merged.add(cur);

        // 5) Scan gaps within [OPEN, CLOSE]
        // Gap before first
        int prevEnd = OPEN;

        for (Interval in : merged) {
            // consider only the portion that intersects the open-close window
            int start = in.start;
            int end = in.end;

            // If this interval ends before we open, it doesn't block anything.
            if (end <= OPEN) continue;

            // If interval starts after we close, we're done scanning.
            if (start >= CLOSE) break;

            // The blocked interval within our window starts at max(start, OPEN)
            int blockedStart = Math.max(start, OPEN);

            // gap = [prevEnd, blockedStart)
            if (blockedStart - prevEnd >= D) return true;

            // update prevEnd to the end of the blocked part inside our window
            int blockedEnd = Math.min(end, CLOSE);
            prevEnd = Math.max(prevEnd, blockedEnd);
        }

        // Gap after last
        return (CLOSE - prevEnd) >= D;
    }

    // Quick example usage
    public static void main(String[] args) {
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

        System.out.println(canSchedule(new Movie("Any 90-min Movie", 90), schedule));   // true
        System.out.println(canSchedule(new Movie("Any 120-min Movie", 120), schedule)); // false
    }
}

 */