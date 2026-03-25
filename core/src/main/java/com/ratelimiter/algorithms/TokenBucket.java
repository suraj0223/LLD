package com.ratelimiter.algorithms;


import com.ratelimiter.RateLimiterConstants.TokenBucketConstants;

public class TokenBucket {

  private final int bucketCapacity;
  private final long refillRate;

  private long lastRefillMs;               // last refill time in millis
  private int tokens;                   // current tokens (fractional NOT allowed)


  public TokenBucket() {
    this.bucketCapacity = TokenBucketConstants.BUCKET_CAPACITY;
    this.refillRate = TokenBucketConstants.REFILL_RATE;

    this.tokens = TokenBucketConstants.BUCKET_CAPACITY; // initial full cap
    this.lastRefillMs = System.currentTimeMillis();
  }

  public synchronized boolean tryConsume() {

    refillBucket();

    if (this.tokens > 0.0) {
      this.tokens = this.tokens - 1;
      return true;
    }
    return false;
  }


  // Refill logic
  private void refillBucket() {
    long currentTime = System.currentTimeMillis();
    long timeSpend = currentTime - lastRefillMs;

    if (timeSpend <= 0) {
      return;
    }

    // REFILL_RATE represents milliseconds per token (e.g., 1000 = 1 token per second)
    // So tokens refill at: timeSpend / refillRate
    int tokenToRefill = (int) timeSpend / (int) refillRate;

    if (tokenToRefill > 0.0) {
      this.tokens = Math.min(this.bucketCapacity, tokenToRefill + this.tokens);
      this.lastRefillMs = currentTime;
    }
  }

}
