package com.ratelimiter;

public class RateLimiterConstants {


  private RateLimiterConstants() {
  }

  public static class FixedWindowConstants {

    public static final int BUCKET_SIZE = 3;
    public static final long TIME_WINDOW = 500;  // in millis

    private FixedWindowConstants() {
    }
  }

  public static class TokenBucketConstants {

    public static final int BUCKET_CAPACITY = 2;
    public static final long REFILL_RATE = 3000; // 1 token refill will happen after every 3000 sec

    private TokenBucketConstants() {
    }
  }

  public static class SlidingWindowConstants {

    public static final long WINDOW = 10_000; // 10 sec minute
    public static final int LIMIT = 2;       // 2 requests per sliding window
  }

  // RATE : BUCKET_SIZE / TIME_WINDOW
}
