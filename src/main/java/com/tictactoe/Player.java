package com.tictactoe;

import lombok.Getter;

@Getter
public class Player {

    private final Symbol symbol;
    private final MoveStrategy moveStrategy;

    public Player(Symbol symbol, MoveStrategy moveStrategy) {
        this.symbol = symbol;
        this.moveStrategy = moveStrategy;
    }

    public int[] nextMove(Board board) {
        return moveStrategy.nextMove(board);
    }
}
