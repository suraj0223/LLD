package com.tictactoe;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter board size (n for n x n, minimum 3): ");
        int size = scanner.nextInt();

        GameService game = new GameService(size);

        System.out.println("=== Tic-Tac-Toe (" + size + "x" + size + ") ===");
        printBoard(game.getBoard());

        while (true) {
            System.out.println("Player " + game.getCurrentTurn() + "'s turn.");
            System.out.print("Enter row (0-" + (size - 1) + "): ");
            int row = scanner.nextInt();
            System.out.print("Enter col (0-" + (size - 1) + "): ");
            int col = scanner.nextInt();

            try {
                MoveResult result = game.makeMove(row, col);
                printBoard(game.getBoard());

                if (result == MoveResult.WIN) {
                    System.out.println("Player " + game.getBoard().getSymbol(row, col) + " wins!");
                    break;
                } else if (result == MoveResult.DRAW) {
                    System.out.println("It's a draw!");
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid move: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void printBoard(Board board) {
        int size = board.getSize();
        String separator = "-".repeat(size * 4 + 1);

        System.out.println(separator);
        for (int r = 0; r < size; r++) {
            System.out.print("| ");
            for (int c = 0; c < size; c++) {
                Symbol s = board.getSymbol(r, c);
                System.out.print((s == null ? " " : s) + " | ");
            }
            System.out.println();
            System.out.println(separator);
        }
    }
}
