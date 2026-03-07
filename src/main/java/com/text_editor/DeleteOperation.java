package com.text_editor;

public class DeleteOperation implements Operation {
    private final Line line;
    private final int row;
    private final int col;
    private StyledCharacter styledCharacter;

    public DeleteOperation(Line line, int row, int col) {
        this.line = line;
        this.row = row;
        this.col = col;
    }

    @Override
    public void execute() {
        this.styledCharacter = line.removeAt(col);
    }

    @Override
    public void reverse() {
        line.insertAt(col, styledCharacter);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public StyledCharacter getDeletedCharacter() {
        return styledCharacter;
    }
}
