package com.ratelimiter;

import com.ratelimiter.services.TokenBucketService;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    TokenBucketService rateLimiterService = new TokenBucketService();

    // With BUCKET_CAPACITY = 2 and REFILL_RATE = 1000 (1 token per second),
    // rapid calls should only allow 2 requests before rate limiting
    for (int i = 0; i < 10; i++) {
      System.out.println(rateLimiterService.rateLimit("Customer 1")
        + " : Customer 1" + " : " + System.currentTimeMillis());

      Thread.sleep(1000);
    }
  }
}
