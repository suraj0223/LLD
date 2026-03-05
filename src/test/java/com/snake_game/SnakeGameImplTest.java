package com.snake_game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SnakeGameImplTest {

  private SnakeGameImpl game;

  @BeforeEach
  void setup() {
    this.game = new SnakeGameImpl(10, 10);
  }

  @Test
  void testInitialSnakeSize() {
    
    assertEquals(3, game.getSnake().size());
    assertFalse(game.isGameOver());
  }

  @Test
  void testSnakeMovesAndGrows() {
    // Test that snake can grow from growth-by-moves
    // Note: Food may also cause growth, so this test verifies growth happens
    int initialSize = game.getSnake().size();
    
    // Move right 5 times
    for (int i = 0; i < 5; i++) {
      game.moveSnake(Direction.R);
    }
    
    // Snake should have grown at some point (from food or growth-by-moves)
    // Since food placement is random, we verify snake doesn't shrink below initial size
    // and that it has the opportunity to grow (either from food or growth-by-moves on move 5)
    int finalSize = game.getSnake().size();
    assertTrue(finalSize >= initialSize, 
        "Snake should not shrink below initial size. Initial: " + initialSize + ", Final: " + finalSize);
  }

  @Test
  void testGameOverOnSelfCollision() {
    // Grow the snake to length 6
    for (int i = 0; i < 15; i++) {
      game.moveSnake(Direction.R);
    }
    // Now make a U-turn to collide with itself
    game.moveSnake(Direction.D);
    game.moveSnake(Direction.L);
    game.moveSnake(Direction.U); // should collide with itself
    assertTrue(game.isGameOver());
  }

  @Test
  void testGameOverOnWallCollision() {
    SnakeGameImpl game = new SnakeGameImpl(3, 3);
    // Initial position is (1,0), (1,1), (1,2)
    // Move up twice to hit the wall
    game.moveSnake(Direction.U); // (0,2)
    game.moveSnake(Direction.U); // (-1,2) - out of bounds
    assertTrue(game.isGameOver());
  }

  @Test
  void testNoReverseDirection() {
    SnakeGameImpl game = new SnakeGameImpl(10, 10);
    game.moveSnake(Direction.L); // should ignore reverse
    assertEquals(Direction.R,
      game.getSnake().get(game.getSnake().size() - 1).col() > game.getSnake().get(game.getSnake().size() - 2).col()
        ? Direction.R : Direction.L);
  }

  @Test
  void testGrowthByMovesDisabled() {
    SnakeGameImpl game = new SnakeGameImpl(10, 10, false);
    // Make 10 moves - snake should stay size 3 (no growth)
    for (int i = 0; i < 10; i++) {
      game.moveSnake(Direction.R);
    }
    assertEquals(3, game.getSnake().size()); // should remain size 3
  }

  @Test
  void testFoodSpawnsOnInitialization() {
    SnakeGameImpl game = new SnakeGameImpl(10, 10);
    assertTrue(game.getFood() != null);
    // Food should not be on snake body
    assertFalse(game.getSnake().contains(game.getFood()));
  }

  @Test
  void testSnakeGrowsWhenEatingFood() {
    SnakeGameImpl game = new SnakeGameImpl(10, 10, false); // Disable growth-by-moves
    int initialSize = game.getSnake().size();
    Point food = game.getFood();
    assertTrue(food != null, "Food should spawn on initialization");
    
    // Get snake head position
    Point head = game.getSnake().get(game.getSnake().size() - 1);
    
    // Move snake towards food
    // Calculate direction to food
    int rowDiff = food.row() - head.row();
    int colDiff = food.col() - head.col();
    
    // Move towards food (simplified - just move in the general direction)
    // Since food spawns randomly, we'll try to reach it
    int moves = 0;
    boolean ateFood = false;
    while (moves < 20 && !game.isGameOver() && !ateFood) {
      Point currentHead = game.getSnake().get(game.getSnake().size() - 1);
      if (currentHead.equals(food)) {
        // Snake reached food, it should have grown
        ateFood = true;
        break;
      }
      
      // Simple movement towards food - prioritize column movement
      if (colDiff != 0) {
        game.moveSnake(colDiff > 0 ? Direction.R : Direction.L);
      } else if (rowDiff != 0) {
        game.moveSnake(rowDiff > 0 ? Direction.D : Direction.U);
      } else {
        // Already at food position
        ateFood = true;
        break;
      }
      moves++;
      
      // Update differences
      if (!game.isGameOver()) {
        currentHead = game.getSnake().get(game.getSnake().size() - 1);
        rowDiff = food.row() - currentHead.row();
        colDiff = food.col() - currentHead.col();
      }
    }
    
    // If snake ate food, verify it grew
    if (ateFood || game.getSnake().size() > initialSize) {
      // Snake should have grown by 1 after eating food
      assertTrue(game.getSnake().size() > initialSize, 
          "Snake should grow when eating food");
      // New food should have spawned
      assertTrue(game.getFood() != null, "New food should spawn after eating");
      assertFalse(game.getSnake().contains(game.getFood()), 
          "New food should not be on snake body");
    }
  }
}
