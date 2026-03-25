package com.snakegame;

public class RightMovementStrategy implements MovementStrategy {
  @Override
  public Point moveNext(Point point) {
    return new Point(point.getRow(), point.getCol()+1);
  }
}
