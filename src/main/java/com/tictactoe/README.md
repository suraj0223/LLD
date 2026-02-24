## Design Tic-Tac-Toe Game

> Design the tic-tac-toe game, focusing on the win logic. The system should determine when a player has won by connecting three of their pieces in a row, either horizontally, vertically, or diagonally.

### Requirements
- Players take turns placing their symbol (X or O) on an empty cell of a 3x3 board
- Game should detect a win (three in a row horizontally, vertically, or diagonally)
- Game should detect a draw (board full, no winner)
- Moves on occupied cells or out-of-bounds coordinates should be rejected

### Core Entities
- Board
- Symbol (X, O)
- GameService
- MoveResult

### Class Design

```text
Enum Symbol {
    X,
    O
}

Enum MoveResult {
    WIN,
    DRAW
}

Board
- grid: Symbol[3][3]
+ placePiece(row: int, col: int, symbol: Symbol): void
+ getSymbol(row: int, col: int): Symbol
+ isFull(): boolean

GameService
- board: Board
- currentTurn: Symbol
+ makeMove(row: int, col: int): MoveResult
- checkWin(row: int, col: int): boolean
- switchTurn(): void
```

### Win Check Algorithm
After each move, check only the row, column, and (if applicable) diagonals passing through the placed cell. This is O(n) per move instead of scanning the entire board.

1. Check the row of the last move for three matching symbols
2. Check the column of the last move for three matching symbols
3. If on the main diagonal (row == col), check it
4. If on the anti-diagonal (row + col == 2), check it
5. If no win and board is full, it's a draw

