package com.snakegame;

public class DownMovementStrategy implements MovementStrategy{

  @Override
  public Point moveNext(Point point) {
    return new Point(point.getRow() + 1, point.getCol());
  }
}
