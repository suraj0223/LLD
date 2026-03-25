package com.ratelimiter.algorithms;

import com.ratelimiter.RateLimiterConstants.SlidingWindowConstants;
import java.util.ArrayDeque;
import java.util.Deque;

public class SlidingWindowLimiter {

  private final long windowSizeMs; // e.g., 60_000 ms
  private final int limit;         // max requests allowed in the window

  private final Deque<Long> timestamps = new ArrayDeque<>();

  public SlidingWindowLimiter() {
    this.windowSizeMs = SlidingWindowConstants.WINDOW;
    this.limit = SlidingWindowConstants.LIMIT;
  }

  /**
   * Sliding window logic using timestamp log.
   */
  public synchronized boolean tryConsume() {
    long now = System.currentTimeMillis();

    // Remove timestamps older than (now - windowSize)
    while (!timestamps.isEmpty() && timestamps.peekFirst() <= now - windowSizeMs) {
      timestamps.pollFirst();
    }

    // Now timestamps contains only the requests in the sliding window
    if (timestamps.size() < limit) {
      timestamps.addLast(now);
      return true;
    }

    return false; // too many requests
  }
}
