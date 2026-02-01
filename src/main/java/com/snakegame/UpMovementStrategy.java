package com.snakegame;

public class UpMovementStrategy implements MovementStrategy {
  @Override
  public Point moveNext(Point point) {
    return new Point(point.getRow() - 1, point.getCol());
  }
}
