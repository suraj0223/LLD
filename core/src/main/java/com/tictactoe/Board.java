package com.tictactoe;

import lombok.Getter;

@Getter
public class Board {

    private static Board instance;

    private final Symbol[][] grid;
    private final int size;

    private Board(int size) {
        if (size < 3) {
            throw new IllegalArgumentException("Board size must be at least 3");
        }
        this.size = size;
        this.grid = new Symbol[size][size];
    }

    public static Board getInstance(int size) {
        if (instance == null) {
            instance = new Board(size);
        }
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    public void placePiece(int row, int col, Symbol symbol) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IllegalArgumentException("Position (" + row + ", " + col + ") is out of bounds");
        }
        if (grid[row][col] != null) {
            throw new IllegalArgumentException("Position (" + row + ", " + col + ") is already occupied");
        }
        grid[row][col] = symbol;
    }

    public Symbol getSymbol(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IllegalArgumentException("Position (" + row + ", " + col + ") is out of bounds");
        }
        return grid[row][col];
    }

    public boolean isFull() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (grid[r][c] == null) {
                    return false;
                }
            }
        }
        return true;
    }
}
