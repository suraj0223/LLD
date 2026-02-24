package com.tictactoe;

import lombok.Getter;

@Getter
public class GameService {

    private final Board board;
    private Symbol currentTurn;

    public GameService(int size) {
        board = new Board(size);
        currentTurn = Symbol.X;
    }

    public MoveResult makeMove(int row, int col) {
        board.placePiece(row, col, currentTurn);

        if (hasWon(row, col)) {
            return MoveResult.WIN;
        }
        if (board.isFull()) {
            return MoveResult.DRAW;
        }

        switchTurn();
        return null;
    }

    private boolean hasWon(int row, int col) {
        return isRowComplete(row)
                || isColumnComplete(col)
                || (row == col && isMainDiagonalComplete())
                || (row + col == board.getSize() - 1 && isAntiDiagonalComplete());
    }

    private boolean isRowComplete(int row) {
        Symbol s = currentTurn;
        for (int c = 0; c < board.getSize(); c++) {
            if (board.getSymbol(row, c) != s) {
                return false;
            }
        }
        return true;
    }

    private boolean isColumnComplete(int col) {
        Symbol s = currentTurn;
        for (int r = 0; r < board.getSize(); r++) {
            if (board.getSymbol(r, col) != s) {
                return false;
            }
        }
        return true;
    }

    private boolean isMainDiagonalComplete() {
        Symbol s = currentTurn;
        for (int i = 0; i < board.getSize(); i++) {
            if (board.getSymbol(i, i) != s) {
                return false;
            }
        }
        return true;
    }

    private boolean isAntiDiagonalComplete() {
        Symbol s = currentTurn;
        int size = board.getSize();
        for (int i = 0; i < size; i++) {
            if (board.getSymbol(i, size - 1 - i) != s) {
                return false;
            }
        }
        return true;
    }

    private void switchTurn() {
        currentTurn = (currentTurn == Symbol.X) ? Symbol.O : Symbol.X;
    }

}
