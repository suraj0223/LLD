package com.parking;

public class UpiPaymentStrategy implements PaymentStrategy {
  @Override
  public void pay(double amount) {
    System.out.println("Paid $" + String.format("%.2f", amount) + " by UPI.");
  }
}
