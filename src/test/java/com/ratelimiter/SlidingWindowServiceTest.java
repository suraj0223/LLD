package com.ratelimiter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ratelimiter.services.SlidingWindowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SlidingWindowServiceTest {

  private SlidingWindowService slidingWindowService;

  @BeforeEach
  void setUp() {
    slidingWindowService = new SlidingWindowService();
  }

  @Test
  void testBasicRateLimitWithinLimit() throws InterruptedException {
    String userId = "user1";

    // Should allow 2 requests (LIMIT = 2)
    assertTrue(slidingWindowService.rateLimit(userId), "Request 1 should be allowed");
    Thread.sleep(10); // Small delay to ensure different timestamps
    assertTrue(slidingWindowService.rateLimit(userId), "Request 2 should be allowed");
  }

  @Test
  void testRateLimitExceedsLimit() throws InterruptedException {
    String userId = "user2";

    // Consume all allowed requests (LIMIT = 2)
    assertTrue(slidingWindowService.rateLimit(userId));
    Thread.sleep(10);
    assertTrue(slidingWindowService.rateLimit(userId));

    // Next request should be rejected (limit exceeded)
    assertFalse(slidingWindowService.rateLimit(userId), "Request 3 should be rate limited");
  }

  @Test
  void testSlidingWindowBehavior() throws InterruptedException {
    String userId = "user3";

    // Make 2 requests
    assertTrue(slidingWindowService.rateLimit(userId));
    Thread.sleep(10);
    assertTrue(slidingWindowService.rateLimit(userId));

    // Wait for 31 seconds (WINDOW = 30_000 ms) so old requests expire
    Thread.sleep(11_000);

    // Should be able to make 2 more requests (old ones expired)
    assertTrue(slidingWindowService.rateLimit(userId), "Request 1 after window slide should be allowed");
    Thread.sleep(10);
    assertTrue(slidingWindowService.rateLimit(userId), "Request 2 after window slide should be allowed");

    // Should be rate limited now
    assertFalse(slidingWindowService.rateLimit(userId));
  }

  @Test
  void testMultipleUsersIsolation() throws InterruptedException {
    String user1 = "user1";
    String user2 = "user2";

    // Both users should get their own sliding window (LIMIT = 2 each)
    assertTrue(slidingWindowService.rateLimit(user1));
    Thread.sleep(10);
    assertTrue(slidingWindowService.rateLimit(user1));

    // User2 should still have full capacity
    assertTrue(slidingWindowService.rateLimit(user2), "User2 request 1 should be allowed");
    Thread.sleep(10);
    assertTrue(slidingWindowService.rateLimit(user2), "User2 request 2 should be allowed");

    // User1 should be rate limited
    assertFalse(slidingWindowService.rateLimit(user1), "User1 should be rate limited after 2 requests");

    // User2 should also be rate limited now
    assertFalse(slidingWindowService.rateLimit(user2), "User2 should be rate limited after 2 requests");
  }

  @Test
  void testConcurrentRequests() throws InterruptedException {
    String userId = "user4";

    // Simulate rapid requests
    int allowedCount = 0;
    int deniedCount = 0;

    for (int i = 0; i < 5; i++) {
      if (slidingWindowService.rateLimit(userId)) {
        allowedCount++;
      } else {
        deniedCount++;
      }
      Thread.sleep(10); // Small delay to ensure different timestamps
    }

    // Should allow exactly 2 requests (LIMIT = 2)
    assertEquals(2, allowedCount, "Expected 2 allowed requests, got " + allowedCount);
    assertEquals(3, deniedCount, "Expected 3 denied requests, got " + deniedCount);
  }

  @Test
  void testSameUserMultipleCalls() throws InterruptedException {
    String userId = "user5";

    // First 2 should succeed
    assertTrue(slidingWindowService.rateLimit(userId), "Request 1 should be allowed");
    Thread.sleep(10);
    assertTrue(slidingWindowService.rateLimit(userId), "Request 2 should be allowed");

    // 3rd should fail
    assertFalse(slidingWindowService.rateLimit(userId), "Request 3 should be rate limited");
  }

  @Test
  void testWindowDoesNotSlideBeforeTimeExpires() throws InterruptedException {
    String userId = "user6";

    // Consume all allowed requests
    assertTrue(slidingWindowService.rateLimit(userId));
    Thread.sleep(10);
    assertTrue(slidingWindowService.rateLimit(userId));
    assertFalse(slidingWindowService.rateLimit(userId));

    // Wait less than WINDOW (30 seconds) to ensure window hasn't slid
    Thread.sleep(15_000); // 15 seconds

    // Should still be rate limited (oldest request still within window)
    assertFalse(slidingWindowService.rateLimit(userId), "Should still be rate limited before window slides");
  }

  @Test
  void testPartialWindowUsage() throws InterruptedException {
    String userId = "user7";

    // Use only 1 request
    assertTrue(slidingWindowService.rateLimit(userId));

    // Wait for window to slide (31 seconds)
    Thread.sleep(11_000);

    // Should have full capacity again (2 requests)
    assertTrue(slidingWindowService.rateLimit(userId), "Request 1 after window slide should be allowed");
    Thread.sleep(10);
    assertTrue(slidingWindowService.rateLimit(userId), "Request 2 after window slide should be allowed");
    assertFalse(slidingWindowService.rateLimit(userId));
  }

  @Test
  void testGradualRequestExpiration() throws InterruptedException {
    String userId = "user8";

    // Make 2 requests with small delays
    assertTrue(slidingWindowService.rateLimit(userId));
    Thread.sleep(100);
    assertTrue(slidingWindowService.rateLimit(userId));

    // Should be rate limited
    assertFalse(slidingWindowService.rateLimit(userId));

    // Wait for first request to expire (31 seconds from first request)
    Thread.sleep(11_000);

    // Should be able to make 1 more request (oldest expired)
    assertTrue(slidingWindowService.rateLimit(userId), "Should allow 1 request after oldest expires");

    // Should still be rate limited (1 request still in window)
    assertFalse(slidingWindowService.rateLimit(userId));
  }

  @Test
  void testRequestsSpreadOverWindow() throws InterruptedException {
    String userId = "user9";

    // Make 2 requests
    assertTrue(slidingWindowService.rateLimit(userId));
    Thread.sleep(10);
    assertTrue(slidingWindowService.rateLimit(userId));

    // Should be at limit (2 requests)
    assertFalse(slidingWindowService.rateLimit(userId));

    // Wait 31 seconds (WINDOW = 30_000 ms) so first request expires
    Thread.sleep(11_000);

    // First request expired, should allow 1 more
    assertTrue(slidingWindowService.rateLimit(userId), "Request 1 after first request expired should be allowed");

    // Should be rate limited again (2 requests in window now)
    assertFalse(slidingWindowService.rateLimit(userId));
  }

  @Test
  void testBurstRequestsThenWait() throws InterruptedException {
    String userId = "user10";

    // Burst: make all 2 requests rapidly
    assertTrue(slidingWindowService.rateLimit(userId));
    Thread.sleep(10);
    assertTrue(slidingWindowService.rateLimit(userId));
    assertFalse(slidingWindowService.rateLimit(userId));

    // Wait for entire window to slide (31 seconds)
    Thread.sleep(11_000);

    // Should be able to burst again
    assertTrue(slidingWindowService.rateLimit(userId), "Burst request 1 after window slide should be allowed");
    Thread.sleep(10);
    assertTrue(slidingWindowService.rateLimit(userId), "Burst request 2 after window slide should be allowed");
    assertFalse(slidingWindowService.rateLimit(userId));
  }
}

