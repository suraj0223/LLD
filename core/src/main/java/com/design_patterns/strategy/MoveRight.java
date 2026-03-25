package com.design_patterns.strategy;

public class MoveRight implements MovementStrategy {

  @Override
  public void move() {
    System.out.println("Move Right ... ");
  }
}
