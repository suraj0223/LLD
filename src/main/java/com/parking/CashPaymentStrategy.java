package com.parking;

public class CashPaymentStrategy implements PaymentStrategy {
  @Override
  public void pay(double amount) {
    System.out.println("Paid $" + String.format("%.2f", amount) + " by CASH.");
  }
}
