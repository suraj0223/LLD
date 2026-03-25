package com.parking;

public class CardPaymentStrategy implements PaymentStrategy {
  @Override
  public void pay(double amount) {
    System.out.println("Paid $" + String.format("%.2f", amount) + " by CARD.");
  }
}
