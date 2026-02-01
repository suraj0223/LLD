package com.snakegame;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class SnakeGameImpl implements SnakeGame{
  private int row;
  private int col;
  private final Deque<Point> snake;
  private final Set<Point> snakeBody;
  private Direction currentDirection;
  private boolean isGameOver;
  private int moveCount;


  public SnakeGameImpl(int row, int col) {
    this.row = row;
    this.col = col;

    this.snake = new ArrayDeque<>();
    this.snakeBody = new HashSet<>();
    this.currentDirection = Direction.R;
    this.isGameOver = false;
    this.moveCount = 0;

    for (int i = 0; i < 3; i++)  {
      snake.addLast(new Point(0,i));
      snakeBody.add(new Point(0, i));
    }

  }

  private MovementStrategy getStrategy(Direction direction) {
    return switch (direction) {
      case Direction.D -> new DownMovementStrategy();
      case Direction.U -> new UpMovementStrategy();
      case Direction.L -> new LeftMovementStrategy();
      case Direction.R -> new RightMovementStrategy();
    };
  }


  public Deque<Point> getBody() {
    return this.snake;
  }


  // This method is responsible for snake movement
  @Override public void moveSnake(Direction direction) {
    if (isGameOver) {
      System.out.println("Game Over!");
      isGameOver = true;
      return;
    }

    // sanity check for direction
    if (direction == Direction.R && currentDirection == Direction.L
    || direction == Direction.L && currentDirection == Direction.R
      || direction == Direction.D && currentDirection == Direction.U
      || direction == Direction.U && currentDirection == Direction.D
    ) {
      System.out.println("Invalid direction!! ");
      direction = currentDirection;
    }

    currentDirection = direction;

    Point head = snake.peekLast();
    MovementStrategy movementStrategy = getStrategy(direction);
    Point newHead = movementStrategy.moveNext(head);
    moveCount += 1;

    // TODO : Handle boundary condition

    if (snakeBody.contains(newHead)) {
      System.out.println("Game Over!");
      isGameOver = true;
      return;
    }

    snake.addLast(newHead);
    snakeBody.add(newHead);

    boolean isGrow = (moveCount % 5) == 0;
    if (!isGrow) {
      Point tail = snake.pollFirst();
      snakeBody.remove(tail);
    }

  }

  // Get the current status of the Game
  @Override public boolean isGameOver() {
    return isGameOver;
  }
}
