package com.ratelimiter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ratelimiter.services.FixedWindowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FixedWindowServiceTest {

  private FixedWindowService fixedWindowService;

  @BeforeEach
  void setUp() {
    fixedWindowService = new FixedWindowService();
  }

  @Test
  void testBasicRateLimitWithinBucketSize() {
    String customerId = "customer1";

    // Should allow 3 requests (BUCKET_SIZE = 3)
    assertTrue(fixedWindowService.rateLimit(customerId));
    assertTrue(fixedWindowService.rateLimit(customerId));
    assertTrue(fixedWindowService.rateLimit(customerId));
  }

  @Test
  void testRateLimitExceedsBucketSize() {
    String customerId = "customer2";

    // Consume all tokens in bucket
    assertTrue(fixedWindowService.rateLimit(customerId));
    assertTrue(fixedWindowService.rateLimit(customerId));
    assertTrue(fixedWindowService.rateLimit(customerId));

    // Next request should be rejected (bucket exhausted)
    assertFalse(fixedWindowService.rateLimit(customerId));
  }

  @Test
  void testWindowResetAfterTimeWindow() throws InterruptedException {
    String customerId = "customer3";

    // Consume all tokens
    assertTrue(fixedWindowService.rateLimit(customerId));
    assertTrue(fixedWindowService.rateLimit(customerId));
    assertTrue(fixedWindowService.rateLimit(customerId));
    assertFalse(fixedWindowService.rateLimit(customerId)); // Should be rejected

    // Wait for window to reset (TIME_WINDOW = 500ms)
    Thread.sleep(600);

    // After window reset, should allow requests again
    assertTrue(fixedWindowService.rateLimit(customerId));
    assertTrue(fixedWindowService.rateLimit(customerId));
    assertTrue(fixedWindowService.rateLimit(customerId));
  }

  @Test
  void testMultipleCustomersIsolation() {
    String customer1 = "customer1";
    String customer2 = "customer2";

    // Both customers should get their own window
    assertTrue(fixedWindowService.rateLimit(customer1));
    assertTrue(fixedWindowService.rateLimit(customer1));
    assertTrue(fixedWindowService.rateLimit(customer1));

    // Customer2 should still have full capacity
    assertTrue(fixedWindowService.rateLimit(customer2));
    assertTrue(fixedWindowService.rateLimit(customer2));
    assertTrue(fixedWindowService.rateLimit(customer2));

    // Customer1 should be rate limited
    assertFalse(fixedWindowService.rateLimit(customer1));

    // Customer2 should also be exhausted now
    assertFalse(fixedWindowService.rateLimit(customer2));
  }

  @Test
  void testConcurrentRequests() {
    String customerId = "customer4";

    // Simulate rapid requests
    int allowedCount = 0;
    int deniedCount = 0;

    for (int i = 0; i < 5; i++) {
      if (fixedWindowService.rateLimit(customerId)) {
        allowedCount++;
      } else {
        deniedCount++;
      }
    }

    // Should allow exactly 3 requests
    assertEquals(3, allowedCount, "Expected 3 allowed requests, got " + allowedCount);
    assertEquals(2, deniedCount, "Expected 2 denied requests, got " + deniedCount);
  }

  @Test
  void testSameCustomerMultipleCalls() {
    String customerId = "customer5";

    // First 3 should succeed
    for (int i = 0; i < 3; i++) {
      assertTrue(fixedWindowService.rateLimit(customerId),
        "Request " + (i + 1) + " should be allowed");
    }

    // 4th should fail
    assertFalse(fixedWindowService.rateLimit(customerId),
      "Request 4 should be rate limited");
  }

  @Test
  void testWindowDoesNotResetBeforeTimeExpires() throws InterruptedException {
    String customerId = "customer6";

    // Consume all tokens
    assertTrue(fixedWindowService.rateLimit(customerId));
    assertTrue(fixedWindowService.rateLimit(customerId));
    assertTrue(fixedWindowService.rateLimit(customerId));
    assertFalse(fixedWindowService.rateLimit(customerId));

    // Wait less than TIME_WINDOW (500ms) to ensure window hasn't expired
    Thread.sleep(300);

    // Should still be rate limited
    assertFalse(fixedWindowService.rateLimit(customerId),
      "Should still be rate limited before window expires");
  }

  @Test
  void testPartialWindowUsage() throws InterruptedException {
    String customerId = "customer7";

    // Use only 2 tokens
    assertTrue(fixedWindowService.rateLimit(customerId));
    assertTrue(fixedWindowService.rateLimit(customerId));

    // Wait for window to reset (TIME_WINDOW = 500ms)
    Thread.sleep(600);

    // Should have full bucket again (3 tokens)
    assertTrue(fixedWindowService.rateLimit(customerId));
    assertTrue(fixedWindowService.rateLimit(customerId));
    assertTrue(fixedWindowService.rateLimit(customerId));
    assertFalse(fixedWindowService.rateLimit(customerId));
  }
}

