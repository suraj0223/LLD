package com.design_patterns.strategy;

public class MoveLeft implements MovementStrategy {

  @Override
  public void move() {
    System.out.println("Moving left ...");
  }
}
