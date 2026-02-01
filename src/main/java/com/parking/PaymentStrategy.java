package com.parking;

/**
 * Strategy pattern: defines how payment is processed. Different implementations (cash, card, UPI).
 */
public interface PaymentStrategy {
  void pay(double amount);
}
