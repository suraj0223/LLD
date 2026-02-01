package com.snakegame;

public class LeftMovementStrategy implements MovementStrategy{
  @Override
  public Point moveNext(Point point) {
    return new Point(point.getRow(), point.getCol()-1);
  }
}
