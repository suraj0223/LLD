package com.snakegame;

/*
interface SnakeGame {
    moveSnake(snakeDirection);
    isGameOver();
}

- every 5 steps it will grows longer + 1
- collids with itself : game over
- if direction is not correct: game will continue and snake will move in same direction
- initial snake size would be 3 initially
- start with top left corner and moving towards right
 */
public class Main {

  public static void main(String[] args) {
    SnakeGameImpl snakeGame = new SnakeGameImpl(10, 10);

    System.out.print("Game Started! \n");

    snakeGame.moveSnake(Direction.R);
    snakeGame.moveSnake(Direction.D);


    System.out.println("Current Snake: ");
    for (var x: snakeGame.getBody()) {
      System.out.println(x.getRow() + ": " + x.getCol());
    }


    snakeGame.moveSnake(Direction.R);
    snakeGame.moveSnake(Direction.D);
    snakeGame.moveSnake(Direction.U);

    System.out.println("Current Snake: ");
    for (var x: snakeGame.getBody()) {
      System.out.print(x.getRow() + ": " + x.getCol() + ", ");
    }

  }
}
