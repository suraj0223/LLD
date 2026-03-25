package com.design_patterns.strategy;

public class Main {

  public static void main(String[] args) {
    MovementStrategy movementStrategy = new MoveLeft();

    movementStrategy.move();

  }
}
