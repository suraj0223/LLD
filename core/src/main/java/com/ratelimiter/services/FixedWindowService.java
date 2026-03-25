package com.ratelimiter.services;

import com.ratelimiter.algorithms.FixedWindow;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FixedWindowService {

  private final ConcurrentMap<String, FixedWindow> limiters =
    new ConcurrentHashMap<>();

  public boolean rateLimit(String customerId) {

    FixedWindow limiter = limiters.computeIfAbsent(
      customerId,
      id -> new FixedWindow()
    );

    return limiter.tryConsume();
  }
}
