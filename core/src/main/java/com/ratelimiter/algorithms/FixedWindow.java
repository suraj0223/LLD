package com.ratelimiter.algorithms;

import com.ratelimiter.RateLimiterConstants.FixedWindowConstants;

public class FixedWindow {

  private long windowEndTimeMs;
  private int requestRemaining;

  public FixedWindow() {

    this.windowEndTimeMs = System.currentTimeMillis() + FixedWindowConstants.TIME_WINDOW; // add a time window from now
    this.requestRemaining = FixedWindowConstants.BUCKET_SIZE;  // start with full bucket
  }

  /**
   * Try to consume 1 token in this fixed window.
   * Thread-safe at per-customer level.
   */
  public synchronized boolean tryConsume() {
    long currentTimeMillis = System.currentTimeMillis();

    // If current window expired → reset window + quota
    if (currentTimeMillis >= windowEndTimeMs) {
      windowEndTimeMs = currentTimeMillis + FixedWindowConstants.TIME_WINDOW;
      requestRemaining = FixedWindowConstants.BUCKET_SIZE;
    }

    if (requestRemaining > 0) {
      requestRemaining--;
      return true;
    } else {
      return false;
    }
  }
}
