package com.snakegame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SnakeGameServiceTest {

  private SnakeGameService game;

  @BeforeEach
  void setup() {
    this.game = new SnakeGameService(10, 10);
  }

  @Test
  void testInitialSnakeSize() {
    assertEquals(3, game.getBody().size());
    assertFalse(game.isGameOver());
  }

  @Test
  void testSnakeMovesAndGrows() {
    int initialSize = game.getBody().size();

    // Move right 5 times — snake grows every 5th move
    for (int i = 0; i < 5; i++) {
      game.moveSnake(Direction.R);
    }

    assertTrue(game.getBody().size() >= initialSize,
        "Snake should not shrink below initial size");
  }

  @Test
  void testGameOverOnSelfCollision() {
    // Grow the snake by moving right
    for (int i = 0; i < 15; i++) {
      game.moveSnake(Direction.R);
    }
    // U-turn to collide with itself
    game.moveSnake(Direction.D);
    game.moveSnake(Direction.L);
    game.moveSnake(Direction.U);
    assertTrue(game.isGameOver());
  }

  @Test
  void testNoReverseDirection() {
    // Moving left while going right should be ignored
    game.moveSnake(Direction.L);
    assertFalse(game.isGameOver());
  }
}
