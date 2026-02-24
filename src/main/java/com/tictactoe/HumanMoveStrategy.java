package com.tictactoe;

import java.util.Scanner;

public class HumanMoveStrategy implements MoveStrategy {

    private final Scanner scanner;

    public HumanMoveStrategy(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public int[] nextMove(Board board) {
        int maxIndex = board.getSize() - 1;
        System.out.print("Enter row (0-" + maxIndex + "): ");
        int row = scanner.nextInt();
        System.out.print("Enter col (0-" + maxIndex + "): ");
        int col = scanner.nextInt();
        return new int[]{row, col};
    }
}
