package com.ratelimiter.services;

import com.ratelimiter.algorithms.SlidingWindowLimiter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SlidingWindowService {

  private final ConcurrentMap<String, SlidingWindowLimiter> limiters = new ConcurrentHashMap<>();

  public boolean rateLimit(String userId) {

    SlidingWindowLimiter limiter = limiters.computeIfAbsent(
      userId,
      id -> new SlidingWindowLimiter()
    );

    return limiter.tryConsume();
  }
}
