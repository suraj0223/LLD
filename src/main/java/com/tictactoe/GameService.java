package com.tictactoe;

import lombok.Getter;

@Getter
public class GameService {

    private final Board board;
    private final Player playerX;
    private final Player playerO;
    private Player currentPlayer;

    public GameService(int size, Player playerX, Player playerO) {
        this.board = Board.getInstance(size);
        this.playerX = playerX;
        this.playerO = playerO;
        this.currentPlayer = playerX;
    }

    public MoveResult playTurn() {
        int[] move = currentPlayer.nextMove(board);
        return makeMove(move[0], move[1]);
    }

    public MoveResult makeMove(int row, int col) {
        board.placePiece(row, col, currentPlayer.getSymbol());

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
        Symbol s = currentPlayer.getSymbol();
        for (int c = 0; c < board.getSize(); c++) {
            if (board.getSymbol(row, c) != s) {
                return false;
            }
        }
        return true;
    }

    private boolean isColumnComplete(int col) {
        Symbol s = currentPlayer.getSymbol();
        for (int r = 0; r < board.getSize(); r++) {
            if (board.getSymbol(r, col) != s) {
                return false;
            }
        }
        return true;
    }

    private boolean isMainDiagonalComplete() {
        Symbol s = currentPlayer.getSymbol();
        for (int i = 0; i < board.getSize(); i++) {
            if (board.getSymbol(i, i) != s) {
                return false;
            }
        }
        return true;
    }

    private boolean isAntiDiagonalComplete() {
        Symbol s = currentPlayer.getSymbol();
        int size = board.getSize();
        for (int i = 0; i < size; i++) {
            if (board.getSymbol(i, size - 1 - i) != s) {
                return false;
            }
        }
        return true;
    }

    private void switchTurn() {
        currentPlayer = (currentPlayer == playerX) ? playerO : playerX;
    }

}
