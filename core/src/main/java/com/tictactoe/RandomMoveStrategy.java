package com.tictactoe;

import java.util.Random;

public class RandomMoveStrategy implements MoveStrategy {

    private final Random random = new Random();

    @Override
    public int[] nextMove(Board board) {
        int size = board.getSize();
        int row;
        int col;
        do {
            row = random.nextInt(size);
            col = random.nextInt(size);
        } while (board.getSymbol(row, col) != null);

        System.out.println("Random move: (" + row + ", " + col + ")");
        return new int[]{row, col};
    }
}
