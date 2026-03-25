package com.ratelimiter.services;

import com.ratelimiter.algorithms.TokenBucket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TokenBucketService {

  private final ConcurrentMap<String, TokenBucket> limiters = new ConcurrentHashMap<>();

  public boolean rateLimit(String customerId) {

    TokenBucket bucket = limiters.computeIfAbsent(
      customerId,
      id -> new TokenBucket()
    );

    return bucket.tryConsume();
  }
}
